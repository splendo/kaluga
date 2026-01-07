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

import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled
 */
actual interface LocationMonitor : ServiceMonitor {

    /**
     * Builder for creating a [LocationMonitor]
     */
    actual class Builder(val locationManager: CLLocationManager = CLLocationManager()) {

        /**
         * Creates the [LocationMonitor]
         * @return the created [LocationMonitor]
         */
        actual fun create(): LocationMonitor = DefaultLocationMonitor(
            locationManager = locationManager,
        )
    }
}

/**
 * Default implementation of [LocationMonitor]
 * @param locationManager the [CLLocationManager] to manage the location
 */
class DefaultLocationMonitor(private val locationManager: CLLocationManager) :
    DefaultServiceMonitor(),
    LocationMonitor {

    internal class LocationManagerDelegate(private val updateState: () -> Unit) :
        NSObject(),
        CLLocationManagerDelegateProtocol {
        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            updateState()
        }
    }

    override val isServiceEnabled: Boolean
        get() = locationManager.locationServicesEnabled()

    override fun monitoringDidStart() {
        locationManager.delegate = LocationManagerDelegate(::updateState)
    }

    override fun monitoringDidStop() {
        locationManager.delegate = null
    }
}
