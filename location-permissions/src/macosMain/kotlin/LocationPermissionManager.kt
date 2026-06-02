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
import platform.CoreLocation.CLAccuracyAuthorization
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLLocationAccuracyReduced

// macOS uses a single usage-description key, unlike the when-in-use/always split on iOS & watchOS.
// Otherwise (macOS 11+) the authorization model matches: when-in-use vs always, and accuracy authorization.
private const val NS_LOCATION_USAGE_DESCRIPTION = "NSLocationUsageDescription"

internal actual fun CLLocationManager.configureForLocationPermission(permission: LocationPermission) {
    desiredAccuracy = if (permission.precise) kCLLocationAccuracyBest else kCLLocationAccuracyReduced
}

internal actual fun locationUsageDescriptions(permission: LocationPermission): List<String>? = listOf(NS_LOCATION_USAGE_DESCRIPTION)

internal actual fun CLLocationManager.requestLocationAuthorization(permission: LocationPermission) {
    if (permission.background) {
        requestAlwaysAuthorization()
    } else {
        requestWhenInUseAuthorization()
    }
}

actual fun CLLocationManager.authorizationStatus(locationPermission: LocationPermission): ApplePermissionsHelper.AuthorizationStatus =
    (authorizationStatus to (accuracyAuthorization == CLAccuracyAuthorization.CLAccuracyAuthorizationFullAccuracy))
        .toAuthorizationStatus(locationPermission)

private fun Pair<CLAuthorizationStatus, Boolean>.toAuthorizationStatus(permission: LocationPermission): ApplePermissionsHelper.AuthorizationStatus = when (first) {
    kCLAuthorizationStatusNotDetermined -> ApplePermissionsHelper.AuthorizationStatus.NotDetermined
    kCLAuthorizationStatusRestricted -> ApplePermissionsHelper.AuthorizationStatus.Restricted
    kCLAuthorizationStatusDenied -> ApplePermissionsHelper.AuthorizationStatus.Denied
    kCLAuthorizationStatusAuthorizedAlways ->
        if (permission.precise && !second) ApplePermissionsHelper.AuthorizationStatus.Denied
        else ApplePermissionsHelper.AuthorizationStatus.Authorized
    kCLAuthorizationStatusAuthorizedWhenInUse ->
        if (permission.background || (permission.precise && !second)) ApplePermissionsHelper.AuthorizationStatus.Denied
        else ApplePermissionsHelper.AuthorizationStatus.Authorized
    else -> {
        com.splendo.kaluga.logging.error("Unknown CLAuthorizationStatus $first")
        ApplePermissionsHelper.AuthorizationStatus.Denied
    }
}
