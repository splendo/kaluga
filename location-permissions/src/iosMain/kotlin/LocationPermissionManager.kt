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

package com.splendo.kaluga.permissions.location

import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.handleAuthorizationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreLocation.CLAccuracyAuthorization
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyReduced
import platform.Foundation.NSBundle
import platform.darwin.NSObject
import kotlin.time.Duration

const val NSLocationWhenInUseUsageDescription = "NSLocationWhenInUseUsageDescription"
const val NSLocationAlwaysAndWhenInUseUsageDescription = "NSLocationAlwaysAndWhenInUseUsageDescription"
const val NSLocationAlwaysUsageDescription = "NSLocationAlwaysUsageDescription"

actual class DefaultLocationPermissionManager(
    private val bundle: NSBundle,
    locationPermission: LocationPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private val locationManager = MainCLLocationManagerAccessor {
        desiredAccuracy = if (permission.precise) kCLLocationAccuracyBest else kCLLocationAccuracyReduced
    }
    private val authorizationStatus = suspend {
        locationManager.updateLocationManager {
            if (IOSVersion.systemVersion > IOSVersion(13)) {
                authorizationStatus to (accuracyAuthorization == CLAccuracyAuthorization.CLAccuracyAuthorizationFullAccuracy)
            } else {
                CLLocationManager.authorizationStatus() to true
            }
        }.toAuthorizationStatus(permission)
    }

    private val authorizationDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            launch {
                handleAuthorizationStatus(authorizationStatus())
            }
        }
        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus /* = kotlin.Int */) {
            launch {
                handleAuthorizationStatus(authorizationStatus())
            }
        }
    }

    override fun requestPermission() {
        super.requestPermission()
        val locationDeclarations = mutableListOf(NSLocationWhenInUseUsageDescription)
        if (permission.background) {
            locationDeclarations.addAll(listOf(NSLocationAlwaysAndWhenInUseUsageDescription, NSLocationAlwaysUsageDescription))
        }
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, *locationDeclarations.toTypedArray()).isEmpty()) {
            launch {
                locationManager.updateLocationManager {
                    if (permission.background)
                        requestAlwaysAuthorization()
                    else
                        requestWhenInUseAuthorization()
                }
            }
        } else {
            revokePermission(true)
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        launch {
            locationManager.updateLocationManager {
                delegate = authorizationDelegate
            }
            handleAuthorizationStatus(authorizationStatus())
        }
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        launch {
            locationManager.updateLocationManager {
                delegate = null
            }
        }
    }
}

actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    override fun create(locationPermission: LocationPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): LocationPermissionManager {
        return DefaultLocationPermissionManager(context, locationPermission, settings, coroutineScope)
    }
}

private fun Pair<CLAuthorizationStatus, Boolean>.toAuthorizationStatus(permission: LocationPermission): IOSPermissionsHelper.AuthorizationStatus {
    return when (first) {
        kCLAuthorizationStatusNotDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        kCLAuthorizationStatusRestricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        kCLAuthorizationStatusDenied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        kCLAuthorizationStatusAuthorizedAlways -> if (permission.precise && !second) IOSPermissionsHelper.AuthorizationStatus.Denied else IOSPermissionsHelper.AuthorizationStatus.Authorized
        kCLAuthorizationStatusAuthorizedWhenInUse -> if (permission.background || (permission.precise && !second)) IOSPermissionsHelper.AuthorizationStatus.Denied else IOSPermissionsHelper.AuthorizationStatus.Authorized
        else -> {
            com.splendo.kaluga.logging.error("Unknown CLAuthorizationStatus $first")
            IOSPermissionsHelper.AuthorizationStatus.Denied
        }
    }
}
