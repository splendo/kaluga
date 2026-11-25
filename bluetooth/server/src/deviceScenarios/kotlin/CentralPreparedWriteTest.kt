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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.server.TestCentral.Instruction
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

/**
 * Prepared writes that do not end in a regular execute: parts at the wrong offset, a cancelled queue, and a queue
 * the link drops before it is executed.
 */
class CentralPreparedWriteTest : CentralTest() {

    private val first = Record(0x0A0Bu, "the first value of the typed characteristic", null)
    private val second = Record(0x0C0Du, "the second value", 0x0102030405060708u)

    // A part whose offset leaves a gap is not appended to the parts before it: the server answers the execute with
    // an error, as validating the offsets is left to the execute.
    @Test
    fun partAtTheWrongOffset() = centralTest {
        stage(first.bytes)
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 0, start = 0, length = 18)))
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 22, start = 18, length = 18)))
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 40, start = 36, length = first.bytes.size - 36)))
        assertEquals(GattResponse.InvalidOffset.statusCode, status(Instruction.Execute(write = true)))
        assertNull(withTimeoutOrNull(1.seconds) { typedWrites.receive() })
    }

    @Test
    fun cancelledQueue() = centralTest {
        stage(first.bytes)
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 0, start = 0, length = 18)))
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 18, start = 18, length = 18)))
        assertEquals(0, status(Instruction.Execute(write = false)))
        assertNull(withTimeoutOrNull(1.seconds) { typedWrites.receive() })

        stage(second.bytes)
        assertEquals(0, status(Instruction.Write(TestCentral.TYPED)))
        assertEquals(second, within(10.seconds, "typed write after the cancel") { typedWrites.receive() })
    }

    @Test
    fun queueCutOffByADisconnect() = centralTest {
        stage(first.bytes)
        assertEquals(0, status(Instruction.Prepare(TestCentral.TYPED, offset = 0, start = 0, length = 18)))
        assertEquals(0, status(Instruction.Disconnect))
        awaitCentralDisconnected()
        assertNull(withTimeoutOrNull(1.seconds) { typedWrites.receive() })

        awaitCentral()
        stage(second.bytes)
        assertEquals(0, status(Instruction.Write(TestCentral.TYPED)))
        assertEquals(second, within(10.seconds, "typed write on the next connection") { typedWrites.receive() })
    }
}
