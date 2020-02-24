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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState

actual class LocationPermissionManager(actual val location: Permission.Location, repo: LocationPermissionStateRepo) : PermissionManager<Permission.Location>(repo) {

    override suspend fun requestPermission() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun initializeState(): PermissionState<Permission.Location> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun startMonitoring(interval: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun stopMonitoring() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

actual class LocationPermissionManagerBuilder :BaseLocationPermissionManagerBuilder {

    override fun create(location: Permission.Location, repo: LocationPermissionStateRepo): LocationPermissionManager {
        return LocationPermissionManager(location, repo)
    }
}