/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlin.test.*

abstract class DeviceTest : FlowableTest<DeviceState>() {

    companion object {
        val reconnectionSettings = ConnectionSettings.ReconnectionSettings.Limited(2)
        val connectionSettings = ConnectionSettings(reconnectionSettings)
        val initialRssi = -10
    }

    private lateinit var deviceStateRepo: Device
    private lateinit var connectionManager: MockDeviceConnectionManager

    abstract val deviceInfoHolder: DeviceInfoHolder
    abstract fun createServices(stateRepo: StateRepo<DeviceState>): List<Service>
    abstract fun createCharacteristic(stateRepo: StateRepo<DeviceState>) : Characteristic
    abstract fun createDescriptor(stateRepo: StateRepo<DeviceState>) : Descriptor
    abstract fun validateCharacteristicUpdated() : Boolean
    abstract fun validateDescriptorUpdated() : Boolean

    @BeforeTest
    override fun setUp() {
        super.setUp()
        deviceStateRepo = Device(connectionSettings, deviceInfoHolder, initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(connectionSettings: ConnectionSettings, deviceInfo: DeviceInfoHolder, stateRepo: StateRepo<DeviceState>): BaseDeviceConnectionManager {
                connectionManager = MockDeviceConnectionManager(connectionSettings, deviceInfo, stateRepo)
                return connectionManager
            }
        })
        flowable.complete(deviceStateRepo.flowable.value)
    }

    @Test
    fun testInitialState() = testWithFlow {
        test {
            assertTrue(it is DeviceState.Disconnected)
            assertEquals(initialRssi, it.lastKnownRssi)
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
                when(deviceState) {
                    is DeviceState.Connecting -> deviceState.cancelConnection
                    else -> deviceState.remain
                }
            }
        }
        disconnecting(this)
        disconnect(this)
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
            deviceStateRepo.takeAndChangeState {deviceState ->
                when(deviceState) {
                    is DeviceState.Connected -> deviceState.reconnect
                    else -> deviceState.remain
                }
            }
        }

        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
        }
        action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when(deviceState) {
                    is DeviceState.Reconnecting -> deviceState.didConnect
                    else -> deviceState.remain
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
                    else -> deviceState.remain
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
                    else -> deviceState.remain
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
                    else -> deviceState.remain
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
            assertEquals(initialRssi, deviceStateRepo.peekState().lastKnownRssi)
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> {
                        deviceState.readRssi()
                        deviceState.rssiDidUpdate(-20)
                    }
                    else -> deviceState.remain
                }
            }
        }

        test {
            assertTrue(connectionManager.readRssiCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected)
            assertEquals(-20, it.lastKnownRssi)
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
                    is DeviceState.Connected.Idle -> deviceState.discoverServices
                    else -> deviceState.remain
                }
            }
        }
        test {
            assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected.Discovering)
        }
        val services = createServices(deviceStateRepo)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Discovering -> deviceState.didDiscoverServices(services)
                    else -> deviceState.remain
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
        createServices(deviceStateRepo)
        val characteristic = createCharacteristic(deviceStateRepo)
        action {
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.Idle -> deviceState.handleAction(DeviceAction.Read.Characteristic(characteristic))
                    else -> deviceState.remain
                }
            }
        }
        test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
        }
        val descriptor = CompletableDeferred<Descriptor>()
        action {
            descriptor.complete(createDescriptor(deviceStateRepo))
            deviceStateRepo.takeAndChangeState { deviceState ->
                when (deviceState) {
                    is DeviceState.Connected.HandlingAction -> deviceState.addAction(DeviceAction.Write.Descriptor(null, descriptor.getCompleted()))
                    else -> deviceState.remain
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
                    else -> deviceState.remain
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
                    else -> deviceState.remain
                }
            }
        }
        test {
            assertTrue(validateDescriptorUpdated())
            assertTrue(it is DeviceState.Connected.Idle)
        }
    }

    private fun getDisconnectedState(flowTest: FlowTest<DeviceState>) {
        flowTest.test {
            assertTrue(it is DeviceState.Disconnected)
        }
    }

    private fun getDisconnectingState(flowTest: FlowTest<DeviceState>) {
        flowTest.test {
            assertTrue(connectionManager.disconnectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnecting)
        }
    }

    private suspend fun connecting(flowTest: FlowTest<DeviceState>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnected -> deviceState.connect
                    else -> deviceState.remain
                }
            }
        }
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Connecting)
        }
    }

    private suspend fun connect(flowTest: FlowTest<DeviceState>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when (deviceState) {
                    is DeviceState.Connecting -> deviceState.didConnect
                    else -> deviceState.remain
                }
            }
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(emptyList(), it.services)
        }
    }

    private suspend fun disconnecting(flowTest: FlowTest<DeviceState>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.disconnect
                    else -> deviceState.remain
                }
            }
        }
        getDisconnectingState(flowTest)
    }

    private suspend fun disconnect(flowTest: FlowTest<DeviceState>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when (deviceState) {
                    is DeviceState.Disconnecting -> deviceState.didDisconnect
                    else -> deviceState.remain
                }
            }
        }
        getDisconnectedState(flowTest)
    }

}

internal class MockDeviceConnectionManager(connectionSettings: ConnectionSettings,
                                  deviceInfoHolder: DeviceInfoHolder,
                                  stateRepo: StateRepo<DeviceState>
) : BaseDeviceConnectionManager(connectionSettings, deviceInfoHolder, stateRepo) {

    internal lateinit var connectCompleted: EmptyCompletableDeferred
    internal lateinit var discoverServicesCompleted: EmptyCompletableDeferred
    internal lateinit var disconnectCompleted: EmptyCompletableDeferred
    internal lateinit var readRssiCompleted: EmptyCompletableDeferred
    internal lateinit var performActionCompleted: CompletableDeferred<DeviceAction>

    init {
        reset()
    }

    internal fun reset() {
        connectCompleted = EmptyCompletableDeferred()
        discoverServicesCompleted = EmptyCompletableDeferred()
        disconnectCompleted = EmptyCompletableDeferred()
        readRssiCompleted = EmptyCompletableDeferred()
        performActionCompleted = CompletableDeferred()
    }

    override suspend fun connect() {
        connectCompleted.complete()
    }

    override suspend fun discoverServices() {
        discoverServicesCompleted.complete()
    }

    override suspend fun disconnect() {
        disconnectCompleted.complete()
    }

    override suspend fun readRssi() {
        readRssiCompleted.complete()
    }

    override suspend fun performAction(action: DeviceAction): Boolean {
        performActionCompleted.complete(action)
        return true
    }
}