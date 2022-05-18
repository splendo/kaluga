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

import android.Manifest
import android.content.Context
import com.splendo.kaluga.permissions.AndroidPermissionsManager
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo

actual class LocationPermissionManager(
    context: Context,
    actual val locationPermission: LocationPermission,
    stateRepo: PermissionStateRepo<LocationPermission>
) : PermissionManager<LocationPermission>(stateRepo) {

    private val permissions: Array<String> get() {
        val result = mutableListOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (locationPermission.precise)
            result.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission.background && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            result.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        return result.toTypedArray()
    }

    private val permissionsManager = AndroidPermissionsManager(context, this, permissions)

    override suspend fun requestPermission() {
        permissionsManager.requestPermissions()
    }

    override suspend fun startMonitoring(interval: Long) {
        permissionsManager.startMonitoring(interval)
    }

    override suspend fun stopMonitoring() {
        permissionsManager.stopMonitoring()
    }
}

actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    override fun create(locationPermission: LocationPermission, repo: PermissionStateRepo<LocationPermission>): PermissionManager<LocationPermission> {
        return LocationPermissionManager(context.context, locationPermission, repo)
    }
}
