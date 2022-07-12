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
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias ScanningStateFlowRepo = StateRepo<ScanningState, MutableStateFlow<ScanningState>>

abstract class BaseScanningStateRepo(
    createNotInitializedState: () -> ScanningState.NotInitialized,
    createInitializingState: suspend ColdStateFlowRepo<ScanningState>.(ScanningState.Inactive) -> suspend () -> ScanningState,
    createDeinitializingState: suspend ColdStateFlowRepo<ScanningState>.(ScanningState.Active) -> suspend () -> ScanningState.Deinitialized,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
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
    firstState = createNotInitializedState
)

open class ScanningStateImplRepo(
    createScanner: () -> Scanner,
    private val createDevice: (Identifier, DeviceInfoImpl, DeviceWrapper, BaseDeviceConnectionManager.Builder) -> Device,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : BaseScanningStateRepo(
    createNotInitializedState = { ScanningStateImpl.NotInitialized },
    createInitializingState = { state ->
        when (val stateImpl = state as ScanningStateImpl.Inactive) {
            is ScanningStateImpl.NotInitialized -> {
                val scanner = createScanner()
                (this as ScanningStateImplRepo).startMonitoringScanner(scanner)
                stateImpl.startInitializing(scanner)
            }
            is ScanningStateImpl.Deinitialized -> {
                (this as ScanningStateImplRepo).startMonitoringScanner(stateImpl.scanner)
                stateImpl.reinitialize
            }
        }
    },
    createDeinitializingState = { state ->
        (this as ScanningStateImplRepo).superVisorJob.cancelChildren()
        state.deinitialize
    }
) {

    private val superVisorJob = SupervisorJob(coroutineContext[Job])
    private suspend fun startMonitoringScanner(scanner: Scanner) {
        val hasStarted = EmptyCompletableDeferred()
        CoroutineScope(coroutineContext + superVisorJob).launch {
            scanner.events.onStart { hasStarted.complete() }.collect { event ->
                when (event) {
                    is Scanner.Event.PermissionChanged -> handlePermissionChangedEvent(event, scanner)
                    is Scanner.Event.BluetoothDisabled -> takeAndChangeState(remainIfStateNot = ScanningState.Enabled::class) { it.disable }
                    is Scanner.Event.BluetoothEnabled -> takeAndChangeState(remainIfStateNot = ScanningState.NoBluetooth.Disabled::class) { it.enable }
                    is Scanner.Event.FailedScanning -> takeAndChangeState(remainIfStateNot = ScanningState.Enabled.Scanning::class) { it.stopScanning }
                    is Scanner.Event.DeviceDiscovered -> handleDeviceDiscovered(event)
                    is Scanner.Event.DeviceConnected -> handleDeviceConnectionChanged(event.identifier, true)
                    is Scanner.Event.DeviceDisconnected -> handleDeviceConnectionChanged(event.identifier, false)
                }
            }
        }
        hasStarted.await()
    }

    private suspend fun handlePermissionChangedEvent(event: Scanner.Event.PermissionChanged, scanner: Scanner) = takeAndChangeState { state ->
        when (state) {
            is ScanningState.Initializing -> {
                state.initialized(event.hasPermission, scanner.isHardwareEnabled())
            }
            is ScanningState.Permitted -> {
                if (event.hasPermission)
                    state.remain()
                else
                    state.revokePermission
            }
            is ScanningState.NoBluetooth.MissingPermissions -> if (event.hasPermission) state.permit(scanner.isHardwareEnabled()) else state.remain()
            else -> { state.remain() }
        }
    }

    private suspend fun handleDeviceDiscovered(event: Scanner.Event.DeviceDiscovered) = takeAndChangeState(remainIfStateNot = ScanningState.Enabled.Scanning::class) { state ->
        state.discoverDevice(event.identifier, event.rssi, event.advertisementData) {
            val (deviceWrapper, connectionManagerBuilder) = event.deviceCreator()
            createDevice(event.identifier, DeviceInfoImpl(deviceWrapper, event.rssi, event.advertisementData), deviceWrapper, connectionManagerBuilder)
        }
    }

    private suspend fun handleDeviceConnectionChanged(identifier: Identifier, connected: Boolean) = useState { state ->
        if (state is ScanningState.Enabled) {
            state.discovered.devices.find { it.identifier == identifier }?.let { device ->
                if (connected)
                    device.handleConnected()
                else
                    device.handleDisconnected()
            }
        }
    }
}

class ScanningStateRepo(
    settingsBuilder: (CoroutineContext) -> BaseScanner.Settings,
    builder: BaseScanner.Builder,
    createDevice: (Identifier, DeviceInfoImpl, DeviceWrapper, BaseDeviceConnectionManager.Builder) -> Device,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    contextCreator: CoroutineContext.(String) -> CoroutineContext = { this + singleThreadDispatcher(it) },
) : ScanningStateImplRepo(
    createScanner = {
        builder.create(
            settingsBuilder(coroutineContext.contextCreator("BluetoothPermissions")),
            CoroutineScope(coroutineContext.contextCreator("BluetoothScanner"))
        )
    },
    createDevice = createDevice,
    coroutineContext = coroutineContext,
)
