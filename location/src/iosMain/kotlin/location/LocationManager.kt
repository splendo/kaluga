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
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyReduced
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseLocationManager]
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 */
actual class DefaultLocationManager(
    settings: Settings,
    coroutineScope: CoroutineScope,
) : BaseLocationManager(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultLocationManager]
     */
    class Builder : BaseLocationManager.Builder {

        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager = DefaultLocationManager(
            settings,
            coroutineScope,
        )
    }

    private class Delegate(
        private val onLocationsChanged: MutableSharedFlow<Location.KnownLocation>,
    ) : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val locations = didUpdateLocations.mapNotNull { (it as? CLLocation)?.knownLocation }
            if (locations.isNotEmpty()) {
                handleLocationChanged(locations)
            }
        }

        override fun locationManager(manager: CLLocationManager, didUpdateToLocation: CLLocation, fromLocation: CLLocation) {
            handleLocationChanged(listOf(didUpdateToLocation.knownLocation))
        }

        override fun locationManager(manager: CLLocationManager, didFinishDeferredUpdatesWithError: NSError?) {
        }

        private fun handleLocationChanged(locations: List<Location.KnownLocation>) = locations.forEach {
            onLocationsChanged.tryEmit(it) // should always works as the buffer is DROP_OLDEST
        }
    }

    actual override val locationMonitor: LocationMonitor = LocationMonitor.Builder(CLLocationManager()).create()
    private val locationManager = MainCLLocationManagerAccessor {
        desiredAccuracy = if (locationPermission.precise) kCLLocationAccuracyBest else kCLLocationAccuracyReduced
        distanceFilter = settings.minUpdateDistanceMeters.toDouble()
    }

    private val locationUpdateDelegate: Delegate
    init {
        val sharedLocations = sharedLocations
        locationUpdateDelegate = Delegate(sharedLocations)
    }

    actual override suspend fun requestEnableLocation() {
        // No access to UIApplication.openSettingsURLString
        // We have to fallback to alert then?
    }

    actual override suspend fun startMonitoringLocation() {
        val locationUpdateDelegate = locationUpdateDelegate
        locationManager.updateLocationManager {
            delegate = locationUpdateDelegate
            startUpdatingLocation()
        }
    }

    actual override suspend fun stopMonitoringLocation() {
        launch {
            locationManager.updateLocationManager {
                stopUpdatingLocation()
                delegate = null
            }
        }
    }
}

/**
 * Default [BaseLocationStateRepoBuilder]
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Location permissions.
 * Needs to have [com.splendo.kaluga.permissions.location.LocationPermission] registered.
 */
actual class LocationStateRepoBuilder(
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions,
) : BaseLocationStateRepoBuilder {

    /**
     * Constructor
     * @param bundle the [NSBundle]
     */
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
    ): LocationStateRepo {
        return LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(), coroutineContext)
    }
}
