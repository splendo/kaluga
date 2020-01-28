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
import com.splendo.kaluga.state.StateRepoAccesor
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

    private lateinit var connectionManager: MockDeviceConnectionManager

    abstract val deviceInfoHolder: DeviceInfoHolder
    abstract fun createServices(repoAccessor: StateRepoAccesor<DeviceState>): List<Service>
    abstract fun createCharacteristic(repoAccessor: StateRepoAccesor<DeviceState>) : Characteristic
    abstract fun createDescriptor(repoAccessor: StateRepoAccesor<DeviceState>) : Descriptor
    abstract fun validateCharacteristicUpdated() : Boolean
    abstract fun validateDescriptorUpdated() : Boolean

    @BeforeTest
    override fun setUp() {
        super.setUp()
        flowable.complete(Device(connectionSettings, deviceInfoHolder, initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(connectionSettings: ConnectionSettings, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): BaseDeviceConnectionManager {
                connectionManager = MockDeviceConnectionManager(connectionSettings, deviceInfo, repoAccessor)
                return connectionManager
            }
        }).flowable.value)
    }

    @Test
    fun testInitialState() = runBlockingWithFlow { flowTest ->
        flowTest.test {
            assertTrue(it is DeviceState.Disconnected)
            assertEquals(initialRssi, it.lastKnownRssi)
        }
    }

    @Test
    fun testConnected() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState(it)
        val connectingState = connecting(disconnectedState, it)
        connect(connectingState, it)
    }

    @Test
    fun testCancelConnection() = runBlockingWithFlow {
        val connectingState = connecting(getDisconnectedState(it), it)
        it.action {
            connectingState.cancelConnection()
        }
        disconnect(getDisconnectingState(it), it)
    }

    @Test
    fun testDisconnect() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState(it)
        val connectingState = connecting(disconnectedState, it)
        val connectedState = connect(connectingState, it)
        val disconnectingState = disconnecting(connectedState, it)
        disconnect(disconnectingState, it)
    }

    @Test
    fun testReconnect() = runBlockingWithFlow { flowTest ->
        val disconnectedState = getDisconnectedState(flowTest)
        val connectingState = connecting(disconnectedState, flowTest)
        val connectedState = connect(connectingState, flowTest)

        flowTest.action {
            connectionManager.reset()
            connectedState.reconnect()
        }

        val reconnecting = CompletableDeferred<DeviceState.Reconnecting>()
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
            reconnecting.complete(it)
        }
        flowTest.action {
            reconnecting.getCompleted().didConnect()
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected)
        }
    }

    @Test
    fun testReconnectFailed() = runBlockingWithFlow { flowTest ->
        val disconnectedState = getDisconnectedState(flowTest)
        val connectingState = connecting(disconnectedState, flowTest)
        val connectedState = connect(connectingState, flowTest)

        flowTest.action {
            connectionManager.reset()
            connectedState.reconnect()
        }

        val reconnecting1 = CompletableDeferred<DeviceState.Reconnecting>()
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
            reconnecting1.complete(it)
        }
        flowTest.action {
            connectionManager.reset()
            reconnecting1.getCompleted().retry()
        }
        val reconnecting2 = CompletableDeferred<DeviceState.Reconnecting>()
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(1, it.attempt)
            reconnecting2.complete(it)
        }
        flowTest.action {
            connectionManager.reset()
            reconnecting2.getCompleted().retry()
        }
        flowTest.test {
            assertFalse(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnected)
        }

    }

    @Test
    fun testReadRssi() = runBlockingWithFlow { flowTest ->
        val disconnectedState = getDisconnectedState(flowTest)
        val connectingState = connecting(disconnectedState, flowTest)
        val connectedState = connect(connectingState, flowTest)

        assertEquals(initialRssi, connectedState.lastKnownRssi)
        flowTest.action {
            connectedState.readRssi()
            connectedState.rssiDidUpdate(-20)
        }

        flowTest.test {
            assertTrue(connectionManager.readRssiCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected)
            assertEquals(-20, it.lastKnownRssi)
        }
    }

    @Test
    fun testDiscoverDevices() = runBlockingWithFlow() { flowTest ->
        val disconnectedState = getDisconnectedState(flowTest)
        val connectingState = connecting(disconnectedState, flowTest)
        val connectedState = connect(connectingState, flowTest)
        flowTest.action {
            connectedState.discoverServices()
        }
        val discoverState = CompletableDeferred<DeviceState.Connected.Discovering>()
        flowTest.test {
            assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected.Discovering)
            discoverState.complete(it)
        }
        val services = CompletableDeferred<List<Service>>()
        flowTest.action {
            services.complete(createServices(discoverState.getCompleted().repoAccessor))
            discoverState.getCompleted().didDiscoverServices(services.getCompleted())
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(services.getCompleted(), it.services)
        }
    }

    @Test
    fun testHandleActions() = runBlockingWithFlow { flowTest ->
        val disconnectedState = getDisconnectedState(flowTest)
        val connectingState = connecting(disconnectedState, flowTest)
        val connectedState = connect(connectingState, flowTest)
        createServices(connectedState.repoAccessor)
        val characteristic = createCharacteristic(connectedState.repoAccessor)
        flowTest.action {
            connectedState.handleAction(DeviceAction.Read.Characteristic(characteristic))
        }
        var handlingAction = CompletableDeferred<DeviceState.Connected.HandlingAction>()
        flowTest.test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        val descriptor = CompletableDeferred<Descriptor>()
        flowTest.action {
            descriptor.complete(createDescriptor(handlingAction.getCompleted().repoAccessor))
            handlingAction.getCompleted().addAction(DeviceAction.Write.Descriptor(null, descriptor.getCompleted()))
            handlingAction = CompletableDeferred()
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        flowTest.action {
            handlingAction.getCompleted().actionCompleted()
            handlingAction = CompletableDeferred()
        }
        flowTest.test {
            assertTrue(validateCharacteristicUpdated())
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        flowTest.action {
            handlingAction.getCompleted().actionCompleted()
        }
        flowTest.test {
            assertTrue(validateDescriptorUpdated())
            assertTrue(it is DeviceState.Connected.Idle)
        }
    }

    private suspend fun getDisconnectedState(flowTest: FlowTest<DeviceState>) : DeviceState.Disconnected {
        val disconnectedState = CompletableDeferred<DeviceState.Disconnected>()
        flowTest.test {
            assertTrue(it is DeviceState.Disconnected)
            disconnectedState.complete(it)
        }
        return disconnectedState.await()
    }

    private suspend fun getDisconnectingState(flowTest: FlowTest<DeviceState>) : DeviceState.Disconnecting {
        val disconnectingState = CompletableDeferred<DeviceState.Disconnecting>()
        flowTest.test {
            assertTrue(connectionManager.disconnectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnecting)
            disconnectingState.complete(it)
        }
        return disconnectingState.await()
    }

    private suspend fun connecting(disconnectedState: DeviceState.Disconnected, flowTest: FlowTest<DeviceState>) : DeviceState.Connecting {
        flowTest.action {
            disconnectedState.connect()
        }
        val connectingState = CompletableDeferred<DeviceState.Connecting>()
        flowTest.test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Connecting)
            connectingState.complete(it)
        }
        return connectingState.await()
    }

    private suspend fun connect(connectingState: DeviceState.Connecting, flowTest: FlowTest<DeviceState>) : DeviceState.Connected.Idle {
        val connectedState = CompletableDeferred<DeviceState.Connected.Idle>()
        flowTest.action {
            connectingState.didConnect()
        }
        flowTest.test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(emptyList(), it.services)
            connectedState.complete(it)
        }
        return connectedState.await()
    }

    private suspend fun disconnecting(connectedState: DeviceState.Connected, flowTest: FlowTest<DeviceState>): DeviceState.Disconnecting {
        flowTest.action {
            connectedState.disconnect()
        }
        return getDisconnectingState(flowTest)
    }

    private suspend fun disconnect(disconnecting: DeviceState.Disconnecting, flowTest: FlowTest<DeviceState>): DeviceState.Disconnected {
        flowTest.action {
            disconnecting.didDisconnect()
        }
        return getDisconnectedState(flowTest)
    }

}

internal class MockDeviceConnectionManager(connectionSettings: ConnectionSettings,
                                  deviceInfoHolder: DeviceInfoHolder,
                                  repoAccessor: StateRepoAccesor<DeviceState>
) : BaseDeviceConnectionManager(connectionSettings, deviceInfoHolder, repoAccessor) {

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