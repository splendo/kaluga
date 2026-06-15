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

import com.splendo.kaluga.permissions.base.ApplePermissionsHelper
import com.splendo.kaluga.permissions.base.AuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAuthorizationStatusHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSBundle
import platform.darwin.NSObject
import kotlin.time.Duration

/**
 * Applies the per-target configuration to the [CLLocationManager] when it is first created.
 * (e.g. iOS toggles `allowsBackgroundLocationUpdates`; tvOS/watchOS set the desired accuracy; macOS does nothing.)
 */
internal expect fun CLLocationManager.configureForLocationPermission(permission: LocationPermission)

/**
 * The Info.plist usage-description keys that must be present to request [permission], or `null` if
 * the permission cannot be requested on this platform (e.g. background location on tvOS).
 */
internal expect fun locationUsageDescriptions(permission: LocationPermission): List<String>?

/**
 * Issues the platform authorization request for [permission].
 * Implementations differ because not every target exposes `requestAlwaysAuthorization` (tvOS does not).
 */
internal expect fun CLLocationManager.requestLocationAuthorization(permission: LocationPermission)

/**
 * Resolves the current platform authorization status for [permission].
 * Implementations differ in OS-version handling, accuracy authorization availability and which
 * [platform.CoreLocation.CLAuthorizationStatus] values are reachable on the target.
 */
expect fun CLLocationManager.authorizationStatus(locationPermission: LocationPermission): ApplePermissionsHelper.AuthorizationStatus

/**
 * The default Apple [BasePermissionManager] for [LocationPermission].
 *
 * The request/monitoring flow is identical across Apple targets; the genuinely platform-specific
 * pieces are delegated to [configureForLocationPermission], [locationUsageDescriptions],
 * [CLLocationManager.requestLocationAuthorization] and [CLLocationManager.authorizationStatus].
 * @param bundle the [NSBundle] the [LocationPermission] is to be granted in
 * @param locationPermission the [LocationPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultLocationPermissionManager(private val bundle: NSBundle, locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private class Delegate(private val locationPermission: LocationPermission, private val onPermissionChanged: AuthorizationStatusHandler) :
        NSObject(),
        KalugaLocationPermissionDelegateProtocol {
        override fun didChangeAuthorizationForLocationManager(manager: CLLocationManager) {
            onPermissionChanged.status(manager.authorizationStatus(locationPermission))
        }
    }

    private val permissionHandler = DefaultAuthorizationStatusHandler(eventChannel, logTag, logger)
    private val locationManager = MainCLLocationManagerAccessor { configureForLocationPermission(permission) }

    private val authorizationDelegate = Delegate(permission, permissionHandler)
    private var locationWrapper: KalugaLocationPermissionWrapper? = null

    actual override fun requestPermissionDidStart() {
        val declarations = locationUsageDescriptions(permission)
        if (declarations == null) {
            // The permission cannot be requested on this platform (e.g. background location on tvOS).
            permissionHandler.status(ApplePermissionsHelper.AuthorizationStatus.Denied)
            return
        }
        if (ApplePermissionsHelper.missingDeclarationsInPList(bundle, *declarations.toTypedArray()).isEmpty()) {
            launch {
                locationManager.updateLocationManager {
                    requestLocationAuthorization(permission)
                }
            }
        } else {
            permissionHandler.status(ApplePermissionsHelper.AuthorizationStatus.Restricted)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        val permission = permission
        launch {
            // Link a delegate so authorization changes are reported back immediately.
            val status = locationManager.updateLocationManager {
                locationWrapper?.unlink()
                locationWrapper = KalugaLocationPermissionWrapper.createByLinkingWithLocationManager(this, authorizationDelegate)
                authorizationStatus(permission)
            }
            permissionHandler.status(status)
        }
    }

    actual override fun monitoringDidStop() {
        launch {
            locationManager.updateLocationManager {
                locationWrapper?.unlink()
                locationWrapper = null
            }
        }
    }
}

/**
 * An Apple [BaseLocationPermissionManagerBuilder].
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {
    actual override fun create(locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope): LocationPermissionManager =
        DefaultLocationPermissionManager(context, locationPermission, settings, coroutineScope)
}
