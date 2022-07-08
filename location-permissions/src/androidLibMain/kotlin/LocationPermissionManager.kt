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
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.handleAndroidPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

actual class DefaultLocationPermissionManager(
    context: Context,
    locationPermission: LocationPermission,
    settings: Settings,
    coroutineScope: CoroutineScope
) : BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private val permissions: Array<String> get() {
        val result = mutableListOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission.precise)
            result.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission.background && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            result.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        return result.toTypedArray()
    }

    private val permissionsManager = AndroidPermissionsManager(context, permissions, coroutineScope, ::logDebug, ::logError, ::handleAndroidPermissionState)

    override fun requestPermission() {
        super.requestPermission()
        permissionsManager.requestPermissions()
    }

    override fun startMonitoring(interval: Duration) {
        super.startMonitoring(interval)
        permissionsManager.startMonitoring(interval)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        permissionsManager.stopMonitoring()
    }
}

actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    override fun create(
        locationPermission: LocationPermission,
        settings: BasePermissionManager.Settings,
        coroutineScope: CoroutineScope
    ): LocationPermissionManager {
        return DefaultLocationPermissionManager(context.context, locationPermission, settings, coroutineScope)
    }
}
