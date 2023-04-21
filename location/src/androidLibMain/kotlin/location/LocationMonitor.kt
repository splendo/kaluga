/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import com.splendo.kaluga.service.ServiceMonitorState
import kotlin.coroutines.CoroutineContext

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled
 */
actual interface LocationMonitor {

    /**
     * Builder for creating a [LocationMonitor]
     * @param applicationContext the [Context] in which to monitor the location
     * @param locationManager the [LocationManager] to use for monitoring the location
     */
    actual class Builder(
        private val applicationContext: Context = ApplicationHolder.applicationContext,
        private val locationManager: LocationManager = applicationContext
            .getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: throw IllegalArgumentException(
                "LocationService should not be null, please check your device capabilities."
            )
    ) {
        constructor() : this(
            ApplicationHolder.applicationContext,
            ApplicationHolder.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        )

        /**
         * Creates the [LocationMonitor]
         * @return the created [LocationMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor = DefaultLocationMonitor(
            applicationContext = applicationContext,
            locationManager = locationManager,
            coroutineContext
        )
    }
}

/**
 * Default implementation of [LocationMonitor]
 * @param applicationContext the [Context] in which to monitor the location
 * @param locationManager the [LocationManager] to use for monitoring the location
 */
class DefaultLocationMonitor(
    private val applicationContext: Context,
    private val locationManager: LocationManager,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext = coroutineContext), LocationMonitor {

    private val isUnauthorized: Boolean
        get() = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_DENIED


    private val locationAvailabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.MODE_CHANGED_ACTION) {
                val isLocationEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    intent.getBooleanExtra(LocationManager.EXTRA_LOCATION_ENABLED, false)
                } else {
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }
                debug(TAG) { "location state is $isLocationEnabled" }
                launchTakeAndChangeState {
                    {
                        if (isLocationEnabled) {
                            isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Enabled)
                        } else {
                            isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Disabled)
                        }
                    }
                }
            }
        }
    }

    override fun monitoringDidStart() {
        applicationContext.registerReceiver(
            locationAvailabilityBroadcastReceiver,
            IntentFilter(LocationManager.MODE_CHANGED_ACTION)
        )
        launchTakeAndChangeState {
            {
                val isLocationEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    locationManager.isLocationEnabled
                } else {
                    LocationManagerCompat.isLocationEnabled(locationManager)
                }
                debug(TAG) { "(startMonitoring) Location isEnabled $isLocationEnabled" }
                if (isLocationEnabled) {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Enabled)
                } else {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Disabled)
                }
            }
        }
    }

    override fun monitoringDidStop() {
        if (locationAvailabilityBroadcastReceiver.isOrderedBroadcast) {
            applicationContext.unregisterReceiver(locationAvailabilityBroadcastReceiver)
        }
    }

    private fun isUnauthorizedOrDefault(default: ServiceMonitorState) =
        if (isUnauthorized) {
            ServiceMonitorStateImpl.Initialized.Unauthorized
        } else {
            default as ServiceMonitorStateImpl
        }
}
