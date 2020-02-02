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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

sealed class DeviceState (open val lastKnownRssi: Int,
                          internal open val connectionManager: BaseDeviceConnectionManager)
    : State<DeviceState>(),
    DeviceInfo by connectionManager.deviceInfoHolder, CoroutineScope by connectionManager.stateRepo {

    sealed class Connected(open val services: List<Service>,
                           lastKnownRssi: Int,
                           connectionManager: BaseDeviceConnectionManager)
        : DeviceState(lastKnownRssi, connectionManager) {

        data class Idle internal constructor(override val services: List<Service>,
                                             override val lastKnownRssi: Int,
                                             override val connectionManager: BaseDeviceConnectionManager)
            : Connected(services, lastKnownRssi, connectionManager) {

            fun startDiscovering() {
                launch {
                    connectionManager.stateRepo.takeAndChangeState { deviceState ->
                        if (deviceState is Idle)
                            discoverServices
                        else
                            remain
                    }
                }
            }

            val discoverServices = suspend {
                Discovering(lastKnownRssi, connectionManager)
            }

            fun handleAction(action: DeviceAction): suspend () -> HandlingAction {
                return { HandlingAction(action, emptyList(), services, lastKnownRssi, connectionManager) }
            }

        }

        data class Discovering internal constructor(override val lastKnownRssi: Int,
                                                    override val connectionManager: BaseDeviceConnectionManager)
            : Connected(emptyList(), lastKnownRssi, connectionManager),
            HandleAfterOldStateIsRemoved<DeviceState> {

            internal fun didDiscoverServices(services: List<Service>): suspend () -> Idle {
                return {Idle(services, lastKnownRssi, connectionManager)}
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                connectionManager.discoverServices()
            }

        }

        data class HandlingAction internal constructor(internal val action: DeviceAction,
                                                       internal val nextActions: List<DeviceAction>,
                                                       override val services: List<Service>,
                                                       override val lastKnownRssi: Int,
                                                       override val connectionManager: BaseDeviceConnectionManager)
            : Connected(services, lastKnownRssi, connectionManager),
        HandleAfterOldStateIsRemoved<DeviceState> {

            fun addAction(newAction: DeviceAction) : suspend () -> HandlingAction {
                return {HandlingAction(action, listOf(*nextActions.toTypedArray(), newAction), services, lastKnownRssi, connectionManager)}
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
                    Idle(services, lastKnownRssi, connectionManager)
                } else {
                    val nextAction = nextActions.first()
                    val remainingActions = nextActions.drop(1)
                    HandlingAction(nextAction, remainingActions, services, lastKnownRssi, connectionManager)
                }
            }

            override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
                when (oldState) {
                    is HandlingAction -> {
                        if (oldState.action == action)
                            return
                    }
                }

                connectionManager.performAction(action)
            }

        }

        fun startDisconnected() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Connected)
                        disconnect
                    else
                        remain
                }
            }
        }

        val disconnect = disconnecting

        val reconnect = suspend {
            Reconnecting(0, services, lastKnownRssi, connectionManager)
        }

        suspend fun readRssi() {
            connectionManager.readRssi()
        }

        override suspend fun finalState() {
            super.finalState()

            connectionManager.disconnect()
        }
    }
    data class Connecting internal constructor(override val lastKnownRssi: Int,
                                               override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(lastKnownRssi, connectionManager),
    HandleAfterOldStateIsRemoved<DeviceState>{

        val cancelConnection = disconnecting

        fun handleConnect() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Connecting) {
                        didConnect
                    } else {
                        remain
                    }
                }
            }
        }

        internal val didConnect = suspend {
            Connected.Idle(emptyList(), lastKnownRssi, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when(oldState) {
                is Disconnected -> connectionManager.connect()
            }
        }
    }

    data class Reconnecting internal constructor(val attempt: Int,
                                                 val services: List<Service>,
                                                 override val lastKnownRssi: Int,
                                                 override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(lastKnownRssi, connectionManager),
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
            Connected.Idle(services, lastKnownRssi, connectionManager)
        }

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connected, is Reconnecting -> connectionManager.connect()
            }
        }

    }

    data class Disconnected internal constructor(override val lastKnownRssi: Int,
                                                 override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(lastKnownRssi, connectionManager) {

        fun startConnecting() {
            launch {
                connectionManager.stateRepo.takeAndChangeState { deviceState ->
                    if (deviceState is Disconnected) {
                        connect
                    } else {
                        remain
                    }
                }
            }
        }

        val connect = suspend {
            Connecting(lastKnownRssi, connectionManager)
        }

    }
    data class Disconnecting internal constructor(override val lastKnownRssi: Int,
                                                  override val connectionManager: BaseDeviceConnectionManager)
        : DeviceState(lastKnownRssi, connectionManager),
        HandleAfterOldStateIsRemoved<DeviceState> {

        override suspend fun afterOldStateIsRemoved(oldState: DeviceState) {
            when (oldState) {
                is Connecting, is Reconnecting, is Connected -> connectionManager.disconnect()
            }
        }
    }

    fun rssiDidUpdate(rssi: Int): suspend () -> DeviceState {
        return {
            when (this) {
                is Connected.Idle -> copy(lastKnownRssi = rssi)
                is Connected.HandlingAction -> copy(lastKnownRssi = rssi)
                is Connected.Discovering -> copy(lastKnownRssi = rssi)
                is Connecting -> copy(lastKnownRssi = rssi)
                is Reconnecting -> copy(lastKnownRssi = rssi)
                is Disconnecting -> copy(lastKnownRssi = rssi)
                is Disconnected -> copy(lastKnownRssi = rssi)
            }
        }
    }

    internal val didDisconnect = suspend {
        Disconnected(lastKnownRssi, connectionManager)
    }

    protected val disconnecting = suspend {
        Disconnecting(lastKnownRssi, connectionManager)
    }

}

class Device internal constructor(connectionSettings: ConnectionSettings,
                                  private val deviceInfoHolder: DeviceInfoHolder,
                                  private val initialRssi: Int,
                                  connectionBuilder: BaseDeviceConnectionManager.Builder,
                                  coroutineContext: CoroutineContext = MainQueueDispatcher)
    : HotStateRepo<DeviceState>(coroutineContext), DeviceInfo by deviceInfoHolder {

    internal val deviceConnectionManager = connectionBuilder.create(connectionSettings, deviceInfoHolder, this)

    override fun initialValue(): DeviceState {
        return DeviceState.Disconnected(initialRssi, deviceConnectionManager)
    }

}