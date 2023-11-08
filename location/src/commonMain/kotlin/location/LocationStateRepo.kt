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
import com.splendo.kaluga.base.state.ColdStateFlowRepo
import com.splendo.kaluga.base.state.StateRepo
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A [StateRepo]/[MutableStateFlow] of [LocationState]
 */
typealias LocationStateFlowRepo = StateRepo<LocationState, MutableStateFlow<LocationState>>

private val defaultLocationDispatcher by lazy {
    singleThreadDispatcher("Location")
}

/**
 * An abstract [ColdStateFlowRepo] for managing [LocationState]
 * @param createNotInitializedState method for creating the initial [LocationState.NotInitialized] State
 * @param createInitializingState method for transitioning from a [LocationState.Inactive] into a [LocationState.Initializing] given an implementation of this [ColdStateFlowRepo]
 * @param createDeinitializingState method for transitioning from a [LocationState.Active] into a [LocationState.Deinitialized] given an implementation of this [ColdStateFlowRepo]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
abstract class BaseLocationStateRepo(
    createNotInitializedState: () -> LocationState.NotInitialized,
    createInitializingState: suspend ColdStateFlowRepo<LocationState>.(LocationState.Inactive) -> suspend () -> LocationState.Initializing,
    createDeinitializingState: suspend ColdStateFlowRepo<LocationState>.(LocationState.Active) -> suspend () -> LocationState.Deinitialized,
    coroutineContext: CoroutineContext,
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
    firstState = createNotInitializedState,
)

/**
 * A [BaseLocationStateRepo] managed using a [LocationManager]
 * @param createLocationManager method for creating the [LocationManager] to manage the [LocationState]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine.
 */
open class LocationStateImplRepo(
    createLocationManager: suspend () -> LocationManager,
    coroutineContext: CoroutineContext,
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
    },
    coroutineContext = coroutineContext,
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
                if (event.hasPermission) state.remain() else state.revokePermission
            }
            is LocationState.Disabled.NotPermitted -> if (event.hasPermission) state.permit(locationManager.isLocationEnabled()) else state.remain()
            else -> {
                state.remain()
            }
        }
    }
}

/**
 * A [LocationStateImplRepo] using a [BaseLocationManager]
 * @param settingsBuilder method for creating [BaseLocationManager.Settings]
 * @param builder the [BaseLocationManager.Builder] for building a [BaseLocationManager]
 * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine
 */
class LocationStateRepo(
    settingsBuilder: suspend (CoroutineContext) -> BaseLocationManager.Settings,
    builder: BaseLocationManager.Builder,
    coroutineContext: CoroutineContext,
) : LocationStateImplRepo(
    createLocationManager = {
        builder.create(
            settingsBuilder(coroutineContext + CoroutineName("LocationPermissions")),
            CoroutineScope(coroutineContext + CoroutineName("LocationManager")),
        )
    },
    coroutineContext = coroutineContext,
)

/**
 * Builder for creating a [LocationStateRepo]
 */
interface BaseLocationStateRepoBuilder {

    /**
     * Creates the [LocationStateRepo]
     * @param locationPermission the [LocationPermission] to use while monitoring the location
     * @param settingsBuilder method for creating [BaseLocationManager.Settings] using a [LocationPermission] and [Permissions]
     * @param coroutineContext the [CoroutineContext] the [CoroutineContext] used to create a coroutine scope for this state machine
     */
    fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> BaseLocationManager.Settings = { permission, permissions -> BaseLocationManager.Settings(permission, permissions) },
        coroutineContext: CoroutineContext = defaultLocationDispatcher,
    ): LocationStateRepo
}

/**
 * Default [BaseLocationStateRepoBuilder]
 */
expect class LocationStateRepoBuilder : BaseLocationStateRepoBuilder
