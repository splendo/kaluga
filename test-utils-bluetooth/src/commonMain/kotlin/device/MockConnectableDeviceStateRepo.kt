/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.bluetooth.device

import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.device.BaseConnectableDeviceStateRepo
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

class MockConnectableDeviceStateRepo(
    mockConnectableDeviceManager: MockConnectableDeviceManager,
    coroutineContext: CoroutineContext,
) : BaseConnectableDeviceStateRepo(
    coroutineContext = coroutineContext,
    initialState = { MockDeviceState.Disconnected(mockConnectableDeviceManager) },
)

class MockConnectableDeviceManager {

    val mockStartDisconnected = this::startDisconnected.mock()
    fun startDisconnected(): Unit = mockStartDisconnected.call()

    val mockReadRssi = this::readRssi.mock()
    suspend fun readRssi(): Unit = mockReadRssi.call()

    val mockRequestMtu = this::requestMtu.mock()
    suspend fun requestMtu(mtu: MTU): Boolean = mockRequestMtu.call(mtu)

    val mockPair = this::pair.mock()
    suspend fun pair(): Unit = mockPair.call()

    val mockUnpair = this::unpair.mock()
    suspend fun unpair(): Unit = mockUnpair.call()

    val mockConnect = this::connect.mock()
    suspend fun connect(): Unit = mockConnect.call()

    val mockDisconnect = this::disconnect.mock()
    suspend fun disconnect(): Unit = mockDisconnect.call()

    val mockDiscoverServices = this::discoverServices.mock()
    suspend fun discoverServices(): Unit = mockDiscoverServices.call()

    val mockStartDiscovering = this::startDiscovering.mock()
    fun startDiscovering(): Unit = mockStartDiscovering.call()

    val mockHandleCancelConnecting = this::handleCancelConnecting.mock()
    fun handleCancelConnecting(): Unit = mockHandleCancelConnecting.call()

    val mockHandleCancelReconnecting = this::handleCancelReconnecting.mock()
    fun handleCancelReconnecting(): Unit = mockHandleCancelReconnecting.call()

    val mockStartConnecting = this::startConnecting.mock()
    fun startConnecting(reconnectionSettings: ConnectionSettings.ReconnectionSettings?): Unit = mockStartConnecting.call(reconnectionSettings)
}
