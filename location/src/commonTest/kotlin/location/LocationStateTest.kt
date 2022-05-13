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
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.BaseFlowTest
import com.splendo.kaluga.test.location.MockLocationManager
import com.splendo.kaluga.test.location.MockLocationStateRepoBuilder
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.MockPermissionManager
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
            time = Location.Time.MeasuredTime(1000),
            horizontalAccuracy = 1.0,
            verticalAccuracy = 1.0,
            altitude = 1.0,
            speed = 1.0,
            course = 1.0
        )
        private val location2 = Location.KnownLocation(
            latitude = 52.079,
            longitude = 4.3413,
            time = Location.Time.MeasuredTime(1000),
            horizontalAccuracy = 2.0,
            verticalAccuracy = 2.0,
            altitude = 2.0,
            speed = 2.0,
            course = 2.0
        )
    }

    data class Configuration(
        val locationPermission: LocationPermission,
        val autoRequestPermission: Boolean,
        val autoEnableLocations: Boolean,
        val initialPermissionState: MockPermissionManager.MockPermissionState,
        val locationEnabled: Boolean
    )

    class Context(private val configuration: Configuration, coroutineScope: CoroutineScope) :
        TestContext {
        val permissionsBuilder: MockPermissionsBuilder = MockPermissionsBuilder(
            initialPermissionState = configuration.initialPermissionState
        ).apply {
            registerAllPermissionsBuilders()
        }
        val permissions = Permissions(
            permissionsBuilder,
            coroutineContext = coroutineScope.coroutineContext
        ).apply {
            // Make sure permissionState has been created as it may break the tests otherwise
            get(configuration.locationPermission)
        }
        val locationStateRepoBuilder = MockLocationStateRepoBuilder(
            permissions,
            MockLocationManager.Builder(configuration.locationEnabled)
        )

        val locationStateRepo = locationStateRepoBuilder.create(
            configuration.locationPermission,
            configuration.autoRequestPermission,
            configuration.autoEnableLocations,
            coroutineScope.coroutineContext
        )
        val locationManager get() = locationStateRepoBuilder.locationManagerBuilder.builtLocationManagers.first()
        val permissionManager get() = permissionsBuilder.createdLocationPermissionManagers.first()
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
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
            permissionManager.stopMonitoringMock.verify()
        }
    }

    @Test
    fun testStartNoPermission() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.DENIED,
        false
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
        }
    }

    @Test
    fun testStartNoPermissionAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.DENIED,
        false
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location
            )
        }
        mainAction {
            locationManager.locationEnabled.value = true
            permissionManager.setPermissionAllowed()
        }
        test {
            locationManager.startMonitoringLocationEnabledMock.verify()
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
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
        MockPermissionManager.MockPermissionState.DENIED,
        false
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED),
                it.location
            )
        }
        mainAction {
            permissionManager.setPermissionAllowed()
        }
        test {
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoGPS() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.ALLOWED,
        false
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testStartNoGPSAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        MockPermissionManager.MockPermissionState.ALLOWED,
        false
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            locationManager.startMonitoringPermissionsMock.verify()
            locationManager.startMonitoringLocationEnabledMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location
            )
        }
        mainAction {
            locationManager.locationEnabled.value = true
        }
        test {
            locationManager.startMonitoringLocationMock.verify()
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS),
                it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
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
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.stopMonitoringMock.verify()
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
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.stopMonitoringMock.verify()
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
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.setPermissionDenied()
        }
        test {
            locationManager.stopMonitoringLocationEnabledMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.PERMISSION_DENIED
                ), it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
        }
    }

    @Test
    fun testGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
        }
        test {
            locationManager.requestLocationEnableMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            assertIs<LocationState.Disabled.NoGPS>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.NO_GPS
                ), it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }
    }

    @Test
    fun testResumeFlow() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()
        }

        test {
            permissionManager.startMonitoringMock.verify(
                eq(PermissionStateRepo.defaultMonitoringInterval),
                2
            )
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            locationManager.stopMonitoringPermissionsMock.verify(2)
            locationManager.stopMonitoringLocationMock.verify(2)
            locationManager.stopMonitoringLocationEnabledMock.verify(2)
            permissionManager.stopMonitoringMock.verify(2)
        }
    }

    @Test
    fun testResumeFlowPermissionDenied() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.stopMonitoringMock.verify()
            locationManager.stopMonitoringPermissionsMock.verify()
            locationManager.stopMonitoringLocationMock.verify()
            locationManager.stopMonitoringLocationEnabledMock.verify()

            permissionManager.setPermissionLocked()
        }

        test {
            assertIs<LocationState.Disabled.NotPermitted>(it)
            assertEquals(
                Location.UnknownLocation.WithLastLocation(
                    location1,
                    Location.UnknownLocation.Reason.PERMISSION_DENIED
                ), it.location
            )
        }

        action {
            resetFlow()
        }

        mainAction {
            locationManager.stopMonitoringPermissionsMock.verify(2)
        }
    }

    @Test
    fun testResumeFlowGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        MockPermissionManager.MockPermissionState.ALLOWED,
        true
    ) {
        mainAction {
            permissionsBuilder.createLocationPermissionManagerMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval))
            assertIs<LocationState.Enabled>(it)
            assertEquals(
                Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
                it.location
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
            permissionManager.stopMonitoringMock.verify()
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
                    Location.UnknownLocation.Reason.NO_GPS
                ), it.location
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
        initialPermissionState: MockPermissionManager.MockPermissionState,
        locationEnabled: Boolean,
        test: suspend BaseFlowTest<Configuration, Context, LocationState, LocationStateRepo>.(LocationStateRepo) -> Unit
    ) {

        testWithFlowAndTestContext(
            Configuration(
                locationPermission,
                autoRequestPermission,
                autoEnableLocations,
                initialPermissionState,
                locationEnabled
            ), blockWithContext = test
        )
    }
}
