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
import android.os.Build
import com.splendo.kaluga.permissions.base.AndroidPermissionState
import com.splendo.kaluga.permissions.base.AndroidPermissionsManager
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionManager.Settings
import com.splendo.kaluga.permissions.base.DefaultAndroidPermissionStateHandler
import com.splendo.kaluga.permissions.base.PermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/**
 * The [BasePermissionManager] to use as a default for [LocationPermission]
 * @param context the [Context] the [LocationPermission] is to be granted in
 * @param locationPermission the [LocationPermission] to manage
 * @param settings the [Settings] to apply to this manager.
 * @param coroutineScope the [CoroutineScope] of this manager.
 */
actual class DefaultLocationPermissionManager(private val context: Context, locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope) :
    BasePermissionManager<LocationPermission>(locationPermission, settings, coroutineScope) {

    private companion object {
        val FOREGROUND_REQUEST_TIMEOUT = 2.minutes
        val FOREGROUND_POLL_INTERVAL = 200.milliseconds
    }

    private val foregroundPermissions: Array<String> = listOfNotNull(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        if (permission.precise) Manifest.permission.ACCESS_FINE_LOCATION else null,
    ).toTypedArray()

    // From Android 10 (API 29) background location is a separate permission. On Android 11 (API 30)+
    // the system *ignores* a request that combines foreground and background location, so it must be
    // requested separately, and only after foreground location has been granted.
    private val backgroundPermissions: Array<String> = if (permission.background && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        emptyArray()
    }

    private val permissions: Array<String> = foregroundPermissions + backgroundPermissions

    private val permissionHandler = DefaultAndroidPermissionStateHandler(eventChannel, logTag, logger)
    private val permissionsManager = AndroidPermissionsManager(context, permissions, coroutineScope, logTag, logger, permissionHandler)

    private fun isForegroundGranted() = foregroundPermissions.all { AndroidPermissionState.get(context, it) == AndroidPermissionState.GRANTED }

    actual override fun requestPermissionDidStart() {
        if (backgroundPermissions.isEmpty()) {
            permissionsManager.requestPermissions(foregroundPermissions)
            return
        }
        // Request foreground first; once granted, request background as a separate step.
        launch {
            if (!isForegroundGranted()) {
                permissionsManager.requestPermissions(foregroundPermissions)
                val granted = withTimeoutOrNull(FOREGROUND_REQUEST_TIMEOUT) {
                    while (!isForegroundGranted()) {
                        delay(FOREGROUND_POLL_INTERVAL)
                    }
                    true
                } ?: false
                if (!granted) return@launch
            }
            permissionsManager.requestPermissions(backgroundPermissions)
        }
    }

    actual override fun monitoringDidStart(interval: Duration) {
        permissionsManager.startMonitoring(interval)
    }

    actual override fun monitoringDidStop() {
        permissionsManager.stopMonitoring()
    }
}

/**
 * A [BaseLocationPermissionManagerBuilder]
 * @param context the [PermissionContext] this permissions manager builder runs on
 */
actual class LocationPermissionManagerBuilder actual constructor(private val context: PermissionContext) : BaseLocationPermissionManagerBuilder {

    actual override fun create(locationPermission: LocationPermission, settings: Settings, coroutineScope: CoroutineScope): LocationPermissionManager =
        DefaultLocationPermissionManager(context.context, locationPermission, settings, coroutineScope)
}
