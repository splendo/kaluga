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

package com.splendo.kaluga.example.feature.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.location.location
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocationViewModel(private val repoBuilder: LocationStateRepoBuilder, private val permissionsBuilder: PermissionsBuilder, private val logger: Logger) : ViewModel() {

    sealed interface State {
        /** The location permission factory is still being registered (shown as a spinner). */
        data object Registering : State
        data class Located(val description: String) : State
    }

    private val _state = MutableStateFlow<State>(State.Registering)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // Register the location permission factory if no other feature has wired it up yet.
            // Typically a no-op on revisits; on cold launch it materialises the permission stack.
            permissionsBuilder.registerLocationPermissionIfNotRegistered(
                settings = BasePermissionManager.Settings(logger = logger),
            )
            val permission = LocationPermission(background = true, precise = true)
            repoBuilder
                .create(
                    permission,
                    { p, pBuilder -> BaseLocationManager.Settings(p, pBuilder, logger = logger) },
                    coroutineContext,
                )
                .location()
                .map(::format)
                .collect { _state.value = State.Located(it) }
        }
    }
}

private fun format(location: Location): String = when (location) {
    is Location.KnownLocation -> "${location.latitudeDMS} ${location.longitudeDMS}"

    is Location.UnknownLocation -> {
        val lastKnown = (location as? Location.UnknownLocation.WithLastLocation)?.let {
            " Last Known Location: ${it.lastKnownLocation.latitudeDMS} ${it.lastKnownLocation.longitudeDMS}"
        } ?: ""
        "Unknown Location. Reason: ${location.reason.name}$lastKnown"
    }
}
