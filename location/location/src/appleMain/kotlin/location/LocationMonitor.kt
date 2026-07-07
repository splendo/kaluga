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
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
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
        actual fun create(): LocationMonitor = DefaultLocationMonitor(locationManager = locationManager)
    }
}

/**
 * Default implementation of [LocationMonitor]
 * @param locationManager the [CLLocationManager] to manage the location
 */
class DefaultLocationMonitor(private val locationManager: CLLocationManager) :
    DefaultServiceMonitor(),
    LocationMonitor {

    private val serviceStateObserver = LocationServiceStateObserver(locationManager, ::updateState)

    override val isServiceEnabled: Boolean
        get() = locationManager.isLocationServiceEnabled()

    override fun monitoringDidStart() {
        serviceStateObserver.start()
    }

    override fun monitoringDidStop() {
        serviceStateObserver.stop()
    }
}

/**
 * Whether the location service is enabled, read the way the platform expects (instance vs class method).
 */
internal expect fun CLLocationManager.isLocationServiceEnabled(): Boolean

/**
 * Observes changes to the location service authorization state, invoking [onServiceStateChanged] when they occur.
 *
 * Linking goes through the [KalugaLocationPermissionWrapper] Swift wrapper on every platform: a Kotlin object
 * cannot be set directly as a `CLLocationManager` delegate without risking a Kotlin/Native freeze, so the
 * (strong) Swift wrapper holds the delegate and forwards callbacks.
 */
internal class LocationServiceStateObserver(private val manager: CLLocationManager, private val onServiceStateChanged: () -> Unit) {

    private val delegate = LocationManagerDelegate(onServiceStateChanged)

    private class LocationManagerDelegate(private val updateState: () -> Unit) :
        NSObject(),
        CLLocationManagerDelegateProtocol {

        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            updateState()
        }
    }

    fun start() {
        manager.delegate = delegate
    }

    fun stop() {
        manager.delegate = null
    }
}
