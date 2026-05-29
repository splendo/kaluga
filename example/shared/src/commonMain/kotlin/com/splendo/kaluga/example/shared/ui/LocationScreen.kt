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

package com.splendo.kaluga.example.shared.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.location.location
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.koin.compose.koinInject

@Composable
fun LocationScreen(modifier: Modifier = Modifier) {
    val repoBuilder: LocationStateRepoBuilder = koinInject()
    val logger: Logger = koinInject()
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    DisposableEffect(Unit) { onDispose { scope.cancel() } }

    val locationFlow = remember {
        val permission = LocationPermission(background = true, precise = true)
        repoBuilder
            .create(
                permission,
                { p, pBuilder -> BaseLocationManager.Settings(p, pBuilder, logger = logger) },
                scope.coroutineContext,
            )
            .location()
            .map(::format)
            .stateIn(scope, SharingStarted.Eagerly, "Resolving location…")
    }
    val description by locationFlow.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(description)
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
