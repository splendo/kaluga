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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled
 */
actual interface LocationMonitor : ServiceMonitor {

    /**
     * Builder for creating a [LocationMonitor]
     * @param applicationContext the [Context] in which to monitor the location
     * @param locationManager the [LocationManager] to use for monitoring the location
     */
    actual class Builder(
        private val applicationContext: Context = ApplicationHolder.applicationContext,
        private val locationManager: LocationManager? = applicationContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager,
    ) {

        /**
         * Creates the [LocationMonitor]
         * @return the created [LocationMonitor]
         */
        actual fun create(): LocationMonitor = DefaultLocationMonitor(
            applicationContext = applicationContext,
            locationManager = locationManager,
        )
    }
}

/**
 * Default implementation of [LocationMonitor]
 * @param applicationContext the [Context] in which to monitor the location
 * @param locationManager the [LocationManager] to use for monitoring the location
 */
class DefaultLocationMonitor(private val applicationContext: Context, private val locationManager: LocationManager?) :
    DefaultServiceMonitor(),
    LocationMonitor {

    private val locationAvailabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationManager.MODE_CHANGED_ACTION) {
                updateState()
            }
        }
    }

    override val isServiceEnabled: Boolean
        get() = if (locationManager == null) {
            false
        } else {
            LocationManagerCompat.isLocationEnabled(locationManager)
        }

    override fun monitoringDidStart() {
        applicationContext.registerReceiver(
            locationAvailabilityBroadcastReceiver,
            IntentFilter(LocationManager.MODE_CHANGED_ACTION),
        )
    }

    override fun monitoringDidStop() {
        applicationContext.unregisterReceiver(locationAvailabilityBroadcastReceiver)
    }
}
