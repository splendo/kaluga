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

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.base.BaseFlowTest
import com.splendo.kaluga.test.base.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.location.MockBaseLocationManager
import com.splendo.kaluga.test.location.MockLocationStateRepoBuilder
import com.splendo.kaluga.test.permissions.MockPermissionState
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LocationStateTest :
    BaseFlowTest<LocationStateTest.Configuration, LocationStateTest.Context, LocationState, LocationStateRepo>() {

    companion object {
        private val location1 = Location.KnownLocation(
            latitude = 52.15,
            longitude = 4.4303,
            time = DefaultKalugaDate.now(),
            horizontalAccuracy = 1.0,
            verticalAccuracy = 1.0,
            altitude = 1.0,
            speed = 1.0,
            course = 1.0,
        )
        private val location2 = Location.KnownLocation(
            latitude = 52.079,
            longitude = 4.3413,
            time = DefaultKalugaDate.now(),
            horizontalAccuracy = 2.0,
            verticalAccuracy = 2.0,
            altitude = 2.0,
            speed = 2.0,
            course = 2.0,
        )
    }

    data class Configuration(
        val locationPermission: LocationPermission,
        val autoRequestPermission: Boolean,
        val autoEnableLocations: Boolean,
        val initialPermissionState: MockPermissionState.ActiveState,
        val locationEnabled: Boolean,
    )

    class Context(private val configuration: Configuration, coroutineScope: CoroutineScope) :
        TestContext {
            val permissionsBuilder: MockPermissionsBuilder = MockPermissionsBuilder(
                initialActiveState = configuration.initialPermissionState,
            )

            val locationStateRepoBuilder = MockLocationStateRepoBuilder(
                {
                    permissionsBuilder.registerAllPermissionsBuilders()
                    Permissions(
                        permissionsBuilder,
                        coroutineContext = coroutineScope.coroutineContext,
                    ).apply {
                        // Make sure permissionState has been created as it may break the tests otherwise
                        get(configuration.locationPermission)
                    }
                },
                MockBaseLocationManager.Builder(configuration.locationEnabled),
            )

            private fun settingsBuilder(
                autoRequestPermission: Boolean,
                autoEnableLocations: Boolean,
            ): (LocationPermission, Permissions) -> BaseLocationManager.Settings = { locationPermission, permissions ->
                BaseLocationManager.Settings(locationPermission, permissions, autoRequestPermission = autoRequestPermission, autoEnableLocations = autoEnableLocations)
            }

            val locationStateRepo = locationStateRepoBuilder.create(
                configuration.locationPermission,
                settingsBuilder(configuration.autoRequestPermission, configuration.autoEnableLocations),
                coroutineContext = coroutineScope.coroutineContext,
            )
            val locationManager get() = locationStateRepoBuilder.locationManagerBuilder.builtLocationManagers.first()
            val permissionStateRepo get() = permissionsBuilder.buildLocationStateRepos.first()
        }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration, scope: CoroutineScope) -> Context =
        { configuration, scope -> Context(configuration, scope) }
    override val flowFromTestContext: suspend Context.() -> LocationStateRepo =
        { locationStateRepo }

    override val filter: (Flow<LocationState>) -> Flow<LocationState> = { it.filterOnlyImportant() }

    @Test
    fun testStartEnabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoPermission() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.REQUESTABLE,
        false,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify(rule = never())
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
        }
    }

    @Test
    fun testStartNoPermissionAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.REQUESTABLE,
        false,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify(rule = never())
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location,
            )
        }
        mainAction {
            locationManager.locationEnabled.value = true
            permissionStateRepo.takeAndChangeState { state ->
                @Suppress("UNCHECKED_CAST")
                (state as MockPermissionState<LocationPermission>).allow
            }
        }
        test {
            locationManager.startMonitoringLocationEnabledMock.verify()
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoPermissionAutoRequestNoGPS() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.REQUESTABLE,
        false,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location,
            )
        }
        mainAction {
            permissionStateRepo.takeAndChangeState { state ->
                @Suppress("UNCHECKED_CAST")
                (state as MockPermissionState<LocationPermission>).allow
            }
        }
        test {
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoGPS() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        false,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoGPSAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        MockPermissionState.ActiveState.ALLOWED,
        false,
    ) {
        test {
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location,
            )
        }
        mainAction {
            locationManager.locationEnabled.value = true
        }
        test {
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testLocationChanged() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }
        mainAction {
            locationManager.handleLocationChanged(location2)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location2, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testMultipleLocationChanged() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(listOf(location1, location2))
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location2, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testPermissionRevoked() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }
        mainAction {
            permissionStateRepo.takeAndChangeState { state ->
                @Suppress("UNCHECKED_CAST")
                (state as MockPermissionState<LocationPermission>).lock
            }
            yieldMultiple(3)
        }
        test {
            locationManager.stopMonitoringLocationEnabledMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.PERMISSION_DENIED,
                ),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
        }
    }

    @Test
    fun testGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }
        mainAction {
            locationManager.locationEnabled.value = false
            yieldMultiple(3)
        }
        test {
            locationManager.requestEnableLocationMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.NO_GPS,
                ),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testResumeFlow() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }

        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify(times = 2)
            locationManager.stopMonitoringLocationMock.verify(times = 2)
            locationManager.stopMonitoringLocationEnabledMock.verify(times = 2)
        }
    }

    @Test
    fun testResumeFlowPermissionDenied() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()

            permissionStateRepo.takeAndChangeState { state ->
                @Suppress("UNCHECKED_CAST")
                (state as MockPermissionState<LocationPermission>).lock
            }
        }

        test {
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.PERMISSION_DENIED,
                ),
                it.location,
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify(times = 2)
        }
    }

    @Test
    fun testResumeFlowGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionState.ActiveState.ALLOWED,
        true,
    ) {
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location,
            )
        }
        mainAction {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            yieldMultiple(10)
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()

            locationManager.locationEnabled.value = false
        }

        test {
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.NO_GPS,
                ),
                it.location,
            )
        }
        mainAction {
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    private fun testLocationState(
        locationPermission: LocationPermission,
        autoRequestPermission: Boolean,
        autoEnableLocations: Boolean,
        initialPermissionState: MockPermissionState.ActiveState,
        locationEnabled: Boolean,
        test: suspend BaseFlowTest<Configuration, Context, LocationState, LocationStateRepo>.(LocationStateRepo) -> Unit,
    ) {
        testWithFlowAndTestContext(
            Configuration(
                locationPermission,
                autoRequestPermission,
                autoEnableLocations,
                initialPermissionState,
                locationEnabled,
            ),
            blockWithContext = test,
        )
    }
}
