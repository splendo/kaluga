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
import com.splendo.kaluga.service.ServiceMonitorState
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import kotlin.coroutines.CoroutineContext

/**
 * A [ServiceMonitor] that monitors whether the location service is enabled
 */
actual interface LocationMonitor {

    /**
     * Builder for creating a [LocationMonitor]
     */
    actual class Builder(
        val locationManager: CLLocationManager = CLLocationManager()
    ) {

        /**
         * Creates the [LocationMonitor]
         * @return the created [LocationMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor =
            DefaultLocationMonitor(
            locationManager = locationManager,
            coroutineContext = coroutineContext
        )
    }
}

/**
 * Default implementation of [LocationMonitor]
 * @param locationManager the [CLLocationManager] to manage the location
 */
class DefaultLocationMonitor(
    private val locationManager: CLLocationManager,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext = coroutineContext), LocationMonitor {

    private val isUnauthorized: Boolean
        get() = locationManager.authorizationStatus == kCLAuthorizationStatusRestricted ||
            locationManager.authorizationStatus == kCLAuthorizationStatusDenied

    override fun monitoringDidStart() {
        locationManager.delegate = if (IOSVersion.systemVersion >= IOSVersion(14)) {
            LocationManagerDelegate.NewLocationManagerDelegate(::updateState).delegate
        } else {
            LocationManagerDelegate.OldLocationManagerDelegate(::updateState).delegate
        }
        updateState()
    }

    override fun monitoringDidStop() {
        locationManager.delegate = null
    }

    private fun updateState() {
        launchTakeAndChangeState {
            {
                if (locationManager.locationServicesEnabled) {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Enabled)
                } else {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Disabled)
                }
            }
        }
    }

    private fun isUnauthorizedOrDefault(default: ServiceMonitorState) =
        if (isUnauthorized) {
            ServiceMonitorStateImpl.Initialized.Unauthorized
        } else {
            default as ServiceMonitorStateImpl
        }
}
