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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.bluetooth.test.server.MockLocalServiceWrapperBuilder
import kotlinx.coroutines.CompletableDeferred

/**
 * A fake [ServerState.AwaitingPermissions] whose [awaitPermitted] resolves to [onPermitted].
 * Implementing the (non-sealed) leaf state interfaces is allowed even though their supertypes are sealed.
 */
internal class MockAwaitingPermissions(private val onPermitted: suspend () -> ServerState.HasPermissions) : ServerState.AwaitingPermissions {
    override suspend fun awaitPermitted(autoRequest: Boolean): ServerState.HasPermissions = onPermitted()
    override fun close(): ServerState.Closed = ServerState.Closed
}

/**
 * A fake [ServerState.AwaitingBluetoothEnabled]. [awaitEnabled] resolves to [onEnabled]; [awaitRevoked] suspends until [onAwaitRevoked] completes.
 */
internal class MockAwaitingBluetoothEnabled(private val onEnabled: suspend () -> ServerState.Available) : ServerState.AwaitingBluetoothEnabled {
    val onAwaitRevoked = CompletableDeferred<ServerState.AwaitingPermissions>()
    override suspend fun awaitEnabled(autoEnable: Boolean): ServerState.Available = onEnabled()
    override suspend fun awaitRevoked(): ServerState.AwaitingPermissions = onAwaitRevoked.await()
    override fun close(): ServerState.Closed = ServerState.Closed
}

/**
 * A fake [ServerState.Available]. Action methods are mocked with the Kaluga mocking library so they can be stubbed and verified;
 * [awaitDisabled]/[awaitRevoked] suspend until [onAwaitDisabled]/[onAwaitRevoked] are completed, allowing the test to drive transitions.
 */
internal class MockAvailable(setupMocks: Boolean = true) : ServerState.Available {

    val startAdvertisingMock = ::startAdvertising.mock()
    val stopAdvertisingMock = ::stopAdvertising.mock()
    val addServiceMock = ::addService.mock()
    val removeServiceMock = ::removeService.mock()
    val removeAllServicesMock = ::removeAllServices.mock()
    val executeMock = ::execute.mock()

    val onAwaitDisabled = CompletableDeferred<ServerState.AwaitingBluetoothEnabled>()
    val onAwaitRevoked = CompletableDeferred<ServerState.AwaitingPermissions>()

    init {
        if (setupMocks) {
            startAdvertisingMock.on().doReturn(true)
            addServiceMock.on().doReturn(true)
            executeMock.on().doReturn(true)
        }
    }

    override suspend fun addService(service: LocalService): Boolean = addServiceMock.call(service)
    override fun removeService(service: LocalService) {
        removeServiceMock.call(service)
    }
    override fun removeAllServices() {
        removeAllServicesMock.call()
    }
    override suspend fun startAdvertising(data: AdvertiseData): Boolean = startAdvertisingMock.call(data)
    override fun stopAdvertising() {
        stopAdvertisingMock.call()
    }
    override suspend fun awaitDisabled(): ServerState.AwaitingBluetoothEnabled = onAwaitDisabled.await()
    override suspend fun awaitRevoked(): ServerState.AwaitingPermissions = onAwaitRevoked.await()
    override fun close(): ServerState.Closed = ServerState.Closed
    override suspend fun execute(characteristic: LocalCharacteristic.Notifiable, device: ConnectedDevice, value: ByteArray): Boolean =
        executeMock.call(characteristic, device, value)
    override fun serviceBuilder(uuid: UUID, notify: Notify): LocalServiceDSL.Primary = LocalServiceDSL.Primary(
        uuid,
        notify,
        { _, _ -> },
        { _, _ -> },
        { },
        { descriptorUuid -> LocalDescriptorDSL(descriptorUuid, { _, _ -> }, { _, _ -> }) },
        MockLocalServiceWrapperBuilder(),
    )
}
