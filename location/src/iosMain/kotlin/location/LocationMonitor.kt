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

import com.splendo.kaluga.base.ServiceMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

actual class LocationMonitor(private val locationManager: CLLocationManager) : ServiceMonitor {

    actual class Builder {
        actual fun create(): LocationMonitor = LocationMonitor(
            locationManager = CLLocationManager()
        )
    }

    private val isPoweredOn: Boolean
        get() = locationManager.locationServicesEnabled()

    private val _isEnabled = MutableStateFlow(false)
    override val isEnabled: StateFlow<Boolean> = _isEnabled.asStateFlow()

    override fun startMonitoring() {
        locationManager.delegate = LocationManagerDelegate(::updateState)
        updateState()
    }

    override fun stopMonitoring() {
        locationManager.delegate = null
        updateState()
    }

    private fun updateState() {
        _isEnabled.value = isPoweredOn
    }
}

class LocationManagerDelegate(private val updateState: () -> Unit) : NSObject(), CLLocationManagerDelegateProtocol {
    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        updateState()
    }
}