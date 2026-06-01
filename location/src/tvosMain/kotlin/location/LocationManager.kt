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

import com.splendo.kaluga.location.BaseLocationManager.Settings
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.MainCLLocationManagerAccessor
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyReduced
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

/**
 * tvOS implementation of [BaseLocationManager].
 *
 * tvOS does not support background location updates, so `LocationPermission.background` has no
 * effect on the configured [CLLocationManager]. The accuracy authorization flag is honored.
 */
actual class DefaultLocationManager(settings: Settings, coroutineScope: CoroutineScope) : BaseLocationManager(settings, coroutineScope) {

    class Builder : BaseLocationManager.Builder {
        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager = DefaultLocationManager(settings, coroutineScope)
    }

    private class Delegate(private val onLocationsChanged: MutableSharedFlow<Location.KnownLocation>) :
        NSObject(),
        KalugaLocationDelegateProtocol {
        override fun didUpdateLocations(locations: List<*>, manager: CLLocationManager) {
            val locs = locations.mapNotNull { (it as? CLLocation)?.knownLocation }
            locs.forEach { onLocationsChanged.tryEmit(it) }
        }
    }

    actual override val locationMonitor: LocationMonitor = LocationMonitor.Builder(CLLocationManager()).create()
    private val locationManager = MainCLLocationManagerAccessor {
        desiredAccuracy = if (locationPermission.precise) kCLLocationAccuracyBest else kCLLocationAccuracyReduced
        distanceFilter = settings.minUpdateDistanceMeters.toDouble()
    }

    private val locationUpdateDelegate = Delegate(sharedLocations)
    private var locationWrapper: KalugaLocationWrapper? = null

    actual override suspend fun requestEnableLocation() {
        // tvOS has no equivalent of UIApplication.openSettingsURLString. Consumers should surface a UI prompt instead.
    }

    actual override suspend fun startMonitoringLocation() {
        // tvOS only supports one-shot location via `requestLocation()` — there is no
        // `startUpdatingLocation()`. Each call to startMonitoringLocation triggers a single
        // delivery; callers that need a stream must invoke it again.
        val locationUpdateDelegate = locationUpdateDelegate
        locationManager.updateLocationManager {
            locationWrapper?.unlink()
            locationWrapper = KalugaLocationWrapper.createByLinkingWithLocationManager(this, locationUpdateDelegate)
            requestLocation()
        }
    }

    actual override suspend fun stopMonitoringLocation() {
        launch {
            locationManager.updateLocationManager {
                locationWrapper?.unlink()
                locationWrapper = null
            }
        }
    }
}

actual class LocationStateRepoBuilder(private val permissionsBuilder: suspend (CoroutineContext) -> Permissions) : BaseLocationStateRepoBuilder {

    constructor(
        bundle: NSBundle = NSBundle.mainBundle,
    ) : this(
        { context ->
            Permissions(
                PermissionsBuilder(bundle).apply {
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
