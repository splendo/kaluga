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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.BluetoothFlowTest
import com.splendo.kaluga.test.base.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DeviceTest : BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithDescriptor, BluetoothFlowTest.DescriptorContext, DeviceState>() {

    override val flowFromTestContext: suspend DescriptorContext.() -> Flow<DeviceState> = { device.state }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithDescriptor, scope: CoroutineScope) -> DescriptorContext =
        { configuration, scope -> DescriptorContext(configuration, scope) }

    @Test
    fun testInitialState() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        test {
            deviceConnectionManagerBuilder.createMock.verify()
            assertIs<ConnectableDeviceState.Disconnected>(it)
            assertEquals(configuration.rssi, device.info.first().rssi)
        }
    }

    @Test
    fun testNotConnectableState() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor(advertisementData = MockAdvertisementData(isConnectable = false))) {
        test {
            deviceConnectionManagerBuilder.createMock.verify(rule = never())
            assertIs<NotConnectableDeviceState>(it)
            assertEquals(configuration.rssi, device.info.first().rssi)
        }
    }

    @Test
    fun testNotConnectableToDisconnectedStateTransition() {
        // Is not connectable initially
        val configuration = Configuration.DeviceWithDescriptor(
            advertisementData = MockAdvertisementData(isConnectable = false),
        )
        testWithFlowAndTestContext(configuration) {
            test {
                deviceConnectionManagerBuilder.createMock.verify(rule = never())
                assertIs<NotConnectableDeviceState>(it)
                assertEquals(configuration.rssi, device.info.first().rssi)
            }
            // 'Advertise' as connectable
            mainAction {
                device.advertisementDataDidUpdate(
                    MockAdvertisementData(isConnectable = true),
                )
                yieldMultiple(2)
            }
            test {
                assertIs<ConnectableDeviceState.Disconnected>(it)
            }
        }
    }

    @Test
    fun testConnected() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
    }

    @Test
    fun testCancelConnection() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        mainAction {
            connectionManager.disconnectMock.on().doExecute { connectionManager.handleDisconnect() }
            connectionManager.cancelConnecting()
            yieldMultiple(2)
        }
        getDisconnectingState()
        getDisconnectedState()
    }

    @Test
    fun testDisconnect() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
        disconnecting()
        disconnect()
    }

    @Test
    fun testReconnect() = testWithFlowAndTestContext(
        Configuration.DeviceWithDescriptor(
            connectionSettings = ConnectionSettings(
                reconnectionSettings = ConnectionSettings.ReconnectionSettings.Always,
            ),
        ),
    ) {
        getDisconnectedState()
        connecting()
        connect()

        mainAction {
            connectionManager.handleDisconnect()
            yieldMultiple(2)
        }
        test {
            connectionManager.connectMock.verify(times = 2)
            assertIs<ConnectableDeviceState.Connecting>(it)
        }
        mainAction {
            connectionManager.handleConnect()
            yieldMultiple(2)
        }
        test {
            assertIs<ConnectableDeviceState.Connected>(it)
        }
    }

    @Test
    fun testDiscoverServices() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
        mainAction {
            connectionManager.startDiscovering()
            yieldMultiple(2)
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<ConnectableDeviceState.Connected.Discovering>(it)
        }
        mainAction {
            discoverService()
            yieldMultiple(2)
        }
        test {
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
            assertEquals(listOf(service), it.services)
        }
    }

    @Test
    fun testHandleActions() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
        mainAction {
            connectionManager.startDiscovering()
            yieldMultiple(2)
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<ConnectableDeviceState.Connected.Discovering>(it)
        }
        mainAction {
            discoverService()
            yieldMultiple(2)
        }
        test {
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
            assertEquals(listOf(service), it.services)
        }

        mainAction {
            characteristic.readValue()
            yieldMultiple(2)
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Read.Characteristic>(captor.lastCaptured)
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Read.Characteristic>(it.action)
            assertEquals(0, it.nextActions.size)
        }

        mainAction {
            descriptor.writeValue(byteArrayOf())
            yieldMultiple(2)
        }

        test {
            connectionManager.performActionMock.verify()
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Read.Characteristic>(it.action)
            assertEquals(1, it.nextActions.size)
        }

        mainAction {
            connectionManager.handleCurrentAction()
            yieldMultiple(2)
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.handleCurrentActionCompletedMock.verify(eq(true), captor)
            assertIs<DeviceAction.Read.Characteristic>(captor.lastCaptured)
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor, 2)
            assertIs<DeviceAction.Write.Descriptor>(captor.lastCaptured)
            assertIs<ConnectableDeviceState.Connected.HandlingAction>(it)
            assertIs<DeviceAction.Write.Descriptor>(it.action)
            assertEquals(0, it.nextActions.size)
        }

        mainAction {
            connectionManager.handleCurrentAction()
            yieldMultiple(2)
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.handleCurrentActionCompletedMock.verify(eq(true), captor, 2)
            assertIs<DeviceAction.Write.Descriptor>(captor.lastCaptured)
        }

        test {
            assertIs<ConnectableDeviceState.Connected.Idle>(it)
        }
    }

    private suspend fun getDisconnectedState() {
        test {
            assertIs<ConnectableDeviceState.Disconnected>(it)
        }
    }

    private suspend fun getDisconnectingState() {
        test {
            connectionManager.disconnectMock.verify()
            assertIs<ConnectableDeviceState.Disconnecting>(it)
        }
    }

    private suspend fun connecting() {
        mainAction {
            connectionManager.startConnecting()
            yieldMultiple(2)
        }
        test {
            connectionManager.connectMock.verify()
            assertIs<ConnectableDeviceState.Connecting>(it)
        }
    }

    private suspend fun connect() {
        mainAction {
            connectionManager.handleConnect()
            yieldMultiple(2)
        }
        test {
            assertEquals(configuration.rssi, device.info.first().rssi)
            assertIs<ConnectableDeviceState.Connected.NoServices>(it)
        }
    }

    private suspend fun disconnecting() {
        mainAction {
            connectionManager.startDisconnecting()
            yieldMultiple(2)
        }
        getDisconnectingState()
    }

    private suspend fun disconnect() {
        mainAction {
            connectionManager.handleDisconnect()
            yieldMultiple(2)
        }
        getDisconnectedState()
    }
}
