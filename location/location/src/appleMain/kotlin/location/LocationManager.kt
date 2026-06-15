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
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseLocationManager]
 * @param settings the [Settings] to configure this location manager
 * @param coroutineScope the [CoroutineScope] this location manager runs on
 * @param appleSettings the [AppleLocationSettings] with Apple-specific configuration
 */
actual class DefaultLocationManager(settings: Settings, coroutineScope: CoroutineScope, appleSettings: AppleLocationSettings = AppleLocationSettings()) :
    BaseLocationManager(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultLocationManager]
     * @param appleSettings the [AppleLocationSettings] with Apple-specific configuration
     */
    class Builder(private val appleSettings: AppleLocationSettings = AppleLocationSettings()) : BaseLocationManager.Builder {

        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager = DefaultLocationManager(settings, coroutineScope, appleSettings)
    }

    actual override val locationMonitor: LocationMonitor = LocationMonitor.Builder(CLLocationManager()).create()

    private val locationManager = MainCLLocationManagerAccessor {
        configureForLocation(locationPermission)
        desiredAccuracy = if (locationPermission.precise) kCLLocationAccuracyBest else kCLLocationAccuracyReduced
        distanceFilter = settings.minUpdateDistanceMeters.toDouble()
    }

    private val locationUpdater = DefaultLocationUpdater(LocationDelegate(sharedLocations), appleSettings, coroutineScope)

    actual override suspend fun requestEnableLocation() {
        // Apple platforms cannot deep-link to the location settings from here; consumers should surface a UI prompt instead.
    }

    actual override suspend fun startMonitoringLocation() {
        locationManager.updateLocationManager { locationUpdater.startUpdating(this) }
    }

    actual override suspend fun stopMonitoringLocation() {
        launch {
            locationManager.updateLocationManager { locationUpdater.stopUpdating(this) }
        }
    }
}

/**
 * Applies the platform-specific configuration for the given [permission] to a [CLLocationManager]
 * (e.g. background location updates on iOS). Accuracy and distance filter are configured by the caller.
 */
internal expect fun CLLocationManager.configureForLocation(permission: LocationPermission)

/**
 * Subscribes a [CLLocationManager] to location updates, emitting them to [delegate].
 *
 * Linking the [delegate] through the Swift wrapper (and unlinking it) is identical on every Apple platform
 * and lives here; subclasses only supply [onStartUpdating]/[onStopUpdating] — the platform-specific way to
 * actually drive updates (continuous vs. polled).
 */
internal abstract class LocationUpdater(private val delegate: LocationDelegate) {

    private var locationWrapper: KalugaLocationWrapper? = null

    fun startUpdating(manager: CLLocationManager) {
        locationWrapper?.unlink()
        locationWrapper = KalugaLocationWrapper.createByLinkingWithLocationManager(manager, delegate)
        onStartUpdating(manager)
    }

    fun stopUpdating(manager: CLLocationManager) {
        onStopUpdating(manager)
        locationWrapper?.unlink()
        locationWrapper = null
    }

    /**
     * Starts delivering location updates from [manager]. The [delegate] is already linked.
     */
    protected abstract fun onStartUpdating(manager: CLLocationManager)

    /**
     * Stops delivering location updates from [manager]. The [delegate] is unlinked afterwards.
     */
    protected abstract fun onStopUpdating(manager: CLLocationManager)
}

/**
 * The platform [LocationUpdater].
 *
 * [settings] and [coroutineScope] are only consumed on platforms that need them (e.g. tvOS polling);
 * other platforms ignore them.
 */
internal expect class DefaultLocationUpdater(delegate: LocationDelegate, settings: AppleLocationSettings, coroutineScope: CoroutineScope) : LocationUpdater {
    override fun onStartUpdating(manager: CLLocationManager)
    override fun onStopUpdating(manager: CLLocationManager)
}

/**
 * The [KalugaLocationDelegateProtocol] used by [LocationUpdater] on every Apple platform, forwarding
 * incoming [CLLocation]s to [onLocationsChanged]. Linked to the manager through the Swift wrapper.
 */
internal class LocationDelegate(private val onLocationsChanged: MutableSharedFlow<Location.KnownLocation>) :
    NSObject(),
    KalugaLocationDelegateProtocol {
    override fun didUpdateLocations(locations: List<*>, manager: CLLocationManager) {
        locations.mapNotNull { (it as? CLLocation)?.knownLocation }.forEach {
            onLocationsChanged.tryEmit(it) // should always work as the buffer is DROP_OLDEST
        }
    }
}

/**
 * Default [BaseLocationStateRepoBuilder]
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Location permissions.
 * Needs to have [com.splendo.kaluga.permissions.location.LocationPermission] registered.
 */
actual class LocationStateRepoBuilder(private val permissionsBuilder: suspend (CoroutineContext) -> Permissions) : BaseLocationStateRepoBuilder {

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
    ): LocationStateRepo = LocationStateRepo({ settingsBuilder(locationPermission, permissionsBuilder(it)) }, DefaultLocationManager.Builder(), coroutineContext)
}
