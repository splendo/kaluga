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

import com.splendo.kaluga.base.utils.byOrdinalOrDefault
import com.splendo.kaluga.permissions.IOSPermissionsHelper
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSBundle
import platform.darwin.NSObject

const val NSLocationWhenInUseUsageDescription = "NSLocationWhenInUseUsageDescription"
const val NSLocationAlwaysAndWhenInUseUsageDescription = "NSLocationAlwaysAndWhenInUseUsageDescription"
const val NSLocationAlwaysUsageDescription = "NSLocationAlwaysUsageDescription"

actual class LocationPermissionManager(
    private val bundle: NSBundle,
    actual val locationPermission: LocationPermission,
    stateRepo: PermissionStateRepo<LocationPermission>
) : PermissionManager<LocationPermission>(stateRepo) {

    private val locationManager = CLLocationManager()
    private val authorizationStatus = {
        CLLocationManager.authorizationStatus().toCLAuthorizationStatusKotlin().toAuthorizationStatus(locationPermission.background)
    }

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus /* = kotlin.Int */) {
            val locationPermissionManager = this@LocationPermissionManager
            IOSPermissionsHelper.handleAuthorizationStatus(didChangeAuthorizationStatus.toCLAuthorizationStatusKotlin().toAuthorizationStatus(locationPermissionManager.locationPermission.background), locationPermissionManager)
        }
    }

    override suspend fun requestPermission() {
        val locationDeclarations = mutableListOf(NSLocationWhenInUseUsageDescription)
        if (locationPermission.background) {
            locationDeclarations.addAll(listOf(NSLocationAlwaysAndWhenInUseUsageDescription, NSLocationAlwaysUsageDescription))
        }
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, *locationDeclarations.toTypedArray()).isEmpty()) {
            if (locationPermission.background)
                locationManager.requestAlwaysAuthorization()
            else
                locationManager.requestWhenInUseAuthorization()
        } else {
            revokePermission(true)
        }
    }

    override suspend fun startMonitoring(interval: Long) {
        locationManager.delegate = delegate
    }

    override suspend fun stopMonitoring() {
        locationManager.delegate = null
    }
}

actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    override fun create(locationPermission: LocationPermission, repo: PermissionStateRepo<LocationPermission>): PermissionManager<LocationPermission> {
        return LocationPermissionManager(context, locationPermission, repo)
    }
}

@Suppress("EnumEntryName") // we are modeling an iOS construct so we will stick as close to it as possible. Actual CLAuthorizationStatus values not available
enum class CLAuthorizationStatusKotlin {
    // https://developer.apple.com/documentation/corelocation/clauthorizationstatus
    notDetermined,
    restricted,
    denied,
    authorizedAlways,
    authorizedWhenInUse
}

fun CLAuthorizationStatus.toCLAuthorizationStatusKotlin(): CLAuthorizationStatusKotlin {
    return Enum.byOrdinalOrDefault(
        this,
        CLAuthorizationStatusKotlin.notDetermined
    )
}

private fun CLAuthorizationStatusKotlin.toAuthorizationStatus(background: Boolean): IOSPermissionsHelper.AuthorizationStatus {
    return when (this) {
        CLAuthorizationStatusKotlin.notDetermined -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        CLAuthorizationStatusKotlin.restricted -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        CLAuthorizationStatusKotlin.denied -> IOSPermissionsHelper.AuthorizationStatus.Denied
        CLAuthorizationStatusKotlin.authorizedAlways -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        CLAuthorizationStatusKotlin.authorizedWhenInUse -> if (background) IOSPermissionsHelper.AuthorizationStatus.Denied else IOSPermissionsHelper.AuthorizationStatus.Authorized
    }
}
