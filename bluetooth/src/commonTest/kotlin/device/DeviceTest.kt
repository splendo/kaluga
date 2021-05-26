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
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.mock.bluetooth.MockCharacteristic
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import com.splendo.test.mock.bluetooth.MockDescriptor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.Flow

class DeviceTest : BluetoothFlowTest<DeviceState>() {

    companion object {
        private val reconnectionSettings = ConnectionSettings.ReconnectionSettings.Limited(2)
        val connectionSettings = ConnectionSettings(reconnectionSettings)
    }

    private lateinit var deviceStateRepo: Device

    private fun validateCharacteristicUpdated(): Boolean = (characteristic as MockCharacteristic).didUpdate.isCompleted
    private fun validateDescriptorUpdated(): Boolean = (descriptor as MockDescriptor).didUpdate.isCompleted

    override val flow: suspend () -> Flow<DeviceState> = {
        deviceStateRepo = Device(connectionSettings, DeviceInfoImpl(deviceWrapper, initialRssi, advertisementData), object : BaseDeviceConnectionManager.Builder {

            override fun create(connectionSettings: ConnectionSettings, deviceWrapper: DeviceWrapper, stateRepo: DeviceStateFlowRepo): BaseDeviceConnectionManager {
                connectionManager = MockDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo)
                return connectionManager
            }
        }, deviceStateRepo.coroutineContext)

        deviceStateRepo
    }


    @Test
    fun testInitialState() = testWithFlow {
        test {
            assertTrue(it is DeviceState.Disconnected)
            assertEquals(initialRssi, it.deviceInfo.rssi)
        }
    }

    @Test
    fun testConnected() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)
    }

    @Test
    fun testCancelConnection() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connecting -> deviceState.cancelConnection
                    else -> deviceState.remain()
                }
            }
        }
        disconnecting(this)
        disconnect(this)
    }

    @Test
    fun testConnectNotConnectible() = testWithFlow {
        advertisementData.isConnectible = false
        getDisconnectedState(this)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnected -> {
                        val newState = deviceState.connect(deviceState)
                        assertEquals(deviceState.remain(), newState)
                        newState
                    }
                    else -> deviceState.remain()
                }
            }
        }
    }

    @Test
    fun testDisconnect() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)
        disconnecting(this)
        disconnect(this)
    }

    @Test
    fun testReconnect() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)

        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain()
                }
            }
        }

        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
        }
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Reconnecting -> deviceState.didConnect
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected)
        }
    }

    @Test
    fun testReconnectFailed() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)

        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain()
                }
            }
        }

        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
        }
        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Reconnecting -> deviceState.retry()
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(1, it.attempt)
        }
        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Reconnecting -> deviceState.retry()
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertFalse(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnected)
        }
    }

    @Test
    fun testReadRssi() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)

        action {
            assertEquals(initialRssi, deviceStateRepo.peekState().deviceInfo.rssi)
            deviceStateRepo.takeAndChangeState { deviceState ->
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
            assertTrue(connectionManager.readRssiCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected)
            assertEquals(-20, it.deviceInfo.rssi)
        }
    }

    @Test
    fun testDiscoverDevices() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.discoverServices
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected.Discovering)
        }
        val services = listOf(service)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(services, it.services)
        }
    }

    @Test
    fun testHandleActions() = testWithFlow {
        getDisconnectedState(this)
        connecting(this)
        connect(this)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.NoServices -> deviceState.discoverServices
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected.Discovering)
        }
        val services = listOf(service)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(services, it.services)
        }
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Idle -> deviceState.handleAction(DeviceAction.Read.Characteristic(characteristic))
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
        }
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.HandlingAction -> deviceState.addAction(DeviceAction.Write.Descriptor(null, descriptor))
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
        }
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.HandlingAction -> deviceState.actionCompleted
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(validateCharacteristicUpdated())
            assertTrue(it is DeviceState.Connected.HandlingAction)
        }
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.HandlingAction -> deviceState.actionCompleted
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertTrue(validateDescriptorUpdated())
            assertTrue(it is DeviceState.Connected.Idle)
        }
    }

    private suspend fun getDisconnectedState(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.test {
            assertTrue(it is DeviceState.Disconnected)
        }
    }

    private suspend fun getDisconnectingState(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.test {
            assertTrue(connectionManager.disconnectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnecting)
        }
    }

    private suspend fun connecting(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnected -> deviceState.connect(deviceState)
                    else -> deviceState.remain()
                }
            }
        }
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Connecting)
        }
    }

    private suspend fun connect(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connecting -> deviceState.didConnect
                    else -> deviceState.remain()
                }
            }
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected.NoServices)
        }
    }

    private suspend fun disconnecting(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.disconnecting
                    else -> deviceState.remain()
                }
            }
        }
        getDisconnectingState(flowTest)
    }

    private suspend fun disconnect(flowTest: FlowTest<DeviceState, Flow<DeviceState>>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnecting -> deviceState.didDisconnect
                    else -> deviceState.remain()
                }
            }
        }
        getDisconnectedState(flowTest)
    }
}


