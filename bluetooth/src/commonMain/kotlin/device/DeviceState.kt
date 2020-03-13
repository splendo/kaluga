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

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.state.HandleAfterOldStateIsRemoved
import com.splendo.kaluga.state.HotStateRepo
import com.splendo.kaluga.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

sealed class DeviceAction {
    sealed class Read : DeviceAction() {
        class Characteristic(val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Read()
        class Descriptor(val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Read()
    }
    sealed class Write(val newValue: ByteArray?) : DeviceAction() {
        class Characteristic(newValue: ByteArray?, val characteristic: com.splendo.kaluga.bluetooth.Characteristic) : Write(newValue)
        class Descriptor(newValue: ByteArray?, val descriptor: com.splendo.kaluga.bluetooth.Descriptor) : Write(newValue)
    }

    data class Notification(val characteristic: com.splendo.kaluga.bluetooth.Characteristic, val enable: Boolean) : DeviceAction()

}

sealed class DeviceState (open val deviceInfo: DeviceInfoImpl,
                          internal open val connectionManager: BaseDeviceConnectionManager)
    : State<DeviceState>(),
    DeviceInfo by deviceInfo, CoroutineScope by connectionManager.stateRepo {

    sealed class Connected(deviceInfo: DeviceInfoImpl,
                           connectionManager: BaseDeviceConnectionManager)
        : DeviceState(deviceInfo, connectionManager) {

        data class NoServices internal constructor(override val deviceInfo: DeviceInfoImpl,
                                             override val connectionManager: BaseDeviceConnectionManager)
            : Connected(deviceInfo, connectionManager) {

            fun startDiscovering() {
                launch {
                    connectionManager.stateRepo.takeAndChangeState { deviceState ->
                        if (deviceState is NoServices)
                            discoverServices
                        else
                            remain
                    }
                }
            }

            val discoverServices = suspend {
                Discovering(deviceInfo, connectionManager)
            }

        }

        data class Discovering internal constructor(override val deviceInfo: DeviceInfoImpl,
                                                    override val connectionManager: BaseDeviceConnectionManager)
            : Connected(deviceInfo, connectionManager),
            HandleAfterOldStateIsRemoved<DeviceState> {

            internal fun didDiscoverServices(services: List<Service>): suspend () -> Idle {
                return {Idle(services, deviceInfo, connectionManager)}
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                connectionManager.discoverServices()
            }

        }

        data class Idle internal constructor(val services: List<Service>,
                                                   override val deviceInfo: DeviceInfoImpl,
                                                   override val connectionManager: BaseDeviceConnectionManager) : Connected(deviceInfo, connectionManager) {

            fun handleAction(action: DeviceAction): suspend () -> HandlingAction {
                return { HandlingAction(action, emptyList(), services, deviceInfo, connectionManager) }
            }

        }

        data class HandlingAction internal constructor(internal val action: DeviceAction,
                                                       internal val nextActions: List<DeviceAction>,
                                                       val services: List<Service>,
                                                       override val deviceInfo: DeviceInfoImpl,
                                                       override val connectionManager: BaseDeviceConnectionManager)
            : Connected(deviceInfo, connectionManager),
        HandleAfterOldStateIsRemoved<DeviceState> {

            fun addAction(newAction: DeviceAction) : suspend () -> HandlingAction {
                return {HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, deviceInfo, connectionManager)}
            }

            internal val actionCompleted = suspend {
                when (action) {
                    is DeviceAction.Read.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Read.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Write.Characteristic -> action.characteristic.updateValue()
                    is DeviceAction.Write.Descriptor -> action.descriptor.updateValue()
                    is DeviceAction.Notification -> {
                        if (action.enable)
                            action.characteristic.updateValue()
                    }
                }

                if (nextActions.isEmpty()) {
                    Idle(services, deviceInfo, connectionManager)
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    HandlingAction(nextAction, remainingActions, services, deviceInfo, connectionManager)
                }
            }



            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                when (oldState) {
                    is HandlingAction -> {
                        if (oldState.action == action)
                            return
                    }
                    else -> {}
                }

                connectionManager.performAction(action)
            }

        }

        fun startDisconnected() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Connected)
                        disconnecting
                    else
                        remain
                }
            }
        }

        val reconnect = suspend {
            val services = when (this) {
                is Idle -> this.services
                is HandlingAction -> this.services
                else -> null
            }
            Reconnecting(0, services, deviceInfo, connectionManager)
        }

        suspend fun readRssi() {
            connectionManager.readRssi()
        }

        override suspend fun finalState() {
            super.finalState()

            connectionManager.disconnect()
        }
    }
    data class Connecting internal constructor(override val deviceInfo: DeviceInfoImpl,
                                               override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(deviceInfo, connectionManager),
    HandleAfterOldStateIsRemoved<DeviceState>{

        val cancelConnection = disconnecting

        fun handleCancel() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Connecting) {
                        cancelConnection
                    } else {
                        remain
                    }
                }
            }
        }

        internal val didConnect = suspend {
            Connected.NoServices(deviceInfo, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when(oldState) {
                is Disconnected -> connectionManager.connect()
                else -> {}
            }
        }
    }

    data class Reconnecting internal constructor(val attempt: Int,
                                                 val services: List<Service>?,
                                                 override val deviceInfo: DeviceInfoImpl,
                                                 override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(deviceInfo, connectionManager),
        HandleAfterOldStateIsRemoved<DeviceState>{

        fun retry(): suspend () -> DeviceState {
            return when(val reconnectionSetting = connectionManager.connectionSettings.reconnectionSettings) {
                is ConnectionSettings.ReconnectionSettings.Always -> remain
                is ConnectionSettings.ReconnectionSettings.Never -> didDisconnect
                is ConnectionSettings.ReconnectionSettings.Limited -> {
                    val nextAttempt = attempt + 1
                    if (nextAttempt < reconnectionSetting.attempts) {
                        suspend {copy(attempt = nextAttempt)}
                    } else {
                        didDisconnect
                    }
                }
            }
        }

        fun handleCancel() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Reconnecting)
                        cancelConnection
                    else
                        remain
                }
            }
        }

        val cancelConnection = disconnecting

        internal val didConnect = suspend {
            services?.let { Connected.Idle(services, deviceInfo, connectionManager) } ?: Connected.NoServices(deviceInfo, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connected, is Reconnecting -> connectionManager.connect()
                else -> {}
            }
        }

    }

    data class Disconnected internal constructor(override val deviceInfo: DeviceInfoImpl,
                                                 override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(deviceInfo, connectionManager) {

        fun startConnecting() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Disconnected) {
                        connect()
                    } else {
                        remain
                    }
                }
            }
        }

        fun connect(): suspend () -> DeviceState {
            return if (deviceInfo.advertisementData.isConnectible) {
                suspend {
                    Connecting(deviceInfo, connectionManager)
                }
            } else {
                remain
            }
        }

    }
    data class Disconnecting internal constructor(override val deviceInfo: DeviceInfoImpl,
                                                  override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(deviceInfo, connectionManager),
        HandleAfterOldStateIsRemoved<DeviceState> {

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connecting, is Reconnecting, is Connected -> connectionManager.disconnect()
                else -> {}
            }
        }
    }

    fun rssiDidUpdate(rssi: Int): suspend () -> DeviceState {
        return updateDeviceInfo(deviceInfo.copy(rssi = rssi))
    }

    fun advertisementDataDidUpdate(advertisementData: AdvertisementData): suspend () -> DeviceState {
        return updateDeviceInfo(deviceInfo.copy(advertisementData = advertisementData))
    }

    private fun updateDeviceInfo(newDeviceInfo: DeviceInfoImpl): suspend () -> DeviceState {
        return {
            when (this) {
                is Connected.NoServices -> copy(deviceInfo = newDeviceInfo)
                is Connected.Idle -> copy(deviceInfo = newDeviceInfo)
                is Connected.HandlingAction -> copy(deviceInfo = newDeviceInfo)
                is Connected.Discovering -> copy(deviceInfo = newDeviceInfo)
                is Connecting -> copy(deviceInfo = newDeviceInfo)
                is Reconnecting -> copy(deviceInfo = newDeviceInfo)
                is Disconnecting -> copy(deviceInfo = newDeviceInfo)
                is Disconnected -> copy(deviceInfo = newDeviceInfo)
            }
        }
    }

    internal val didDisconnect = suspend {
        Disconnected(deviceInfo, connectionManager)
    }

    protected val disconnecting = suspend {
        Disconnecting(deviceInfo, connectionManager)
    }

}

class Device internal constructor(connectionSettings: ConnectionSettings,
                                  private val initialDeviceInfo: DeviceInfoImpl,
                                  connectionBuilder: BaseDeviceConnectionManager.Builder,
                                  coroutineContext: CoroutineContext = MainQueueDispatcher)
    : HotStateRepo<DeviceState>(coroutineContext) {

    val identifier: Identifier = initialDeviceInfo.identifier
    internal val deviceConnectionManager = connectionBuilder.create(connectionSettings, initialDeviceInfo.deviceHolder, this)

    override suspend fun initialValue(): DeviceState {
        return DeviceState.Disconnected(initialDeviceInfo, deviceConnectionManager)
    }

}