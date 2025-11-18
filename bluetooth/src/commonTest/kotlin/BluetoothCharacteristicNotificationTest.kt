/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.test.base.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.mock.verifyWithin
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager.ActionCompleted
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class BluetoothCharacteristicNotificationTest :
    BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithCharacteristic, BluetoothFlowTest.CharacteristicContext, DeviceState>() {

    override val createTestContextWithConfiguration: suspend (Configuration.DeviceWithCharacteristic, CoroutineScope) -> CharacteristicContext = { configuration, scope ->
        CharacteristicContext(configuration, scope)
    }
    override val flowFromTestContext: suspend CharacteristicContext.() -> Flow<DeviceState> = { device.state }

    @Test
    fun testEnableNotification() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()
        val subscription = CompletableDeferred<RemoteCharacteristic.Subscription>()
        enableNotifications(subscription)
        subscription.await().hasSubscribedSuccessfully.await()
    }

    @Test
    fun testEnableNotificationWhenAlreadyEnabled() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()

        val subscription = CompletableDeferred<RemoteCharacteristic.Subscription>()
        enableNotifications(subscription)
        subscription.await().hasSubscribedSuccessfully.await()
        mainAction {
            assertIs<GattResponse.WriteSuccess>(characteristic.subscribe {}.hasSubscribedSuccessfully.getCompleted())
            assertTrue(characteristic.isNotifying)
        }
    }

    @Test
    fun testDisableNotification() = testWithFlowAndTestContext(Configuration.DeviceWithCharacteristic()) {
        connect()
        discover()
        val subscription = CompletableDeferred<RemoteCharacteristic.Subscription>()
        enableNotifications(subscription)

        mainAction {
            subscription.await().unsubscribe()
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction<*>>()
            connectionManager.performActionMock.verifyWithin(value = captor, times = 2)
            assertIs<DeviceAction.Notification.Disable>(captor.lastCaptured)
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Disable>(it.action)
        }
        mainAction {
            connectionManager.handleCurrentAction()
            val captor = AnyOrNullCaptor<ActionCompleted<*>>()
            connectionManager.handleCurrentActionCompletedMock.verify(captor, 2)
            assertIs<GattResponse.WriteSuccess>(captor.lastCaptured?.response)
            assertIs<DeviceAction.Notification.Disable>(captor.lastCaptured?.action)
        }
        test {
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
            assertFalse(characteristic.isNotifying)
        }
    }

    @Test
    fun testFailedToEnableNotification() = testWithFlowAndTestContext(
        Configuration.DeviceWithCharacteristic(
            willActionsSucceed = false,
        ),
    ) {
        connect()
        discover()

        mainAction {
            assertFalse(characteristic.isNotifying)
            characteristic.subscribe {}
            yieldMultiple(2)
        }
        test {
            val captor = AnyOrNullCaptor<DeviceAction<*>>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Notification.Enable>(captor.lastCaptured)
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Enable>(it.action)
            assertFalse(characteristic.isNotifying)
        }
    }

    private suspend fun connect() {
        test {
            assertIs<ConnectableDeviceState.Disconnected>(it)
        }
        mainAction {
            connectionManager.startConnecting()
            yieldMultiple(2)
        }
        test {
            connectionManager.connectMock.verify()
            assertIs<ConnectableDeviceState.Connecting>(it)
        }
        mainAction {
            connectionManager.handleConnect()
        }
        test {
            assertIs<ConnectableDeviceState.Connected.NoServices>(it)
        }
    }

    private suspend fun discover() {
        mainAction {
            connectionManager.startDiscovering()
            yieldMultiple(2)
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<ConnectableDeviceState.Connected.Discovering>(it)
        }
        mainAction {
            connectionManager.discover(listOf(service))
        }
        test {
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
            assertEquals(listOf(service), it.services)
        }
    }

    private suspend fun enableNotifications(subscription: CompletableDeferred<RemoteCharacteristic.Subscription>) {
        mainAction {
            assertFalse(characteristic.isNotifying, "Notifications already enabled!")
            subscription.complete(characteristic.subscribe { })
            yieldMultiple(2)
        }
        test {
            val captor = AnyOrNullCaptor<DeviceAction<*>>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Notification.Enable>(captor.lastCaptured)
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Notification.Enable>(it.action)
        }
        mainAction {
            connectionManager.handleCurrentAction()
        }
        test {
            assertTrue(characteristic.isNotifying)
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
        }
    }
}
