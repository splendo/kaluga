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

import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.bluetooth.createServiceWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MockDevice(
    override val identifier: Identifier = randomIdentifier(),
    override val info: MutableStateFlow<MockDeviceInfo> = MutableStateFlow(MockDeviceInfo()),
    private val connectionSettings: ConnectionSettings = ConnectionSettings(),
    private val coroutineContext: CoroutineContext,
    setupMocks: Boolean = true,
    private val connectionDelay: Duration = 1.seconds,
    private val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
) : Device {

    val mockConnectableDeviceManager = MockConnectableDeviceManager()

    private val connectableDeviceStateRepo = MockConnectableDeviceStateRepo(mockConnectableDeviceManager, coroutineContext)

    init {
        if (setupMocks) {
            mockConnectableDeviceManager.mockRequestMtu.on().doExecuteSuspended { (mtu) ->
                connectableDeviceStateRepo.takeAndChangeState(ConnectableDeviceState.Connected::class) { it.didUpdateMtu(mtu) }
                true
            }
            mockConnectableDeviceManager.mockStartDisconnected.on().doExecute {
                handleDisconnecting()
            }
            mockConnectableDeviceManager.mockStartConnecting.on().doExecute {
                handleConnecting()
            }
            mockConnectableDeviceManager.mockConnect.on().doExecuteSuspended {
                delay(connectionDelay)
                handleConnected()
            }
            mockConnectableDeviceManager.mockDisconnect.on().doExecuteSuspended {
                delay(connectionDelay)
                handleDisconnected()
            }
            mockConnectableDeviceManager.mockDiscoverServices.on().doExecute {
                handleDiscoverServices(buildServices())
            }
            mockConnectableDeviceManager.mockStartDiscovering.on().doExecute {
                handleStartDiscoveringServices()
            }
            mockConnectableDeviceManager.mockHandleCancelConnecting.on().doExecute {
                handleCancelConnecting()
            }
            mockConnectableDeviceManager.mockHandleCancelReconnecting.on().doExecute {
                handleCancelConnecting()
            }
        }
    }

    override val state: Flow<DeviceState> = info.flatMapLatest {
        when {
            !it.advertisementData.isConnectable -> flowOf(MockDeviceState.NotConnectable)
            else -> connectableDeviceStateRepo
        }
    }

    private fun buildServices() = info.value.advertisementData.serviceUUIDs.map {
        Service(
            service = createServiceWrapper { uuid = it },
            emitNewAction = {},
            parentLogTag = "MockDeviceService",
            logger = logger,
        )
    }

    fun handleConnecting() {
        connectableDeviceStateRepo.launchTakeAndChangeState(
            coroutineContext,
            ConnectableDeviceState.Disconnected::class,
        ) { it.connect(connectionSettings.reconnectionSettings) }
    }

    fun handleCancelConnecting() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext) { state ->
            when (state) {
                is ConnectableDeviceState.Connecting -> state.cancelConnection
                else -> state.remain()
            }
        }
    }

    override fun handleConnected() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext) { state ->
            when (state) {
                is ConnectableDeviceState.Connecting -> state.didConnect
                else -> state.remain()
            }
        }
    }

    fun handleDisconnecting() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext) { it.disconnecting }
    }

    override fun handleDisconnected() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext) { state ->
            when (state) {
                is ConnectableDeviceState.Connected -> when (state.reconnectionSettings) {
                    is ConnectionSettings.ReconnectionSettings.Always -> state.reconnect
                    is ConnectionSettings.ReconnectionSettings.Never -> state.didDisconnect
                }
                is ConnectableDeviceState.Disconnected -> state.remain()
                is ConnectableDeviceState.Connecting,
                is ConnectableDeviceState.Disconnecting,
                -> state.didDisconnect
            }
        }
    }

    fun handleStartDiscoveringServices() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext, ConnectableDeviceState.Connected.NoServices::class) { it.discoverServices }
    }

    fun handleDiscoverServices(services: List<Service>) {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext, ConnectableDeviceState.Connected.Discovering::class) { it.didDiscoverServices(services) }
    }

    fun addAction(action: DeviceAction) {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext) { state ->
            when (state) {
                is ConnectableDeviceState.Connected.Idle -> state.handleAction(action)
                is ConnectableDeviceState.Connected.HandlingAction -> state.addAction(action)
                else -> state.remain()
            }
        }
    }

    fun completeAction() {
        connectableDeviceStateRepo.launchTakeAndChangeState(coroutineContext, ConnectableDeviceState.Connected.HandlingAction::class) {
            it.actionCompleted
        }
    }

    override fun advertisementDataDidUpdate(advertisementData: BaseAdvertisementData) {
        info.value = info.value.copy(advertisementData = advertisementData)
    }

    override fun rssiDidUpdate(rssi: RSSI) {
        info.value = info.value.copy(rssi = rssi)
    }
}
