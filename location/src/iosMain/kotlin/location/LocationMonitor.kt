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

import com.splendo.kaluga.base.monitor.DefaultServiceMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitor
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

actual interface LocationMonitor : ServiceMonitor {
    actual class Builder(
        val locationManager: CLLocationManager = CLLocationManager()
    ) {
        actual fun create(): LocationMonitor = DefaultLocationMonitor(
            locationManager = locationManager
        )
    }
}

class DefaultLocationMonitor(private val locationManager: CLLocationManager) : DefaultServiceMonitor(), LocationMonitor {

    internal class LocationManagerDelegate(
        private val updateState: () -> Unit
    ) : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            updateState()
        }
    }

    override val isServiceEnabled: Boolean
        get() = locationManager.locationServicesEnabled()

    override fun startMonitoring() {
        super.startMonitoring()
        locationManager.delegate = LocationManagerDelegate(::updateState)
    }

    override fun stopMonitoring() {
        super.stopMonitoring()
        locationManager.delegate = null
    }
}
