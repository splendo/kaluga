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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.uuidFrom
import kotlin.test.Test
import kotlin.test.assertEquals

class ServiceWrapperBuilderTest {

    @Test
    fun testBuilder() {
        val serviceUUID = uuidFrom("180A")
        val characteristicUUID = uuidFrom("180B")
        val descriptorUUID = uuidFrom("180C")

        val builder: ServiceWrapperBuilder.() -> Unit = {
            uuid = serviceUUID
            characteristics {
                characteristic {
                    uuid = characteristicUUID
                    properties = 0x42
                    descriptors {
                        descriptor(descriptorUUID)
                    }
                }
            }
        }

        val wrapper = createServiceWrapper(builder)

        assertEquals(serviceUUID, wrapper.uuid)
        assertEquals(expected = 1, wrapper.characteristics.size)
        val characteristic = wrapper.characteristics.first()
        assertEquals(characteristicUUID, characteristic.uuid)
        assertEquals(expected = 0x42, characteristic.properties)
        val descriptor = characteristic.descriptors.first()
        assertEquals(descriptorUUID, descriptor.uuid)
    }
}
