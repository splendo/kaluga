/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A [StateRepo]/[MutableStateFlow] of [ScanningState]
 */
typealias ScanningStateFlowRepo = StateRepo<ScanningState, MutableStateFlow<ScanningState>>

/**
 * An abstract [ColdStateFlowRepo] for managing [ScanningState]
 * @param createNotInitializedState method for creating the initial [ScanningState.NotInitialized] State
 * @param createInitializingState method for transitioning from a [ScanningState.Inactive] into a [ScanningState.Initializing] given an implementation of this [ColdStateFlowRepo]
 * @param createDeinitializingState method for transitioning from a [ScanningState.Active] into a [ScanningState.Deinitialized] given an implementation of this [ColdStateFlowRepo]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
abstract class BaseScanningStateRepo(
    createNotInitializedState: () -> ScanningState.NotInitialized,
    createInitializingState: suspend ColdStateFlowRepo<ScanningState>.(ScanningState.Inactive) -> suspend () -> ScanningState,
    createDeinitializingState: suspend ColdStateFlowRepo<ScanningState>.(ScanningState.Active) -> suspend () -> ScanningState.Deinitialized,
    coroutineContext: CoroutineContext,
) : ColdStateFlowRepo<ScanningState>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        when (state) {
            is ScanningState.Inactive -> {
                repo.createInitializingState(state)
            }
            is ScanningState.Active, is ScanningState.NoHardware -> state.remain()
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        when (state) {
            is ScanningState.Active -> repo.createDeinitializingState(state)
            is ScanningState.Inactive, is ScanningState.NoHardware -> state.remain()
        }
    },
    firstState = createNotInitializedState,
)

/**
 * A [BaseScanningStateRepo] managed using a [Scanner]
 * @param createScanner method for creating the [Scanner] to manage the [ScanningState]
 * @param contextForIdentifier method for creating [CoroutineContext] given an [Identifier]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
open class ScanningStateImplRepo(
    createScanner: suspend () -> Scanner,
    private val contextForIdentifier: (Identifier) -> CoroutineContext,
    coroutineContext: CoroutineContext,
) : BaseScanningStateRepo(
    createNotInitializedState = { ScanningStateImpl.NotInitialized },
    createInitializingState = { state ->
        when (state) {
            is ScanningStateImpl.NotInitialized -> {
                val scanner = createScanner()
                (this as ScanningStateImplRepo).startMonitoringScanner(scanner)
                state.startInitializing(scanner)
            }
            is ScanningStateImpl.Deinitialized -> {
                (this as ScanningStateImplRepo).startMonitoringScanner(state.scanner)
                state.reinitialize
            }
            else -> state.remain()
        }
    },
    createDeinitializingState = { state ->
        val repo = this as ScanningStateImplRepo
        repo.supervisorJob.cancelChildren()
        repo.dispatcher?.close()
        repo.dispatcher = null
        state.deinitialize
    },
    coroutineContext = coroutineContext,
) {

    private val supervisorJob = SupervisorJob(coroutineContext[Job])
    private var dispatcher: CloseableCoroutineDispatcher? = null
    private fun startMonitoringScanner(scanner: Scanner) {
        val dispatcher = singleThreadDispatcher("ScanningStateRepo").also {
            this.dispatcher = it
        }
        CoroutineScope(coroutineContext + supervisorJob + dispatcher).launch {
            scanner.events.collect { event ->
                when (event) {
                    is Scanner.Event.PermissionChanged -> handlePermissionChangedEvent(event, scanner)
                    is Scanner.Event.BluetoothDisabled -> takeAndChangeState(remainIfStateNot = ScanningState.Enabled::class) { it.disable }
                    is Scanner.Event.BluetoothEnabled -> takeAndChangeState(remainIfStateNot = ScanningState.NoBluetooth.Disabled::class) { it.enable }
                    is Scanner.Event.FailedScanning -> takeAndChangeState(remainIfStateNot = ScanningState.Enabled.Scanning::class) {
                        it.stopScanning(BluetoothService.CleanMode.REMOVE_ALL)
                    }
                    is Scanner.Event.PairedDevicesRetrieved -> handlePairedDevice(event)
                }
            }
        }
        CoroutineScope(coroutineContext + supervisorJob + dispatcher).launch {
            scanner.connectionEvents.collect { connectionEvent ->
                when (connectionEvent) {
                    is Scanner.ConnectionEvent.DeviceConnected -> handleDeviceConnectionChanged(connectionEvent.identifier, true)
                    is Scanner.ConnectionEvent.DeviceDisconnected -> handleDeviceConnectionChanged(connectionEvent.identifier, false)
                }
            }
        }
        CoroutineScope(coroutineContext + supervisorJob + dispatcher).launch {
            scanner.discoveryEvents.collect { discoveredDevices ->
                handleDeviceDiscovered(discoveredDevices)
            }
        }
    }

    private suspend fun handlePermissionChangedEvent(event: Scanner.Event.PermissionChanged, scanner: Scanner) = takeAndChangeState { state ->
        when (state) {
            is ScanningState.Initializing -> {
                state.initialized(event.hasPermission, scanner.isHardwareEnabled())
            }
            is ScanningState.Permitted -> {
                if (event.hasPermission) {
                    state.remain()
                } else {
                    state.revokePermission
                }
            }
            is ScanningState.NoBluetooth.MissingPermissions -> if (event.hasPermission) state.permit(scanner.isHardwareEnabled()) else state.remain()
            else -> {
                state.remain()
            }
        }
    }

    private suspend fun handleDeviceDiscovered(event: List<Scanner.DeviceDiscovered>) = takeAndChangeState(remainIfStateNot = ScanningState.Enabled.Scanning::class) { state ->
        val discoveredDevices = event.map { deviceDiscovered ->
            ScanningState.Enabled.Scanning.DiscoveredDevice(deviceDiscovered.identifier, deviceDiscovered.rssi, deviceDiscovered.advertisementData) {
                val context = contextForIdentifier(deviceDiscovered.identifier)
                deviceDiscovered.deviceCreator(context)
            }
        }
        state.discoverDevices(discoveredDevices)
    }

    private suspend fun handlePairedDevice(event: Scanner.Event.PairedDevicesRetrieved) = takeAndChangeState(remainIfStateNot = ScanningState.Enabled::class) { state ->
        val devices = event.devices.associate {
            it.identifier to {
                val context = contextForIdentifier(it.identifier)
                it.deviceCreator(context)
            }
        }
        state.pairedDevices(devices, event.filter, event.removeForAllPairedFilters)
    }

    private suspend fun handleDeviceConnectionChanged(identifier: Identifier, connected: Boolean) = useState { state ->
        if (state is ScanningState.Enabled) {
            state.devices.allDevices[identifier]?.let { device ->
                if (connected) {
                    device.handleConnected()
                } else {
                    device.handleDisconnected()
                }
            }
        }
    }
}

/**
 * A [ScanningStateImplRepo] using a [BaseScanner]
 * @param settingsBuilder method for creating [BaseScanner.Settings]
 * @param builder the [BaseScanner.Builder] for building a [BaseScanner]
 * @param contextForIdentifier method for creating [CoroutineContext] given an [Identifier]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine
 */
class ScanningStateRepo(
    settingsBuilder: suspend (CoroutineContext) -> BaseScanner.Settings,
    builder: BaseScanner.Builder,
    contextForIdentifier: (Identifier) -> CoroutineContext,
    coroutineContext: CoroutineContext,
) : ScanningStateImplRepo(
    createScanner = {
        builder.create(
            settingsBuilder(coroutineContext + CoroutineName("BluetoothPermissions")),
            CoroutineScope(coroutineContext + CoroutineName("BluetoothScanner")),
        )
    },
    contextForIdentifier = contextForIdentifier,
    coroutineContext = coroutineContext,
)
