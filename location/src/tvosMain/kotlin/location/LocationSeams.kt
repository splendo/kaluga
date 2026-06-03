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

package com.splendo.kaluga.location

import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

private val DEFAULT_POLL_INTERVAL = 1.minutes

// tvOS doesn't support background location updates, so [LocationPermission.background] has no effect here.
internal actual fun CLLocationManager.configureForLocation(permission: LocationPermission) = Unit

internal actual class DefaultLocationUpdater actual constructor(
    delegate: LocationDelegate,
    private val settings: AppleLocationSettings,
    private val coroutineScope: CoroutineScope,
) : LocationUpdater(delegate) {
    private var pollingJob: Job? = null

    // tvOS has no `startUpdatingLocation()`; it only supports one-shot `requestLocation()`. To approximate a
    // stream, requestLocation is re-issued every [AppleLocationSettings.pollInterval] until monitoring stops.
    override fun onStartUpdating(manager: CLLocationManager) {
        pollingJob?.cancel()
        pollingJob = coroutineScope.launch {
            while (isActive) {
                manager.requestLocation()
                delay(settings.pollInterval)
            }
        }
    }

    override fun onStopUpdating(manager: CLLocationManager) {
        pollingJob?.cancel()
        pollingJob = null
    }
}

internal actual fun CLLocationManager.isLocationServiceEnabled(): Boolean = CLLocationManager.locationServicesEnabled()

/**
 * tvOS [AppleLocationSettings].
 * @property pollInterval how often `requestLocation()` is re-issued while monitoring (tvOS has no streaming updates).
 */
actual class AppleLocationSettings actual constructor() {

    constructor(pollInterval: Duration) : this() {
        this.pollInterval = pollInterval
    }

    var pollInterval: Duration = DEFAULT_POLL_INTERVAL
        private set
}
