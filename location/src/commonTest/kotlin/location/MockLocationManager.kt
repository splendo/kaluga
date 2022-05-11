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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockLocationManager(
    locationPermission: LocationPermission,
    permissions: Permissions,
    autoRequestPermission: Boolean,
    autoEnableLocations: Boolean,
    locationStateRepo: LocationStateRepo
) : BaseLocationManager(
    locationPermission,
    permissions,
    autoRequestPermission,
    autoEnableLocations,
    locationStateRepo
) {

    val locationEnabled = MutableStateFlow(false)

    val startMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    val stopMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    val startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    val stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    val requestLocationEnableCompleted = EmptyCompletableDeferred()
    val startMonitoringLocationCompleted = EmptyCompletableDeferred()
    val stopMonitoringLocationCompleted = EmptyCompletableDeferred()

    override val locationMonitor: LocationMonitor = object : LocationMonitor {
        override val isServiceEnabled: Boolean
            get() = locationEnabled.value

        override val isEnabled: Flow<Boolean>
            get() = locationEnabled

        override fun startMonitoring() {}
        override fun stopMonitoring() {}
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsCompleted.complete()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsCompleted.complete()
    }

    override suspend fun startMonitoringLocationEnabled() {
        super.startMonitoringLocationEnabled()
        startMonitoringLocationEnabledCompleted.complete()
    }

    override fun stopMonitoringLocationEnabled() {
        super.stopMonitoringLocationEnabled()
        stopMonitoringLocationEnabledCompleted.complete()
    }

    override suspend fun requestLocationEnable() {
        requestLocationEnableCompleted.complete()
    }

    override suspend fun startMonitoringLocation() {
        startMonitoringLocationCompleted.complete()
    }

    override suspend fun stopMonitoringLocation() {
        stopMonitoringLocationCompleted.complete()
    }
}
