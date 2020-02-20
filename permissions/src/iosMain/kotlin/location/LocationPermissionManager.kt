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

import com.splendo.kaluga.permissions.*
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSBundle
import platform.Photos.*
import platform.darwin.NSObject

actual class LocationPermissionManager(
    private val bundle: NSBundle,
    actual val location: Permission.Location,
    stateRepo: LocationPermissionStateRepo
) : PermissionManager<Permission.Location>(stateRepo) {

    private val locationManager = CLLocationManager()
    private val authorizationStatus = {
        CLLocationManager.authorizationStatus().toAuthorizationStatus(location.background)
    }

    private val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus /* = kotlin.Int */) {
            val locationPermissionManager = this@LocationPermissionManager
            IOSPermissionsHelper.handleAuthorizationStatus(didChangeAuthorizationStatus.toAuthorizationStatus(locationPermissionManager.location.background), locationPermissionManager)
        }

    }

    override suspend fun requestPermission() {
        val locationDeclarations = mutableListOf("NSLocationWhenInUseUsageDescription")
        if (location.background) {
            locationDeclarations.addAll(listOf("NSLocationAlwaysAndWhenInUseUsageDescription", "NSLocationAlwaysUsageDescription"))
        }
        if (IOSPermissionsHelper.checkDeclarationInPList(bundle, *locationDeclarations.toTypedArray()).isEmpty()) {
            if (location.background)
                locationManager.requestAlwaysAuthorization()
            else
                locationManager.requestWhenInUseAuthorization()
        } else {
            revokePermission(true)
        }
    }

    override fun initializeState(): PermissionState<Permission.Location> {
        return IOSPermissionsHelper.getPermissionState(authorizationStatus(), this)
    }

    override fun startMonitoring(interval: Long) {
        locationManager.delegate = delegate
    }

    override fun stopMonitoring() {
        locationManager.delegate = null
    }

}

actual class LocationPermissionManagerBuilder(private val bundle: NSBundle = NSBundle.mainBundle) : BaseLocationPermissionManagerBuilder {

    override fun create(location: Permission.Location, repo: LocationPermissionStateRepo): LocationPermissionManager {
        return LocationPermissionManager(bundle, location, repo)
    }

}

private fun CLAuthorizationStatus.toAuthorizationStatus(background: Boolean): IOSPermissionsHelper.AuthorizationStatus {
    return when(this) {
        0 -> IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        1 -> IOSPermissionsHelper.AuthorizationStatus.Restricted
        2 -> IOSPermissionsHelper.AuthorizationStatus.Denied
        3 -> IOSPermissionsHelper.AuthorizationStatus.Authorized
        4 -> if (background) IOSPermissionsHelper.AuthorizationStatus.Denied else IOSPermissionsHelper.AuthorizationStatus.Authorized
        else -> {
            com.splendo.kaluga.log.error(
                "LocationPermissionManager",
                "Unknown LocationManagerAuthorization status={$this}"
            )
            IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        }
    }
}