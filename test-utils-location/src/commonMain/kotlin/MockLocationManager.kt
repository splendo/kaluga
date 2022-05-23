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

package com.splendo.kaluga.test.location

import co.touchlab.stately.collections.IsoMutableList
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Mock implementation of [BaseLocationManager]
 * @param initialLocationEnabled Sets the initial state of location
 * @param locationPermission The [LocationPermission] managed by this manager
 * @param permissions The [Permissions] to get the requested [locationPermission] from
 * @param autoRequestPermission If `true` this will automatically request permissions if missing
 * @param autoEnableLocations If `true` this will automatically try to enable locations
 * @param locationStateRepo The [LocationStateRepo] managed by this manager
 */
class MockLocationManager(
    initialLocationEnabled: Boolean,
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

    /**
     * Mock implementation of [BaseLocationManager.Builder]
     * @param initialLocationEnabled Sets the initial state of location
     * @param setupMocks If `true` sets up [createMock] to build [MockLocationManager]
     */
    class Builder(initialLocationEnabled: Boolean, setupMocks: Boolean = true) : BaseLocationManager.Builder {

        /**
         * Ths list of build [MockLocationManager]
         */
        val builtLocationManagers = IsoMutableList<MockLocationManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                val builtLocationManagers = builtLocationManagers
                createMock.on().doExecute { (locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo) ->
                    MockLocationManager(initialLocationEnabled, locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo).also {
                        builtLocationManagers.add(it)
                    }
                }
            }
        }

        override fun create(
            locationPermission: LocationPermission,
            permissions: Permissions,
            autoRequestPermission: Boolean,
            autoEnableLocations: Boolean,
            locationStateRepo: LocationStateRepo
        ): BaseLocationManager = createMock.call(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo)
    }

    /**
     * Sets whether location is enabled
     */
    val locationEnabled = MutableStateFlow(initialLocationEnabled)

    override val locationMonitor = MockLocationMonitor(locationEnabled)

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringPermissions]
     */
    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringPermissions]
     */
    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringLocationEnabled]
     */
    val startMonitoringLocationEnabledMock = ::startMonitoringLocationEnabled.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringLocationEnabled]
     */
    val stopMonitoringLocationEnabledMock = ::stopMonitoringLocationEnabled.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [requestLocationEnable]
     */
    val requestLocationEnableMock = ::requestLocationEnable.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringLocation]
     */
    val startMonitoringLocationMock = ::startMonitoringLocation.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringLocation]
     */
    val stopMonitoringLocationMock = ::stopMonitoringLocation.mock()

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsMock.call()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsMock.call()
    }

    override suspend fun startMonitoringLocationEnabled() {
        super.startMonitoringLocationEnabled()
        startMonitoringLocationEnabledMock.call()
    }

    override fun stopMonitoringLocationEnabled() {
        super.stopMonitoringLocationEnabled()
        stopMonitoringLocationEnabledMock.call()
    }

    override suspend fun requestLocationEnable() {
        requestLocationEnableMock.call()
    }

    override suspend fun startMonitoringLocation() {
        startMonitoringLocationMock.call()
    }

    override suspend fun stopMonitoringLocation() {
        stopMonitoringLocationMock.call()
    }
}
