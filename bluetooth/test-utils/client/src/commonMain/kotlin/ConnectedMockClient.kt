/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.test

import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceImpl
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceStateImplRepo
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.discoveredServices
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.test.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.test.device.MockDeviceConnectionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

/**
 * A mock Bluetooth device that has been connected and had its services discovered, ready to route reads and writes.
 *
 * Obtain one with [connectedMockClient]. The device is exposed through [client] so the generated
 * `SharedDeviceClient.bluetooth(client, identifier)` factory can look it up by [identifier].
 *
 * The device stays connected for the lifetime of this object: its connection state repo and a background collection of
 * its `discoveredServices()` run on a dedicated child scope that keeps the device connected so reads and writes route.
 * Call [close] (e.g. in a `finally`) when done; otherwise structured concurrency will wait forever on those background
 * coroutines.
 */
class ConnectedMockClient internal constructor(
    /**
     * The [MockBluetoothClient] exposing the connected device.
     */
    val client: MockBluetoothClient,
    /**
     * The [Identifier] of the connected device.
     */
    val identifier: Identifier,
    /**
     * The [MockDeviceConnectionManager] driving the connected device. Use it (or [pump]) to complete queued actions.
     */
    val connectionManager: MockDeviceConnectionManager,
    /**
     * The discovered [RemoteService]s of the connected device.
     */
    val services: List<RemoteService>,
    private val deviceScope: CoroutineScope,
) {

    /**
     * Returns the [MockCharacteristicWrapper] for the characteristic with the given [characteristic] uuid on the
     * service with the given [service] uuid. Use it to set the value a read will return or to inspect what a write
     * recorded.
     */
    fun characteristicWrapper(service: UUID, characteristic: UUID): MockCharacteristicWrapper {
        val remoteService = services.first { it.uuid == service }
        return remoteService.characteristics.first { it.uuid == characteristic }.wrapper as MockCharacteristicWrapper
    }

    /**
     * Pumps the currently queued [com.splendo.kaluga.bluetooth.device.DeviceAction] so the enqueued read/write
     * completes. Equivalent to `connectionManager.handleCurrentAction()`.
     */
    suspend fun pump() = connectionManager.handleCurrentAction()

    /**
     * Cancels the background coroutines that keep the device connected. Always call this when done with the client.
     */
    fun close() {
        deviceScope.cancel()
    }
}

/**
 * Connects a mock Bluetooth device exposing the service described by [builder], discovers its services, and returns a
 * [ConnectedMockClient] that keeps the device connected so reads and writes route end-to-end.
 *
 * The device and its keep-alive coroutines run on a dedicated child scope of the receiver so [ConnectedMockClient.close]
 * can tear them down without leaving structured concurrency waiting forever. All internal waits are bounded by
 * [withTimeout] so an incorrect sequence fails fast rather than hanging.
 */
suspend fun CoroutineScope.connectedMockClient(builder: ServiceWrapperBuilder.() -> Unit): ConnectedMockClient {
    val deviceScope = CoroutineScope(coroutineContext + SupervisorJob(coroutineContext[kotlinx.coroutines.Job]))
    val serviceWrapper = createServiceWrapper(builder)
    val serviceUuid = serviceWrapper.uuid

    val identifier = randomIdentifier()
    val deviceWrapper = createDeviceWrapper(identifier = identifier)
    val connectionSettings = ConnectionSettings()
    val connectionManagerBuilder = MockDeviceConnectionManager.Builder()

    val device = ConnectableDeviceImpl(
        identifier,
        DeviceInfoImpl(
            deviceWrapper,
            rssi = -100,
            advertisementData = MockAdvertisementData(serviceUUIDs = listOf(serviceUuid), isConnectable = true),
        ),
        connectionSettings,
        { connectionManagerBuilder.create(deviceWrapper, connectionSettings, deviceScope) },
        deviceScope,
    ) { connectionManager, context ->
        ConnectableDeviceStateImplRepo(connectionSettings.reconnectionSettings, connectionManager, context)
    }

    // ConnectableDeviceImpl lazily creates the connection manager once the device is known connectable.
    withTimeout(5.seconds) {
        while (connectionManagerBuilder.createdDeviceConnectionManager.isEmpty()) delay(10)
    }
    val connectionManager = connectionManagerBuilder.createdDeviceConnectionManager.first()
    connectionManager.connectMock.on().doExecute { connectionManager.handleConnect() }

    val client = MockBluetoothClient()
    client.discoveredDevicesFlow.value = listOf(device)

    deviceScope.launch { device.connect() }

    val service = connectionManager.create(serviceWrapper)

    // Supply the discovered service the moment the device reaches Discovering.
    deviceScope.launch {
        device.state.filter { it is ConnectableDeviceState.Connected.Discovering }.first()
        connectionManager.discover(listOf(service))
    }

    // Collecting discoveredServices() drives NoServices -> Discovering and keeps the device connected for the lifetime
    // of the returned ConnectedMockClient. ConnectedMockClient.close() cancels deviceScope, ending this collection.
    deviceScope.launch {
        client.allDevices()[identifier].discoveredServices().collect { }
    }

    // Wait until the service is actually discovered before handing back, so reads route immediately.
    withTimeout(5.seconds) {
        device.state.filter { it is ConnectableDeviceState.Connected.Idle }.first()
    }

    return ConnectedMockClient(client, identifier, connectionManager, listOf(service), deviceScope)
}
