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
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.CompletableDeferred
import kotlin.test.*

abstract class DeviceTest : FlowableTest<DeviceState>() {

    companion object {
        val reconnectionAttempts = 2
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
        flowable.complete(Device(reconnectionAttempts, deviceInfoHolder, initialRssi, object : BaseDeviceConnectionManager.Builder {
            override fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): BaseDeviceConnectionManager {
                connectionManager = MockDeviceConnectionManager(reconnectionAttempts, deviceInfo, repoAccessor)
                return connectionManager
            }
        }).flowable.value)
    }

    @Test
    fun testInitialState() = runBlockingWithFlow {
        test {
            assertTrue(it is DeviceState.Disconnected)
            assertEquals(initialRssi, it.lastKnownRssi)
        }
    }

    @Test
    fun testConnected() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        connect(connectingState)
    }

    @Test
    fun testCancelConnection() = runBlockingWithFlow {
        val connectingState = connecting(getDisconnectedState())
        action {
            connectingState.cancelConnection()
        }
        disconnect(getDisconnectingState())
    }

    @Test
    fun testDisconnect() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)
        val disconnectingState = disconnecting(connectedState)
        disconnect(disconnectingState)
    }

    @Test
    fun testReconnect() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)

        action {
            connectionManager.reset()
            connectedState.reconnect()
        }

        val reconnecting = CompletableDeferred<DeviceState.Reconnecting>()
        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
            reconnecting.complete(it)
        }
        action {
            reconnecting.getCompleted().didConnect()
        }
        test {
            assertTrue(it is DeviceState.Connected)
        }
    }

    @Test
    fun testReconnectFailed() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)

        action {
            connectionManager.reset()
            connectedState.reconnect()
        }

        val reconnecting1 = CompletableDeferred<DeviceState.Reconnecting>()
        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(0, it.attempt)
            reconnecting1.complete(it)
        }
        action {
            connectionManager.reset()
            reconnecting1.getCompleted().retry()
        }
        val reconnecting2 = CompletableDeferred<DeviceState.Reconnecting>()
        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Reconnecting)
            assertEquals(1, it.attempt)
            reconnecting2.complete(it)
        }
        action {
            connectionManager.reset()
            reconnecting2.getCompleted().retry()
        }
        test {
            assertFalse(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnected)
        }

    }

    @Test
    fun testReadRssi() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)

        assertEquals(initialRssi, connectedState.lastKnownRssi)
        action {
            connectedState.readRssi()
            connectedState.rssiDidUpdate(-20)
        }

        test {
            assertTrue(connectionManager.readRssiCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected)
            assertEquals(-20, it.lastKnownRssi)
        }
    }

    @Test
    fun testDiscoverDevices() = runBlockingWithFlow() {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)
        action {
            connectedState.discoverServices()
        }
        val discoverState = CompletableDeferred<DeviceState.Connected.Discovering>()
        test {
            assertTrue(connectionManager.discoverServicesCompleted.isCompleted)
            assertTrue(it is DeviceState.Connected.Discovering)
            discoverState.complete(it)
        }
        val services = CompletableDeferred<List<Service>>()
        action {
            services.complete(createServices(discoverState.getCompleted().repoAccessor))
            discoverState.getCompleted().didDiscoverServices(services.getCompleted())
        }
        test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(services.getCompleted(), it.services)
        }
    }

    @Test
    fun testHandleActions() = runBlockingWithFlow {
        val disconnectedState = getDisconnectedState()
        val connectingState = connecting(disconnectedState)
        val connectedState = connect(connectingState)
        val characteristic = createCharacteristic(connectedState.repoAccessor)
        action {
            connectedState.handleAction(DeviceAction.Read.Characteristic(characteristic))
        }
        var handlingAction = CompletableDeferred<DeviceState.Connected.HandlingAction>()
        test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        val descriptor = CompletableDeferred<Descriptor>()
        action {
            descriptor.complete(createDescriptor(handlingAction.getCompleted().repoAccessor))
            handlingAction.getCompleted().addAction(DeviceAction.Write.Descriptor(null, descriptor.getCompleted()))
            handlingAction = CompletableDeferred()
        }
        test {
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        action {
            handlingAction.getCompleted().actionCompleted()
            handlingAction = CompletableDeferred()
        }
        test {
            assertTrue(validateCharacteristicUpdated())
            assertTrue(it is DeviceState.Connected.HandlingAction)
            handlingAction.complete(it)
        }
        action {
            handlingAction.getCompleted().actionCompleted()
        }
        test {
            assertTrue(validateDescriptorUpdated())
            assertTrue(it is DeviceState.Connected.Idle)
        }
    }

    private suspend fun getDisconnectedState() : DeviceState.Disconnected {
        val disconnectedState = CompletableDeferred<DeviceState.Disconnected>()
        test {
            assertTrue(it is DeviceState.Disconnected)
            disconnectedState.complete(it)
        }
        return disconnectedState.await()
    }

    private suspend fun getDisconnectingState() : DeviceState.Disconnecting {
        val disconnectingState = CompletableDeferred<DeviceState.Disconnecting>()
        test {
            assertTrue(connectionManager.disconnectCompleted.isCompleted)
            assertTrue(it is DeviceState.Disconnecting)
            disconnectingState.complete(it)
        }
        return disconnectingState.await()
    }

    private suspend fun connecting(disconnectedState: DeviceState.Disconnected) : DeviceState.Connecting {
        action {
            disconnectedState.connect()
        }
        val connectingState = CompletableDeferred<DeviceState.Connecting>()
        test {
            assertTrue(connectionManager.connectCompleted.isCompleted)
            assertTrue(it is DeviceState.Connecting)
            connectingState.complete(it)
        }
        return connectingState.await()
    }

    private suspend fun connect(connectingState: DeviceState.Connecting) : DeviceState.Connected.Idle {
        val connectedState = CompletableDeferred<DeviceState.Connected.Idle>()
        action {
            connectingState.didConnect()
        }
        test {
            assertTrue(it is DeviceState.Connected.Idle)
            assertEquals(emptyList(), it.services)
            connectedState.complete(it)
        }
        return connectedState.await()
    }

    private suspend fun disconnecting(connectedState: DeviceState.Connected): DeviceState.Disconnecting {
        action {
            connectedState.disconnect()
        }
        return getDisconnectingState()
    }

    private suspend fun disconnect(disconnecting: DeviceState.Disconnecting): DeviceState.Disconnected {
        action {
            disconnecting.didDisconnect()
        }
        return getDisconnectedState()
    }

}

internal class MockDeviceConnectionManager(reconnectionAttempts: Int,
                                  deviceInfoHolder: DeviceInfoHolder,
                                  repoAccessor: StateRepoAccesor<DeviceState>
) : BaseDeviceConnectionManager(reconnectionAttempts, deviceInfoHolder, repoAccessor) {

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