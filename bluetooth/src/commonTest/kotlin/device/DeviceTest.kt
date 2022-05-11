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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothFlowTest
import com.splendo.kaluga.bluetooth.device.DeviceState.Connected.HandlingAction
import com.splendo.kaluga.bluetooth.device.DeviceState.Connected.Idle
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class DeviceTest : BluetoothFlowTest<DeviceState>() {

    companion object {
        private val reconnectionSettings = ConnectionSettings.ReconnectionSettings.Limited(2)
        val connectionSettings = ConnectionSettings(reconnectionSettings)
    }

    private lateinit var deviceStateRepo: Device

    override val flow = suspend {
        setup(Setup.DESCRIPTOR)
        val connectionSettings = connectionSettings
        val deviceWrapper = deviceWrapper
        val rssi = rssi
        val advertisementData = advertisementData

        val deviceStateRepo = Device(
            connectionSettings, DeviceInfoImpl(deviceWrapper, rssi, advertisementData),
            object : BaseDeviceConnectionManager.Builder {

                override fun create(connectionSettings: ConnectionSettings, deviceWrapper: DeviceWrapper, stateRepo: DeviceStateFlowRepo): BaseDeviceConnectionManager {
                    return MockDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo)
                }
            },
            coroutineContext
        )
        this.deviceStateRepo = deviceStateRepo
        connectionManager = deviceStateRepo.peekState().connectionManager as MockDeviceConnectionManager
        deviceStateRepo
    }

    @Test
    fun testInitialState() = testWithFlow {
        val rssi = rssi
        test {
            assertIs<DeviceState.Disconnected>(it)
            assertEquals(rssi, it.deviceInfo.rssi)
        }
    }

    @Test
    fun testConnected() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()
    }

    @Test
    fun testCancelConnection() = testWithFlow {
        getDisconnectedState()
        connecting()
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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
    fun testConnectNotConnectible() = testWithFlow {
        advertisementData.isConnectible = false
        getDisconnectedState()
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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
    fun testDisconnect() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()
        disconnecting()
        disconnect()
    }

    @Test
    fun testReconnect() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()

        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain()
                }
            }
        }
        val connectCompleted = connectionManager.connectCompleted.get()
        test {
            assertTrue(connectCompleted.isCompleted)
            assertIs<DeviceState.Reconnecting>(it)
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
            assertIs<DeviceState.Connected>(it)
        }
    }

    @Test
    fun testReconnectFailed() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()

        action {
            connectionManager.reset()
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain()
                }
            }
        }

        val connectCompleted = connectionManager.connectCompleted.get()
        test {
            assertTrue(connectCompleted.isCompleted)
            assertIs<DeviceState.Reconnecting>(it)
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
        val connectCompletedSecond = connectionManager.connectCompleted.get()
        test {
            assertTrue(connectCompletedSecond.isCompleted)
            assertIs<DeviceState.Reconnecting>(it)
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
        val connectCompletedThird = connectionManager.connectCompleted.get()
        test {
            assertFalse(connectCompletedThird.isCompleted)
            assertIs<DeviceState.Disconnected>(it)
        }
    }

    @Test
    fun testReadRssi() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()

        action {
            assertEquals(rssi, deviceStateRepo.peekState().deviceInfo.rssi)
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

        val readRssiCompleted = connectionManager.readRssiCompleted.get()
        test {
            assertTrue(readRssiCompleted.isCompleted)
            assertIs<DeviceState.Connected>(it)
            assertEquals(-20, it.deviceInfo.rssi)
        }
    }

    @Test
    fun testRequestMtu() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()

        action {
            deviceStateRepo.useState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> {
                        assertTrue(deviceState.requestMtu(23))
                        connectionManager.handleNewMtu(23)
                    }
                    else -> {}
                }
            }
        }

        val requestMtuCompleted = connectionManager.requestMtuCompleted.get()
        assertTrue(requestMtuCompleted.isCompleted)
        assertEquals(23, connectionManager.mtu)
    }

    @Test
    fun testDiscoverDevices() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain()
                }
            }
        }
        test {
            assertIs<Idle>(it)
            assertEquals(services, it.services)
        }
    }

    @Test
    fun testHandleActions() = testWithFlow {
        getDisconnectedState()
        connecting()
        connect()
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain()
                }
            }
        }
        val service = service
        test {
            assertIs<Idle>(it)
            assertEquals(listOf(service), it.services)
        }

        connectionManager.waitAfterHandlingAction[DeviceAction.Read.Characteristic::class] = EmptyCompletableDeferred()
        connectionManager.waitAfterHandlingAction[DeviceAction.Write.Descriptor::class] = EmptyCompletableDeferred()

        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is Idle -> deviceState.handleAction(DeviceAction.Read.Characteristic(characteristic))
                    else -> fail("unexpected state")
                }
            }
        }

        connectionManager.performActionStarted.get().await()

        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is HandlingAction -> deviceState.addAction(DeviceAction.Write.Descriptor(null, descriptor))
                    else -> fail("unexpected device state $deviceState")
                }
            }
        }

        action {
            connectionManager.waitAfterHandlingAction[DeviceAction.Read.Characteristic::class]?.complete()
        }

        val handledAction = connectionManager.handledAction
        test {
            assertIs<HandlingAction>(it)
            assertTrue(handledAction.first() is DeviceAction.Read)
        }

        action {
            connectionManager.waitAfterHandlingAction[DeviceAction.Write.Descriptor::class]?.complete()
        }

        val handledActionSecond = connectionManager.handledAction
        test {
            // filter because the second event might not be written yet
            assertTrue(handledActionSecond.filter { action -> action !is DeviceAction.Read }.first() is DeviceAction.Write)
            assertIs<HandlingAction>(it)
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
        val disconnectCompleted = connectionManager.disconnectCompleted.get()
        test {
            disconnectCompleted.await()
            assertIs<DeviceState.Disconnecting>(it)
        }
    }

    private suspend fun connecting() {
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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
    }

    private suspend fun connect() {
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
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

    private suspend fun disconnecting() {
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.disconnecting
                    else -> deviceState.remain()
                }
            }
        }
        getDisconnectingState()
    }

    private suspend fun disconnect() {
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnecting -> deviceState.didDisconnect
                    else -> deviceState.remain()
                }
            }
        }
        getDisconnectedState()
    }
}
