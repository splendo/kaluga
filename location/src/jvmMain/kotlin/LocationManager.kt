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


import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo

actual class LocationManager(locationPermissionRepo: LocationPermissionStateRepo, autoRequestPermission: Boolean, autoEnableLocations: Boolean, locationStateRepo: LocationStateRepo) : BaseLocationManager(locationPermissionRepo, autoRequestPermission,
    autoEnableLocations,
    locationStateRepo
) {

    override suspend fun startMonitoringLocationEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun stopMonitoringLocationEnabled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun isLocationEnabled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun requestLocationEnable(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun startMonitoringLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun stopMonitoringLocation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

