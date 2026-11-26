/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.AttMtu
import com.splendo.kaluga.bluetooth.server.TestCentral.Instruction
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * The central writes a typed value too long for one request at the default MTU, as a long write: a series of prepared
 * writes, then an execute, whose parts the central checks come back as it sent them. At a large MTU the value fits in
 * one request.
 */
class CentralLongWriteTest : CentralTest() {

    private val record = Record(0x0102u, "a label long enough to need several prepared writes", 0x1122334455667788u)

    @Test
    fun longWriteOfATypedValueAtTheDefaultMtu() = longWriteOfATypedValue(AttMtu.Default)

    @Test
    fun longWriteOfATypedValueAtALargeMtu() = longWriteOfATypedValue(AttMtu.Large)

    private fun longWriteOfATypedValue(mtu: AttMtu) = centralTest(mtu = mtu) {
        assertTrue(record.bytes.size > 3 * TestCentral.STAGE_SIZE)
        stage(record.bytes)
        assertEquals(0, status(Instruction.Write(TestCentral.TYPED)))
        assertEquals(record, within(10.seconds, "typed write") { typedWrites.receive() })
    }

    @Test
    fun longWriteOfATypedValueWithoutItsTailAtTheDefaultMtu() = longWriteOfATypedValueWithoutItsTail(AttMtu.Default)

    @Test
    fun longWriteOfATypedValueWithoutItsTailAtALargeMtu() = longWriteOfATypedValueWithoutItsTail(AttMtu.Large)

    private fun longWriteOfATypedValueWithoutItsTail(mtu: AttMtu) = centralTest(mtu = mtu) {
        val withoutTail = record.copy(serial = null)
        stage(withoutTail.bytes)
        assertEquals(0, status(Instruction.Write(TestCentral.TYPED)))
        assertEquals(withoutTail, within(10.seconds, "typed write") { typedWrites.receive() })
    }

    // Each Write Request starts the value over, so a value cut off inside its tail never reaches the delegate.
    @Test
    fun valueEndingInsideItsTailIsNotWrittenAtTheDefaultMtu() = valueEndingInsideItsTailIsNotWritten(AttMtu.Default)

    @Test
    fun valueEndingInsideItsTailIsNotWrittenAtALargeMtu() = valueEndingInsideItsTailIsNotWritten(AttMtu.Large)

    private fun valueEndingInsideItsTailIsNotWritten(mtu: AttMtu) = centralTest(mtu = mtu) {
        stage(record.bytes.copyOf(record.bytes.size - 3))
        assertEquals(0, status(Instruction.Write(TestCentral.TYPED)))
        assertNull(withTimeoutOrNull(1.seconds) { typedWrites.receive() })
        assertNull(typedFailures.tryReceive().getOrNull())
    }
}
