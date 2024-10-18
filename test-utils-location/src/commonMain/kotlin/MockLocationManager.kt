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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationManager
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class MockLocationManager(
    override val locationPermission: LocationPermission,
    override val events: MutableSharedFlow<LocationManager.Event>,
    override val locations: MutableSharedFlow<Location.KnownLocation>,
) : LocationManager {

    val isLocationEnabledMock = ::isLocationEnabled.mock()
    override fun isLocationEnabled(): Boolean = isLocationEnabledMock.call()

    val requestEnableLocationMock = ::requestEnableLocation.mock()
    override suspend fun requestEnableLocation(): Unit = requestEnableLocationMock.call()

    val startMonitoringLocationMock = ::startMonitoringLocation.mock()
    override suspend fun startMonitoringLocation(): Unit = startMonitoringLocationMock.call()
    val stopMonitoringLocationMock = ::stopMonitoringLocation.mock()
    override suspend fun stopMonitoringLocation(): Unit = stopMonitoringLocationMock.call()
    val startMonitoringLocationEnabledMock = ::startMonitoringLocationEnabled.mock()
    override suspend fun startMonitoringLocationEnabled(): Unit = startMonitoringLocationEnabledMock.call()
    val stopMonitoringLocationEnabledMock = ::stopMonitoringLocationEnabled.mock()
    override suspend fun stopMonitoringLocationEnabled(): Unit = stopMonitoringLocationEnabledMock.call()
    val startMonitoringPermissionsMock = ::startMonitoringPermissions.mock()
    override suspend fun startMonitoringPermissions(): Unit = startMonitoringPermissionsMock.call()
    val stopMonitoringPermissionsMock = ::stopMonitoringPermissions.mock()
    override suspend fun stopMonitoringPermissions(): Unit = stopMonitoringPermissionsMock.call()
}

/**
 * Mock implementation of [BaseLocationManager]
 * @param initialLocationEnabled Sets the initial state of location
 */
class MockBaseLocationManager(initialLocationEnabled: Boolean, settings: Settings, coroutineScope: CoroutineScope) :
    BaseLocationManager(
        settings,
        coroutineScope,
    ) {

    /**
     * Mock implementation of [BaseLocationManager.Builder]
     * @param initialLocationEnabled Sets the initial state of location
     * @param setupMocks If `true` sets up [createMock] to build [MockBaseLocationManager]
     */
    class Builder(val initialLocationEnabled: Boolean, setupMocks: Boolean = true) : BaseLocationManager.Builder {

        /**
         * Ths list of built [MockBaseLocationManager]
         */
        val builtLocationManagers = concurrentMutableListOf<MockBaseLocationManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                val builtLocationManagers = builtLocationManagers
                createMock.on().doExecute { (settings, coroutineScope) ->
                    MockBaseLocationManager(initialLocationEnabled, settings, coroutineScope).also {
                        builtLocationManagers.add(it)
                    }
                }
            }
        }

        override fun create(settings: Settings, coroutineScope: CoroutineScope): BaseLocationManager = createMock.call(settings, coroutineScope)
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
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [requestEnableLocation]
     */
    val requestEnableLocationMock = ::requestEnableLocation.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [startMonitoringLocation]
     */
    val startMonitoringLocationMock = ::startMonitoringLocation.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [stopMonitoringLocation]
     */
    val stopMonitoringLocationMock = ::stopMonitoringLocation.mock()

    override suspend fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsMock.call()
    }

    override suspend fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsMock.call()
    }

    override suspend fun startMonitoringLocationEnabled() {
        super.startMonitoringLocationEnabled()
        startMonitoringLocationEnabledMock.call()
    }

    override suspend fun stopMonitoringLocationEnabled() {
        super.stopMonitoringLocationEnabled()
        stopMonitoringLocationEnabledMock.call()
    }

    override suspend fun requestEnableLocation() {
        requestEnableLocationMock.call()
    }

    override suspend fun startMonitoringLocation() {
        startMonitoringLocationMock.call()
    }

    override suspend fun stopMonitoringLocation() {
        stopMonitoringLocationMock.call()
    }

    public override fun handleLocationChanged(location: Location.KnownLocation) {
        super.handleLocationChanged(location)
    }

    public override fun handleLocationChanged(locations: List<Location.KnownLocation>) {
        super.handleLocationChanged(locations)
    }
}
