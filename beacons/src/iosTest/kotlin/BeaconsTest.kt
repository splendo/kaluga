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

package kotlin

import com.splendo.kaluga.beacons.Beacon
import com.splendo.kaluga.beacons.Eddystone
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BeaconsTest {

    companion object {
        private fun dataOf(vararg elements: Int) = elements.map { it.toByte() }.toByteArray()
        val EmptyFrame = dataOf()
        val EddystoneUIDFrame = dataOf(
            0x00, // Type
            0xdc, // Tx Power
            0xf7, 0x82, 0x6d, 0xa6, 0xbc, 0x5b, 0x71, 0xe0, 0x89, 0x3e, // Name Space ID
            0x4e, 0x41, 0x61, 0x46, 0x57, 0x4d // Instance ID
        )
    }

    @Test
    fun `Invalid beacon returns null`() {
        val beacon = Beacon.init(
            Identifier("5384002F-6815-43F8-BCAA-E0EFE9C0351C"),
            mapOf(Pair(UUID.UUIDWithString(Eddystone.ServiceUUID), EmptyFrame))
        )
        assertNull(beacon)
        assertNull(beacon?.beaconID)
    }

    @Test
    fun `Valid Eddystone beacon created`() {
        val beacon = Beacon.init(
            Identifier("5384002F-6815-43F8-BCAA-E0EFE9C0351C"),
            mapOf(Pair(UUID.UUIDWithString(Eddystone.ServiceUUID), EddystoneUIDFrame))
        )
        assertNotNull(beacon)
        assertNotNull(beacon.beaconID)
    }
}
