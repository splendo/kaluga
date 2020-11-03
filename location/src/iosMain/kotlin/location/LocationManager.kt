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

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.location.CLAuthorizationStatusKotlin
import com.splendo.kaluga.permissions.location.toCLAuthorizationStatusKotlin
import kotlinx.coroutines.Dispatchers
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.CoroutineContext

actual class LocationManager(
    private val locationManager: CLLocationManager,
    locationPermission: Permission.Location,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationStateRepo: LocationStateRepo
) : BaseLocationManager(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo) {

    class Builder(private val locationManager: CLLocationManager = CLLocationManager()) : BaseLocationManager.Builder {

        override fun create(
            locationPermission: Permission.Location,
            permissions: Permissions,
            autoRequestPermission: Boolean,
            autoEnableLocations: Boolean,
            locationStateRepo: LocationStateRepo
        ): BaseLocationManager = LocationManager(
            locationManager,
            locationPermission,
            permissions,
            autoRequestPermission,
            autoEnableLocations,
            locationStateRepo
        )
    }

    private val locationManagerDelegate = object : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            if (isMonitoringLocationUpdate) {
                val locations = didUpdateLocations.mapNotNull { (it as? CLLocation)?.knownLocation }
                if (locations.isNotEmpty()) {
                    handleLocationChanged(locations)
                }
            }
        }

        override fun locationManager(manager: CLLocationManager, didUpdateToLocation: CLLocation, fromLocation: CLLocation) {
            if (isMonitoringLocationUpdate) {
                handleLocationChanged(listOf(didUpdateToLocation.knownLocation))
            }
        }

        override fun locationManager(manager: CLLocationManager, didFinishDeferredUpdatesWithError: NSError?) {
        }

        override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
            if (isMonitoringLocationEnable) {
                handleLocationEnabledChanged()
            }
        }
    }

    private val authorizationStatus: CLAuthorizationStatusKotlin get() = CLLocationManager.authorizationStatus().toCLAuthorizationStatusKotlin()

    private var _isMonitoringLocationEnable = AtomicBoolean(false)
    var isMonitoringLocationEnable
        get() = _isMonitoringLocationEnable.value
        set(value) { _isMonitoringLocationEnable.value = value }

    private var _isMonitoringLocationUpdate = AtomicBoolean(false)
    var isMonitoringLocationUpdate
        get() = _isMonitoringLocationUpdate.value
        set(value) { _isMonitoringLocationUpdate.value = value }

    override suspend fun startMonitoringLocationEnabled() {
        isMonitoringLocationEnable = true
        locationManager.delegate = locationManagerDelegate
        locationManager.startUpdatingLocation()
    }

    override suspend fun stopMonitoringLocationEnabled() {
        locationManager.stopUpdatingLocation()
        isMonitoringLocationEnable = false
    }

    override suspend fun isLocationEnabled(): Boolean = when (authorizationStatus) {
        // Enabled
        CLAuthorizationStatusKotlin.authorizedAlways,
        CLAuthorizationStatusKotlin.authorizedWhenInUse -> true
        // Disabled
        CLAuthorizationStatusKotlin.restricted,
        CLAuthorizationStatusKotlin.denied,
        CLAuthorizationStatusKotlin.notDetermined -> false
    }

    override suspend fun requestLocationEnable() {
        // No access to UIApplication.openSettingsURLString
        // We have to fallback to alert then?
    }

    override suspend fun startMonitoringLocation() {
        isMonitoringLocationUpdate = true
        locationManager.delegate = locationManagerDelegate
        locationManager.startUpdatingLocation()
    }

    override suspend fun stopMonitoringLocation() {
        locationManager.stopUpdatingLocation()
        isMonitoringLocationUpdate = false
    }
}

actual class LocationStateRepoBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val locationManager: CLLocationManager = CLLocationManager(),
    private val permissions: Permissions = Permissions(PermissionsBuilder(bundle), Dispatchers.Main)
) : LocationStateRepo.Builder {

    override fun create(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean, coroutineContext: CoroutineContext): LocationStateRepo {
        return LocationStateRepo(locationPermission, permissions, autoRequestPermission, autoEnableLocations, LocationManager.Builder(locationManager), coroutineContext)
    }
}
