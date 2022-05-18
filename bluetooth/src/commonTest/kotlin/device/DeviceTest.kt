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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.BluetoothFlowTest
import com.splendo.kaluga.bluetooth.device.DeviceState.Connected.HandlingAction
import com.splendo.kaluga.bluetooth.device.DeviceState.Connected.Idle
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.mock.matcher.AnyOrNullCaptor
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verify
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.fail

class DeviceTest :
    BluetoothFlowTest<BluetoothFlowTest.Configuration.DeviceWithDescriptor, BluetoothFlowTest.DescriptorContext, DeviceState>() {

    override val flowFromTestContext: suspend DescriptorContext.() -> Device = { device }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration.DeviceWithDescriptor, scope: CoroutineScope) -> DescriptorContext =
        { configuration, scope -> DescriptorContext(configuration, scope) }

    @Test
    fun testInitialState() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        test {
            assertIs<DeviceState.Disconnected>(it)
            assertEquals(configuration.rssi, it.deviceInfo.rssi)
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
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connecting -> deviceState.cancelConnection
                    else -> deviceState.remain()
                }
            }
        }
        disconnecting()
        disconnect()
    }

    @Test
    fun testConnectNotConnectible() = testWithFlowAndTestContext(
        Configuration.DeviceWithDescriptor(
            advertisementData = MockAdvertisementData(isConnectible = false)
        )
    ) {
        getDisconnectedState()
        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnected -> {
                        deviceState.connect(deviceState).also {
                            assertEquals(deviceState.remain(), it)
                        }
                    }
                    else -> deviceState.remain()
                }
            }
        }
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
                ConnectionSettings.ReconnectionSettings.Limited(
                    2
                )
            )
        )
    ) {
        getDisconnectedState()
        connecting()
        connect()

        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain()
                }
            }
        }
        test {
            connectionManager.connectMock.verify(2)
            assertIs<DeviceState.Reconnecting>(it)
            assertEquals(0, it.attempt)
        }
        mainAction {
            connectionManager.handleConnect()
        }
        test {
            assertIs<DeviceState.Connected>(it)
        }
    }

    @Test
    fun testReconnectFailed() = testWithFlowAndTestContext(
        Configuration.DeviceWithDescriptor(
            connectionSettings = ConnectionSettings(
                ConnectionSettings.ReconnectionSettings.Limited(
                    2
                )
            )
        )
    ) {
        getDisconnectedState()
        connecting()
        connect()

        mainAction {
            connectionManager.handleDisconnect()
        }

        test {
            connectionManager.connectMock.verify(2)
            assertIs<DeviceState.Reconnecting>(it)
            assertEquals(0, it.attempt)
        }
        mainAction {
            connectionManager.handleDisconnect()
        }
        test {
            connectionManager.connectMock.verify(3)
            assertIs<DeviceState.Reconnecting>(it)
            assertEquals(1, it.attempt)
        }
        mainAction {
            connectionManager.handleDisconnect()
        }
        test {
            connectionManager.connectMock.verify(3)
            assertIs<DeviceState.Disconnected>(it)
        }
    }

    @Test
    fun testReadRssi() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()

        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> {
                        deviceState.readRssi()
                        deviceState.rssiDidUpdate(-20)
                    }
                    else -> deviceState.remain()
                }
            }
        }

        test {
            connectionManager.readRssiMock.verify()
            assertIs<DeviceState.Connected>(it)
            assertEquals(-20, it.deviceInfo.rssi)
        }
    }

    @Test
    fun testRequestMtu() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()

        mainAction {
            device.useState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> {
                        assertTrue(deviceState.requestMtu(23))
                    }
                    else -> {}
                }
            }

            connectionManager.requestMtuMock.verify(eq(23))
            assertEquals(23, connectionManager.mtu)
        }
    }

    @Test
    fun testDiscoverDevices() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
        mainAction {
            device.useState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.startDiscovering()
                    else -> {}
                }
            }
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<DeviceState.Connected.Discovering>(it)
        }
        mainAction {
            discoverService()
        }
        test {
            assertIs<Idle>(it)
            assertEquals(listOf(service), it.services)
        }
    }

    @Test
    fun testHandleActions() = testWithFlowAndTestContext(Configuration.DeviceWithDescriptor()) {
        getDisconnectedState()
        connecting()
        connect()
        mainAction {
            device.useState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.startDiscovering()
                    else -> {}
                }
            }
        }
        test {
            connectionManager.discoverServicesMock.verify()
            assertIs<DeviceState.Connected.Discovering>(it)
        }
        mainAction {
            discoverService()
        }
        test {
            assertIs<Idle>(it)
            assertEquals(listOf(service), it.services)
        }

        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is Idle -> deviceState.handleAction(
                        DeviceAction.Read.Characteristic(
                            characteristic
                        )
                    )
                    else -> fail("unexpected state")
                }
            }
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor)
            assertIs<DeviceAction.Read.Characteristic>(captor.lastCaptured)
            assertIs<HandlingAction>(it)
            assertIs<DeviceAction.Read.Characteristic>(it.action)
            assertEquals(0, it.nextActions.size)
        }

        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is HandlingAction -> deviceState.addAction(
                        DeviceAction.Write.Descriptor(
                            null,
                            descriptor
                        )
                    )
                    else -> fail("unexpected device state $deviceState")
                }
            }
        }

        test {
            connectionManager.performActionMock.verify()
            assertIs<HandlingAction>(it)
            assertIs<DeviceAction.Read.Characteristic>(it.action)
            assertEquals(1, it.nextActions.size)
        }

        mainAction {
            connectionManager.handleCurrentAction()
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.handleCurrentActionCompletedMock.verify(eq(true), captor)
            assertIs<DeviceAction.Read.Characteristic>(captor.lastCaptured)
        }

        test {
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.performActionMock.verify(captor, 2)
            assertIs<DeviceAction.Write.Descriptor>(captor.lastCaptured)
            assertIs<HandlingAction>(it)
            assertIs<DeviceAction.Write.Descriptor>(it.action)
            assertEquals(0, it.nextActions.size)
        }

        mainAction {
            connectionManager.handleCurrentAction()
            val captor = AnyOrNullCaptor<DeviceAction>()
            connectionManager.handleCurrentActionCompletedMock.verify(eq(true), captor, 2)
            assertIs<DeviceAction.Write.Descriptor>(captor.lastCaptured)
        }

        test {
            assertIs<Idle>(it)
        }
    }

    private suspend fun getDisconnectedState() {
        test {
            assertIs<DeviceState.Disconnected>(it)
        }
    }

    private suspend fun getDisconnectingState() {
        test {
            connectionManager.disconnectMock.verify()
            assertIs<DeviceState.Disconnecting>(it)
        }
    }

    private suspend fun connecting() {
        mainAction {
            device.takeAndChangeState { deviceState ->
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
    }

    private suspend fun connect() {
        mainAction {
            connectionManager.handleConnect()
        }
        test {
            assertEquals(configuration.rssi, it.rssi)
            assertIs<DeviceState.Connected.NoServices>(it)
        }
    }

    private suspend fun disconnecting() {
        mainAction {
            device.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.disconnecting
                    else -> deviceState.remain()
                }
            }
        }
        getDisconnectingState()
    }

    private suspend fun disconnect() {
        mainAction {
            connectionManager.handleDisconnect()
        }
        getDisconnectedState()
    }
}
