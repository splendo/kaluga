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

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BluetoothMultipleCharacteristicValuesTest : BluetoothFlowTest<ByteArray?>() {

    private val firstCharacteristic get() = service.characteristics[0]
    private val secondCharacteristic get() = service.characteristics[1]

    override val flow = suspend {
        setup(Setup.CHARACTERISTIC)
        merge(
            bluetooth.devices()[device.identifier].services()[service.uuid].map {
                println("SR1 🍁️ ${it?.uuid}")
                it
            }.characteristics()[firstCharacteristic.uuid].map {
                println("CH1 🍁️ ${it?.uuid}")
                it
            }.value().map {
                println("V1 🍁️ ${it?.decodeToString()}")
                                                                                                                                    it
            },
            bluetooth.devices()[device.identifier].services()[service.uuid].map {
                println("SR2 🌿️ ${it?.uuid}")
                it
            }.characteristics()[secondCharacteristic.uuid].map {
                println("CH2 🌿 ${it?.uuid}")
                it
            }.value().map {
                println("V2 🌿 ️ ${it?.decodeToString()}")
                it
            }
        ).map {
        println("🪲 ${it?.decodeToString()}")
            it
        }
    }

    @Test
    fun testGetFirstValueFromMultipleCharacteristicsInputFlow() = testWithFlow {

        val newValue = "Test".encodeToByteArray()

        scanDevice()
        bluetooth.startScanning()

        test {
            assertEquals(null, it)
        }
        action {
            connectDevice(device)
            connectionManager.discoverServicesCompleted.get().await()
            discoverService(service, device)
            firstCharacteristic.writeValue(newValue)
        }
        test(skip = 1)  {
            assertTrue(newValue contentEquals it)
        }
    }

    @Test
    fun testGetSecondValueFromMultipleCharacteristicsInputFlow() = testWithFlow {

        val newValue = "Test".encodeToByteArray()

        scanDevice()
        bluetooth.startScanning()

        test(skip = 1) {
            assertEquals(null, it)
        }
        action {
            connectDevice(device)
            connectionManager.discoverServicesCompleted.get().await()
            discoverService(service, device)
            secondCharacteristic.writeValue(newValue)
        }
        test(skip = 1)  {
            assertTrue(newValue contentEquals it)
        }
    }

    @Test
    fun testGetValuesFromMultipleCharacteristicsInputFlow() = testWithFlow {

        val newValue1 = "Test Value 1".encodeToByteArray()
        val newValue2 = "Test Value 2".encodeToByteArray()

        scanDevice()
        bluetooth.startScanning()

        test { assertEquals(null, it) }
        test { assertEquals(null, it) }
        action {
            connectDevice(device)
            connectionManager.discoverServicesCompleted.get().await()
            discoverService(service, device)
            firstCharacteristic.writeValue(newValue1)
        }
        test  {
            println("🤡 ${it?.decodeToString()}")
            assertTrue(newValue1 contentEquals it)
        }
        action {
            secondCharacteristic.writeValue(newValue2)
        }
        test {
            delay(500)
            println("🤡 ${it?.decodeToString()}")
            assertTrue(newValue2 contentEquals it)
        }
    }

    override fun createServiceWrapper() =
        com.splendo.kaluga.test.mock.bluetooth.createServiceWrapper(
            connectionManager.stateRepo,
            uuid = randomUUID(),
            characteristics = listOf(
                randomUUID() to listOf(randomUUID()),
                randomUUID() to listOf(randomUUID())
            )
        )
}
