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

import com.splendo.kaluga.datetime.DefaultKalugaDate
import com.splendo.kaluga.location.BaseLocationManager.Settings
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * A default implementation of [BaseLocationManager] for the JS family (js + wasmJs), backed by the
 * browser [Geolocation API](https://developer.mozilla.org/en-US/docs/Web/API/Geolocation) via
 * `watchPosition`. Background updates and programmatic enabling are not available on the web.
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 */
actual class DefaultLocationManager(settings: Settings, coroutineScope: CoroutineScope) : BaseLocationManager(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultLocationManager]
     */
    class Builder : BaseLocationManager.Builder {
        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager = DefaultLocationManager(settings, coroutineScope)
    }

    actual override val locationMonitor: LocationMonitor = LocationMonitor.Builder().create()

    private val watcher = GeolocationWatcher()

    // Maps a raw `navigator.geolocation` reading (NaN ≙ absent) onto the location flow.
    private val deliverReading: GeolocationCallback = { latitude, longitude, altitude, horizontalAccuracy, verticalAccuracy, speed, course, timestampMillis ->
        handleLocationChanged(
            Location.KnownLocation(
                latitude = latitude,
                longitude = longitude,
                altitude = altitude.takeUnless { it.isNaN() },
                horizontalAccuracy = horizontalAccuracy.takeUnless { it.isNaN() },
                verticalAccuracy = verticalAccuracy.takeUnless { it.isNaN() },
                speed = speed.takeUnless { it.isNaN() },
                course = course.takeUnless { it.isNaN() },
                time = DefaultKalugaDate.epoch(timestampMillis.toLong().milliseconds),
            ),
        )
    }

    actual override suspend fun requestEnableLocation() {
        // The browser has no "enable location" toggle; firing a one-shot request is the closest analog
        // (it surfaces the prompt and yields a fix). The state machine only reaches here once permitted.
        watcher.requestOnce(deliverReading)
    }

    actual override suspend fun startMonitoringLocation() {
        watcher.start(deliverReading)
    }

    actual override suspend fun stopMonitoringLocation() {
        watcher.stop()
    }
}

/**
 * Default [BaseLocationStateRepoBuilder] for the JS family.
 * @param permissionsBuilder method for creating a [Permissions] for the [CoroutineContext] of the state repo
 */
actual class LocationStateRepoBuilder(private val permissionsBuilder: suspend (CoroutineContext) -> Permissions) : BaseLocationStateRepoBuilder {

    constructor() : this(
        { context ->
            Permissions(
                PermissionsBuilder().apply {
                    registerLocationPermissionIfNotRegistered()
                },
                context,
            )
        },
    )

    actual override fun create(
        locationPermission: LocationPermission,
        settingsBuilder: (LocationPermission, Permissions) -> Settings,
        coroutineContext: CoroutineContext,
    ): LocationStateRepo = LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(), coroutineContext)
}
