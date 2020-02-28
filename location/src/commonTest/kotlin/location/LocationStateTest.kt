/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete


class LocationStateTest : FlowableTest<LocationState>() {



}

class MockLocationManager(locationPermission: Permission.Location,
                          locationPermissionManagerBuilder: LocationPermissionManagerBuilder,
                          autoRequestPermission: Boolean, autoEnableLocations: Boolean,
                          locationStateRepo: LocationStateRepo
) : BaseLocationManager(locationPermission, locationPermissionManagerBuilder, autoRequestPermission,
    autoEnableLocations, locationStateRepo
) {

    var locationEnabled: Boolean = false

    var startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    var stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    var requestLocationEnableCompleted = EmptyCompletableDeferred()
    var startMonitoringLocationCompleted = EmptyCompletableDeferred()
    var stopMonitoringLocationCompleted = EmptyCompletableDeferred()

    fun reset() {
        startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
        stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
        requestLocationEnableCompleted = EmptyCompletableDeferred()
        startMonitoringLocationCompleted = EmptyCompletableDeferred()
        stopMonitoringLocationCompleted = EmptyCompletableDeferred()
    }

    override suspend fun startMonitoringLocationEnabled() {
        startMonitoringLocationEnabledCompleted.complete()
    }

    override suspend fun stopMonitoringLocationEnabled() {
        stopMonitoringLocationEnabledCompleted.complete()
    }

    override suspend fun isLocationEnabled(): Boolean {
        return locationEnabled
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
