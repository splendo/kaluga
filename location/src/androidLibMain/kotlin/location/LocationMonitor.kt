/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.ServiceMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class LocationMonitor(
    private val applicationContext: Context
) : ServiceMonitor {

    actual class Builder {
        actual fun create() = LocationMonitor(applicationContext = ApplicationHolder.applicationContext)
    }

    private val locationAvailabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            println("LocationMonitor: arrived in onReceived")
            if (intent?.action == LocationManager.MODE_CHANGED_ACTION) {
                println("LocationMonitor: intent?.action == LocationManager.MODE_CHANGED_ACTION")
                _isEnabled.value = isLocationEnabled
            }
        }
    }

    private val locationManager: LocationManager =
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val isLocationEnabled: Boolean
        get() = LocationManagerCompat.isLocationEnabled(locationManager)

    private val _isEnabled = MutableStateFlow(isLocationEnabled)
    override val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()

    override fun startMonitoring() {
        applicationContext.registerReceiver(
            locationAvailabilityBroadcastReceiver,
            IntentFilter(LocationManager.MODE_CHANGED_ACTION)
        )
    }

    override fun stopMonitoring() {
        applicationContext.unregisterReceiver(locationAvailabilityBroadcastReceiver)
    }
}
