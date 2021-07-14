/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.beacons.Eddystone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

class BeaconServiceTest {

    companion object {

        private val EmptyFrame = dataOf()
        private val EddystoneUIDFrame = dataOf(
            0x00, // Type
            0xdc, // Tx Power
            0xf7, 0x82, 0x6d, 0xa6, 0xbc, 0x5b, 0x71, 0xe0, 0x89, 0x3e, // Name Space ID
            0x4e, 0x41, 0x61, 0x46, 0x01, 0x02 // Instance ID
        )

        private fun dataOf(vararg elements: Int) = elements.map { it.toByte() }.toByteArray()
    }

    @Test
    fun invalidBeaconReturnsNull() {
        assertNull(Eddystone.unpack(EmptyFrame))
    }

    @Test
    fun validEddystoneBeaconCreated() {
        val frame = Eddystone.unpack(EddystoneUIDFrame) ?: fail("Invalid Eddystone frame")
        assertEquals("f7826da6bc5b71e0893e", frame.uid.namespace)
        assertEquals("4e4161460102", frame.uid.instance)
        assertEquals(-36, frame.txPower)
    }
}
