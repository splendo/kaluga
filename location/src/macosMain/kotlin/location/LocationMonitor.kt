/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled.
 *
 * macOS doesn't expose a `CLLocationManagerDelegate` callback for changes via Swift-interop wrappers
 * (the iOS implementation uses `KalugaLocationPermissionDelegateProtocol`); this implementation
 * therefore relies on polling via [CLLocationManager.locationServicesEnabled].
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
        actual fun create(): LocationMonitor = DefaultLocationMonitor(locationManager = locationManager)
    }
}

/**
 * Default macOS implementation of [LocationMonitor].
 */
class DefaultLocationMonitor(private val locationManager: CLLocationManager) :
    DefaultServiceMonitor(),
    LocationMonitor {

    override val isServiceEnabled: Boolean
        get() = CLLocationManager.locationServicesEnabled()

    override fun monitoringDidStart() {
        updateState()
    }

    override fun monitoringDidStop() = Unit
}
