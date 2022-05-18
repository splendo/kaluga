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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.test.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class BluetoothCharacteristicNotificationTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithCharacteristic, BluetoothFlowTest.CharacteristicContext, DeviceState>() {

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithCharacteristic, scope: CoroutineScope) -> CharacteristicContext = { configuration, scope -> CharacteristicContext(configuration, scope) }
    override val flowFromTestContext: suspend CharacteristicContext.() -> Flow<DeviceState> = { device.filterOnlyImportant() }

    @Test
    fun testEnableNotification() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()
        enableNotifications()
    }

    @Test
    fun testEnableNotificationWhenAlreadyEnabled() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()

        enableNotifications()
        mainAction {
            assertNull(characteristic.enableNotification())
            assertTrue(characteristic.isNotifying)
        }
    }

    @Test
    fun testDisableNotification() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()
        enableNotifications()

        mainAction {
            characteristic.disableNotification()
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor, 2)
            assertIs<DeviceAction.Notification.Disable>(captor.lastCaptured)
            assertIs<DeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Disable>(it.action)
        }
        mainAction {
            connectionManager.handleCurrentAction()
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.handleCurrentActionCompletedMock.verify(ParameterMatcher.eq(true), captor, 2)
            assertIs<DeviceAction.Notification.Disable>(captor.lastCaptured)
        }
        test {
            assertIs<DeviceState.Connected.Idle>(it)
            assertFalse(characteristic.isNotifying)
        }
    }

    @Test
    fun testFailedToEnableNotification() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic(willActionsSucceed = false)) {
        connect()
        discover()

        mainAction {
            assertFalse(characteristic.isNotifying)
            characteristic.enableNotification()
        }
        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Notification.Enable>(captor.lastCaptured)
            assertIs<DeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Enable>(it.action)
            assertFalse(characteristic.isNotifying)
        }
    }

    private suspend fun connect() {
        test {
            assertIs<DeviceState.Disconnected>(it)
        }
        mainAction {
            device.takeAndChangeState { deviceState ->
                println("state: $deviceState")
                when (deviceState) {
                    is DeviceState.Disconnected -> deviceState.connect(deviceState)
                    else -> fail("$deviceState is not expected")
                }
            }
        }
        test {
            connectionManager.connectMock.verify()
            assertIs<DeviceState.Connecting>(it)
        }
        mainAction {
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
        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.discoverServices
                    else -> deviceState.remain()
                }
            }
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<DeviceState.Connected.Discovering>(it)
        }
        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(listOf(service))
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertIs<DeviceState.Connected.Idle>(it)
            assertEquals(listOf(service), it.services)
        }
    }

    private suspend fun enableNotifications() {
        mainAction {
            assertFalse(characteristic.isNotifying, "Notifications already enabled!")
            characteristic.enableNotification()
        }
        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Notification.Enable>(captor.lastCaptured)
            assertIs<DeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Enable>(it.action)
        }
        mainAction {
            connectionManager.handleCurrentAction()
        }
        test {
            assertTrue(characteristic.isNotifying)
            assertIs<DeviceState.Connected.Idle>(it)
        }
    }
}
