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
import com.splendo.kaluga.base.IOSVersion
import com.splendo.kaluga.base.monitor.ServiceMonitorState
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import kotlin.coroutines.CoroutineContext

actual interface LocationMonitor {
    /**
     * Builder for [LocationMonitor].
     * @param locationManager [CLLocationManager] used to get info about locations' state.
     */
    actual class Builder(
        private val locationManager: CLLocationManager = CLLocationManager()
    ) {
        actual constructor() : this(CLLocationManager())

        /**
         * Builder's create method.
         * @param coroutineContext [CoroutineContext] used to define the coroutine context where code will run.
         * @return [DefaultServiceMonitor]
         */
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor =
            DefaultLocationMonitor(
                locationManager = locationManager,
                coroutineContext = coroutineContext
            )
    }
}

class DefaultLocationMonitor(
    private val locationManager: CLLocationManager,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext), LocationMonitor {

    private val isUnauthorized: Boolean
        get() = locationManager.authorizationStatus == kCLAuthorizationStatusRestricted ||
            locationManager.authorizationStatus == kCLAuthorizationStatusDenied

    override fun startMonitoring() {
        super.startMonitoring()

        locationManager.delegate = if (IOSVersion.systemVersion >= IOSVersion(14)) {
            LocationManagerDelegate.NewLocationManagerDelegate(::updateState).delegate
        } else {
            LocationManagerDelegate.OldLocationManagerDelegate(::updateState).delegate
        }
        updateState()
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

    override fun stopMonitoring() {
        super.stopMonitoring()
        locationManager.delegate = null
    }

    private fun isUnauthorizedOrDefault(default: ServiceMonitorState) =
        if (isUnauthorized) {
            ServiceMonitorStateImpl.Initialized.Unauthorized
        } else {
            default as ServiceMonitorStateImpl
        }
}
