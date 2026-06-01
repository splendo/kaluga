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

package com.splendo.kaluga.permissions.location

import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.ApplePermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSBundle
import kotlin.time.Duration

private const val NS_LOCATION_USAGE_DESCRIPTION = "NSLocationUsageDescription"

/**
 * macOS [BasePermissionManager] for [LocationPermission].
 *
 * macOS exposes a much simpler authorization model than iOS — there is no when-in-use vs always
 * distinction for ordinary apps, no foreground/background separation, and (pre-macOS 14) no
 * accuracy authorization. This implementation maps to `requestAlwaysAuthorization()` and reports
 * the resolved status from [CLLocationManager.authorizationStatus]; the `background` and
 * `precise` fields of [LocationPermission] are ignored on macOS.
 */
actual class DefaultLocationPermissionManager(private val bundle: NSBundle, locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private val locationManager = MainCLLocationManagerAccessor { /* no per-permission config on macOS */ }

    actual override fun requestPermissionDidStart() {
        if (ApplePermissionsHelper.missingDeclarationsInPList(bundle, NS_LOCATION_USAGE_DESCRIPTION).isEmpty()) {
            launch {
                locationManager.updateLocationManager {
                    requestAlwaysAuthorization()
                }
            }
        } else {
            permissionHandler.status(ApplePermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        launch {
            val status = locationManager.updateLocationManager {
                CLLocationManager.authorizationStatus().toAuthorizationStatus()
            }
            permissionHandler.status(status)
        }
    }

    actual override fun monitoringDidStop() = Unit
}

/**
 * macOS [BaseLocationPermissionManagerBuilder].
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {
    actual override fun create(locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope): LocationPermissionManager =
        DefaultLocationPermissionManager(context, locationPermission, settings, coroutineScope)
}

private fun CLAuthorizationStatus.toAuthorizationStatus(): ApplePermissionsHelper.AuthorizationStatus = when (this) {
    kCLAuthorizationStatusNotDetermined -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined

    kCLAuthorizationStatusRestricted -> ApplePermissionsHelper.AuthorizationStatus.Restricted

    kCLAuthorizationStatusDenied -> ApplePermissionsHelper.AuthorizationStatus.Denied

    kCLAuthorizationStatusAuthorizedAlways -> ApplePermissionsHelper.AuthorizationStatus.Authorized

    else -> {
        error("LocationPermissionManager", "Unknown CLAuthorizationStatus $this")
        ApplePermissionsHelper.AuthorizationStatus.Denied
    }
}
