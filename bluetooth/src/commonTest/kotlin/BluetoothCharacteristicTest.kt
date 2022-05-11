/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.test.bluetooth.characteristic
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class BluetoothCharacteristicTest : BluetoothFlowTest<Characteristic?>() {

    override val flow = suspend {
        setup(Setup.CHARACTERISTIC) {
            characteristics {
                characteristic {
                    properties = 0
                }
            }
        }
        bluetooth.devices()[device.identifier].services()[service.uuid].characteristics()[characteristic.uuid]
    }

    @Test
    fun testGetCharacteristic() = testWithFlow {
        scanDevice()
        bluetooth.startScanning()

        test {
            assertNull(it)
        }

        action {
            connectDevice(device)
            discoverService(service, device)
        }
        val characteristic = characteristic
        test {
            assertEquals(characteristic, it)
            assertFalse(characteristic.hasProperty(CharacteristicProperties.Broadcast))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.Read))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.WriteWithoutResponse))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.Write))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.Notify))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.Indicate))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.SignedWrite))
            assertFalse(characteristic.hasProperty(CharacteristicProperties.ExtendedProperties))
        }
    }
}
