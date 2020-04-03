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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.state.StateRepo
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.test.*

abstract class DeviceTest : FlowableTest<DeviceState>() {

    companion object {
        val reconnectionSettings = ConnectionSettings.ReconnectionSettings.Limited(2)
        val connectionSettings = ConnectionSettings(reconnectionSettings)
        val initialRssi = -10
    }

    private lateinit var deviceStateRepo: Device
    private lateinit var connectionManager: MockDeviceConnectionManager

    abstract val deviceHolder: DeviceHolder
    val advertisementData = MockAdvertisementData()
    abstract fun createServices(stateRepo: StateRepo<DeviceState>): List<Service>
    abstract fun createCharacteristic(stateRepo: StateRepo<DeviceState>) : Characteristic
    abstract fun createDescriptor(stateRepo: StateRepo<DeviceState>) : Descriptor
    abstract fun validateCharacteristicUpdated() : Boolean
    abstract fun validateDescriptorUpdated() : Boolean

    @Test
    fun testInitialState() = runBlocking {
        setup(this)
        testWithFlow {
            test {
                assertTrue(it is DeviceState.Disconnected)
                assertEquals(initialRssi, it.deviceInfo.rssi)
            }
        }
    }

    @Test
    fun testConnected() = runBlocking {
        setup(this)
        testWithFlow {
            getDisconnectedState(this)
            connecting(this)
            connect(this)
        }
    }

    @Test
    fun testCancelConnection() = runBlocking {
        setup(this)
        testWithFlow {
            getDisconnectedState(this)
            connecting(this)
            action {
                deviceStateRepo.takeAndChangeState { deviceState ->
                    when (deviceState) {
                        is DeviceState.Connecting -> deviceState.cancelConnection
                        else -> deviceState.remain
                    }
                }
            }
            disconnecting(this)
            disconnect(this)
        }
    }

    @Test
    fun testConnectNotConnectible() = runBlocking {
        setup(this)
        advertisementData.isConnectible = false
        testWithFlow {
            getDisconnectedState(this)
            flowTest.action {
                deviceStateRepo.takeAndChangeState {deviceState ->
                    when (deviceState) {
                        is DeviceState.Disconnected -> {
                            val newState = deviceState.connect()
                            assertEquals(deviceState.remain, newState)
                            newState
                        }
                        else -> deviceState.remain
                    }
                }
            }
        }
    }

    @Test
    fun testDisconnect() = runBlocking {
        setup(this)
        testWithFlow {
            getDisconnectedState(this)
            connecting(this)
            connect(this)
            disconnecting(this)
            disconnect(this)
        }
    }

    @Test
    fun testReconnect() = runBlocking {
        setup(this)
        testWithFlow {
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
                deviceStateRepo.takeAndChangeState { deviceState ->
                    when (deviceState) {
                        is DeviceState.Reconnecting -> deviceState.didConnect
                        else -> deviceState.remain
                    }
                }
            }
            test {
                assertTrue(it is DeviceState.Connected)
            }
        }
    }

    @Test
    fun testReconnectFailed() = runBlocking {
        setup(this)
        testWithFlow {
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
    }

    @Test
    fun testReadRssi() = runBlocking {
        setup(this)
        testWithFlow {
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
                        else -> deviceState.remain
                    }
                }
            }

            test {
                assertTrue(connectionManager.readRssiCompleted.isCompleted)
                assertTrue(it is DeviceState.Connected)
                assertEquals(-20, it.deviceInfo.rssi)
            }
        }
    }

    @Test
    fun testDiscoverDevices() = runBlocking {
        setup(this)
        testWithFlow {
            getDisconnectedState(this)
            connecting(this)
            connect(this)
            action {
                deviceStateRepo.takeAndChangeState { deviceState ->
                    when (deviceState) {
                        is DeviceState.Connected.NoServices -> deviceState.discoverServices
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
    }

    @Test
    fun testHandleActions() = runBlocking {
        setup(this)
        testWithFlow {
            getDisconnectedState(this)
            connecting(this)
            connect(this)
            action {
                deviceStateRepo.takeAndChangeState { deviceState ->
                    when (deviceState) {
                        is DeviceState.Connected.NoServices -> deviceState.discoverServices
                        else -> deviceState.remain
                    }
                }
            }
            test {
                assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
                assertTrue(it is DeviceState.Connected.Discovering)
            }
            val services = createServices(deviceStateRepo)
            val characteristic = createCharacteristic(deviceStateRepo)
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
    }

    private fun setup(coroutineScope: CoroutineScope) {
        deviceStateRepo = Device(connectionSettings, DeviceInfoImpl(deviceHolder, initialRssi, advertisementData), object : BaseDeviceConnectionManager.Builder {

            override fun create(connectionSettings: ConnectionSettings, deviceHolder: DeviceHolder, stateRepo: StateRepo<DeviceState>, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
                connectionManager = MockDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope)
                return connectionManager
            }
        }, coroutineScope)
        flowable.complete(deviceStateRepo.flowable.value)
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
                    is DeviceState.Disconnected -> deviceState.connect()
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
            assertTrue(it is DeviceState.Connected.NoServices)
        }
    }

    private suspend fun disconnecting(flowTest: FlowTest<DeviceState>) {
        flowTest.action {
            deviceStateRepo.takeAndChangeState {deviceState ->
                when (deviceState) {
                    is DeviceState.Connected -> deviceState.disconnecting
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
    deviceHolder: DeviceHolder,
    stateRepo: StateRepo<DeviceState>,
    coroutineScope: CoroutineScope
) : BaseDeviceConnectionManager(connectionSettings, deviceHolder, stateRepo, coroutineScope) {

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

    override suspend fun performAction(action: DeviceAction) {
        performActionCompleted.complete(action)
    }
}