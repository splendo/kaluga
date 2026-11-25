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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.NetsimPeripheral.Command
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.serialization.BytesFormat
import kotlinx.serialization.Serializable
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

/**
 * A typed value whose layout ends in `@Trailing` fields, which the peripheral sends without its tail, with part of
 * it and in full. The value is read the way a generated client reads a layout without a view: decoded by [BytesFormat]
 * through [RemoteAttribute.read].
 */
class NetsimTrailingLayoutTest : NetsimTest() {

    @Serializable
    data class DeviceStatus(@Size(Length.`8_BIT`) @Unsigned val battery: Int, val voltage: UShort, @Trailing val serviceVersion: UShort?, @Trailing val serial: ULong?) {
        val bytes: ByteArray get() = BytesFormat.encodeToByteArray(serializer(), this)
    }

    private val status = DeviceStatus(battery = 80, voltage = 3300u, serviceVersion = 0x0102u, serial = 0x1122334455667788u)

    @Test
    fun withoutTheTail() = reads(status.copy(serviceVersion = null, serial = null))

    @Test
    fun withPartOfTheTail() = reads(status.copy(serial = null))

    @Test
    fun withTheWholeTail() = reads(status)

    @Test
    fun endingInsideATrailingField() = netsimTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.SetLongValueTo(status.bytes.copyOf(status.bytes.size - 3)))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { attributes.long.read(DeviceStatus.serializer()) }
        assertNull(attributes.long.readOrNull(DeviceStatus.serializer()))
    }

    private fun reads(expected: DeviceStatus) = netsimTest {
        val attributes = connectAndDiscover()
        attributes.send(Command.SetLongValueTo(expected.bytes))
        assertEquals(expected, within(10.seconds, "read") { attributes.long.read(DeviceStatus.serializer()) })
    }
}
