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

import com.splendo.kaluga.base.utils.firstInstance
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class BluetoothCharacteristicNotificationTest : BluetoothFlowTest<DeviceState>() {

    override val flow = suspend {
        setup(Setup.CHARACTERISTIC)
        connectionManager.willActionSucceed.value = true
        device
    }

    @Test
    fun testEnableNotification() = testWithFlow {
        connect()
        discover()
        enableNotifications()
    }

    @Test
    fun testEnableNotificationWhenAlreadyEnabled() = testWithFlow {
        connect()
        discover()

        enableNotifications()
        val characteristic = characteristic
        action {
            assertNull(characteristic.enableNotification())
            assertTrue(characteristic.isNotifying)
        }
    }

    @Test
    fun testDisableNotification() = testWithFlow {
        connect()
        discover()

        val characteristic = enableNotifications()
        action {
            characteristic.disableNotification()
        }

        val handledAction = connectionManager.handledAction
        test {
            assertIs<DeviceState.Connected.HandlingAction>(it)
            assertNotNull(handledAction.firstInstance<DeviceAction.Notification.Disable>())
            assertFalse(characteristic.isNotifying)
        }
        test {
            assertIs<DeviceState.Connected.Idle>(it)
        }
    }

    @Test
    fun testFailedToEnableNotification() = testWithFlow {
        connect()
        discover()

        val characteristic = characteristic
        assertFalse(characteristic.isNotifying)
        connectionManager.willActionSucceed.value = false
        action {
            characteristic.enableNotification()
        }
        val handledAction = connectionManager.handledAction
        test {
            val action = handledAction.firstInstance<DeviceAction.Notification.Enable>()
            assertNotNull(action)
            assertFalse(action.completed.await())
            assertFalse(characteristic.isNotifying)
        }
    }

    private suspend fun connect() {
        action {
            device.takeAndChangeState { deviceState ->
                println("state: $deviceState")
                when (deviceState) {
                    is DeviceState.Disconnected -> deviceState.connect(deviceState)
                    else -> fail("$deviceState is not expected")
                }
            }
        }
        val connectCompleted = connectionManager.connectCompleted.get()
        test {
            connectCompleted.await()
            assertIs<DeviceState.Connecting>(it)
        }
        action {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connecting -> deviceState.didConnect
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertIs<DeviceState.Connected.NoServices>(it)
        }
    }

    private suspend fun discover() {
        action {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.discoverServices
                    else -> deviceState.remain()
                }
            }
        }
        val discoverServicesCompleted = connectionManager.discoverServicesCompleted.get()
        test {
            assertTrue(discoverServicesCompleted.isCompleted)
            assertIs<DeviceState.Connected.Discovering>(it)
        }
        val services = listOf(service)
        action {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain()
                }
            }
        }
        val service = service
        test {
            assertIs<DeviceState.Connected.Idle>(it)
            assertEquals(listOf(service), it.services)
        }
    }

    private suspend fun enableNotifications(): Characteristic {
        val characteristic = characteristic
        assertFalse(characteristic.isNotifying, "Notifications already enabled!")
        action {
            characteristic.enableNotification()
        }
        val handledAction = connectionManager.handledAction
        test {
            assertIs<DeviceState.Connected.HandlingAction>(it)
            assertNotNull(handledAction.firstInstance<DeviceAction.Notification.Enable>())
            assertTrue(characteristic.isNotifying)
        }
        test {
            assertIs<DeviceState.Connected.Idle>(it)
        }
        return characteristic
    }
}
