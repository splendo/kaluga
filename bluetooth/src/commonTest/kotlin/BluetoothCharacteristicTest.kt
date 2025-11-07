/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BluetoothCharacteristicTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithCharacteristic, BluetoothFlowTest.CharacteristicContext, Characteristic?>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithCharacteristic, scope: CoroutineScope) -> CharacteristicContext =
        { configuration, scope -> CharacteristicContext(configuration, scope) }
    override val flowFromTestContext: suspend CharacteristicContext.() -> Flow<Characteristic?> = {
        bluetooth.scannedDevices()[device.identifier].services().getOrNull(serviceUuid).characteristics().getOrNull(characteristicUuid)
    }

    @Test
    fun testGetCharacteristic() = testWithFlowAndTestContext(
        Configuration.DeviceWithCharacteristic(
            serviceWrapperBuilder = {
                characteristics {
                    characteristic {
                    }
                }
            },
        ),
    ) {
        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertNull(it)
        }

        mainAction {
            connectDevice()
            discoverService()
        }

        test {
            assertEquals(characteristic, it)
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Broadcast))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Read))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.WriteWithoutResponse))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Write))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Notify))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Indicate))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.SignedWrite))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.ExtendedProperties))
        }
    }

    @Test
    fun testProperties() = testWithFlowAndTestContext(
        Configuration.DeviceWithCharacteristic(
            serviceWrapperBuilder = {
                characteristics {
                    characteristic {
                        properties += setOf(CharacteristicProperty.Read, CharacteristicProperty.WriteWithoutResponse)
                    }
                }
            },
        ),
    ) {
        mainAction {
            bluetooth.startScanning()
            scanDevice()
        }
        test {
            assertNull(it)
        }

        mainAction {
            connectDevice()
            discoverService()
        }

        test {
            assertEquals(characteristic, it)
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Broadcast))
            assertTrue(characteristic.hasProperty(CharacteristicProperty.Read))
            assertTrue(characteristic.hasProperty(CharacteristicProperty.WriteWithoutResponse))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Write))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Notify))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.Indicate))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.SignedWrite))
            assertFalse(characteristic.hasProperty(CharacteristicProperty.ExtendedProperties))
        }
    }
}
