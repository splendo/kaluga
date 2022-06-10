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
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.IOSPermissionsHelper
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.handleAuthorizationStatus
import kotlinx.coroutines.CoroutineScope
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
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

    private val locationManager = CLLocationManager()
    private val authorizationStatus = {
        CLLocationManager.authorizationStatus().toCLAuthorizationStatusKotlin().toAuthorizationStatus(locationPermission.background)
    }

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus /* = kotlin.Int */) {
            handleAuthorizationStatus(didChangeAuthorizationStatus.toCLAuthorizationStatusKotlin().toAuthorizationStatus(permission.background))
        }
    }

    override fun requestPermission() {
        super.requestPermission()
        val locationDeclarations = mutableListOf(NSLocationWhenInUseUsageDescription)
        if (permission.background) {
            locationDeclarations.addAll(listOf(NSLocationAlwaysAndWhenInUseUsageDescription, NSLocationAlwaysUsageDescription))
        }
        if (IOSPermissionsHelper.missingDeclarationsInPList(bundle, *locationDeclarations.toTypedArray()).isEmpty()) {
            if (permission.background)
                locationManager.requestAlwaysAuthorization()
            else
                locationManager.requestWhenInUseAuthorization()
        } else {
            revokePermission(true)
        }
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        locationManager.delegate = delegate
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        locationManager.delegate = null
    }
}

actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    override fun create(locationPermission: LocationPermission, settings: BasePermissionManager.Settings, coroutineScope: CoroutineScope): LocationPermissionManager {
        return DefaultLocationPermissionManager(context, locationPermission, settings, coroutineScope)
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
