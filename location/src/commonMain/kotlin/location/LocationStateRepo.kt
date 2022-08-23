/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.location

import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.state.ColdStateFlowRepo
import com.splendo.kaluga.state.ColdStateRepo
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias LocationStateFlowRepo = StateRepo<LocationState, MutableStateFlow<LocationState>>

private val defaultLocationDispatcher by lazy {
    singleThreadDispatcher("Location")
}

abstract class BaseLocationStateRepo(
    createNotInitializedState: () -> LocationState.NotInitialized,
    createInitializingState: suspend ColdStateFlowRepo<LocationState>.(LocationState.Inactive) -> suspend () -> LocationState,
    createDeinitializingState: suspend ColdStateFlowRepo<LocationState>.(LocationState.Active) -> suspend () -> LocationState.Deinitialized,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : ColdStateFlowRepo<LocationState>(
    coroutineContext = coroutineContext,
    initChangeStateWithRepo = { state, repo ->
        when (state) {
            is LocationState.Inactive -> {
                repo.createInitializingState(state)
            }
            is LocationState.Active -> state.remain()
        }
    },
    deinitChangeStateWithRepo = { state, repo ->
        when (state) {
            is LocationState.Active -> repo.createDeinitializingState(state)
            is LocationState.Inactive -> state.remain()
        }
    },
    firstState = createNotInitializedState
)

open class LocationStateImplRepo(
    createLocationManager: () -> LocationManager,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : BaseLocationStateRepo(
    createNotInitializedState = { LocationStateImpl.NotInitialized },
    createInitializingState = { state ->
        when (state) {
            is LocationStateImpl.NotInitialized -> {
                val locationManager = createLocationManager()
                (this as LocationStateImplRepo).startMonitoringLocationManager(locationManager)
                state.startInitializing(locationManager)
            }
            is LocationStateImpl.Deinitialized -> {
                (this as LocationStateImplRepo).startMonitoringLocationManager(state.locationManager)
                state.reinitialize
            }
            else -> state.remain()
        }
    },
    createDeinitializingState = { state ->
        (this as LocationStateImplRepo).superVisorJob.cancelChildren()
        state.deinitialized
    }
) {

    private val superVisorJob = SupervisorJob(coroutineContext[Job])
    private suspend fun startMonitoringLocationManager(locationManager: LocationManager) {
        CoroutineScope(coroutineContext + superVisorJob).launch {
            locationManager.events.collect { event ->
                when (event) {
                    is LocationManager.Event.PermissionChanged -> handlePermissionChangedEvent(event, locationManager)
                    is LocationManager.Event.LocationDisabled -> takeAndChangeState(remainIfStateNot = LocationState.Enabled::class) { it.disable }
                    is LocationManager.Event.LocationEnabled -> takeAndChangeState(remainIfStateNot = LocationState.Disabled.NoGPS::class) { it.enable }
                }
            }
        }
        CoroutineScope(coroutineContext + superVisorJob).launch {
            locationManager.locations.collect { location ->
                takeAndChangeState(remainIfStateNot = LocationState.Enabled::class) {
                    it.updateWithLocation(location)
                }
            }
        }
    }

    private suspend fun handlePermissionChangedEvent(event: LocationManager.Event.PermissionChanged, locationManager: LocationManager) = takeAndChangeState { state ->
        when (state) {
            is LocationState.Initializing -> {
                state.initialize(event.hasPermission, locationManager.isLocationEnabled())
            }
            is LocationState.Permitted -> {
                if (event.hasPermission)
                    state.remain()
                else
                    state.revokePermission
            }
            is LocationState.Disabled.NotPermitted -> if (event.hasPermission) state.permit(locationManager.isLocationEnabled()) else state.remain()
            else -> { state.remain() }
        }
    }
}

/**
 * A [ColdStateRepo] that tracks the [LocationState] of the user.
 * Since this is a coldStateRepo location changes will only be requested when there is at least one observer.
 * @param settingsBuilder A function that creates the [BaseLocationManager.Settings] used for this Location State Repo.
 * @param builder
 * @param autoRequestPermission If 'true` the user will automatically receive a request to provide permissions when missing. Set this to `false` if manual permission requests are required.
 * @param autoEnableLocations If `true` the user will automatically receive a request to enable GPS if it is disabled. Set this to `false` if manual gps enabling is required.
 * @param locationManagerBuilder The [BaseLocationManager.Builder] to create the [LocationManager] managing the location state.
 */
class LocationStateRepo(
    settingsBuilder: (CoroutineContext) -> BaseLocationManager.Settings,
    builder: BaseLocationManager.Builder,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate, // singleThreadDispatcher(it)
) : LocationStateImplRepo(
    createLocationManager = {
        builder.create(
            settingsBuilder(coroutineContext + CoroutineName("LocationPermissions")),
            CoroutineScope(coroutineContext + CoroutineName("LocationManager"))
        )
    },
    coroutineContext = coroutineContext,
) {
    interface Builder {
        fun create(
            locationPermission: LocationPermission,
            settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings = { permission, permissions -> BaseLocationManager.Settings(permission, permissions) },
            coroutineContext: CoroutineContext = defaultLocationDispatcher
        ): LocationStateRepo
    }
}

expect class LocationStateRepoBuilder : LocationStateRepo.Builder
