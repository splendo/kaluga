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
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.test.BaseFlowTest
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import com.splendo.kaluga.test.yieldMultiple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext
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
        val initialPermissionState: PermissionState.Known<LocationPermission>,
        val locationEnabled: Boolean
    )

    class Context(configuration: Configuration, coroutineScope: CoroutineScope) : TestContext {
        val permissionsBuilder: MockPermissionsBuilder = MockPermissionsBuilder().apply {
            registerAllPermissionsBuilders()
        }
        val permissions = Permissions(
            permissionsBuilder,
            coroutineContext = coroutineScope.coroutineContext
        ).apply {
            // Make sure permissionState has been created as it may break the tests otherwise
            get(configuration.locationPermission)
        }
        val locationStateRepoBuilder = MockLocationStateRepoBuilder(permissions)

        val locationStateRepo = locationStateRepoBuilder.create(
            configuration.locationPermission,
            configuration.autoRequestPermission,
            configuration.autoEnableLocations,
            coroutineScope.coroutineContext
        )
        val locationManager = locationStateRepoBuilder.locationManager.apply {
            locationEnabled.value = configuration.locationEnabled
        }
        val permissionManager = permissionsBuilder.locationPMManager!!.apply {
            currentState = configuration.initialPermissionState
        }
    }

    override val createTestContextWithConfiguration: suspend (configuration: Configuration, scope: CoroutineScope) -> Context = { configuration, scope -> Context(configuration, scope) }
    override val flowFromTestContext: suspend Context.() -> LocationStateRepo = { locationStateRepo }

    override val filter: (Flow<LocationState>) -> Flow<LocationState> = { it.filterOnlyImportant() }

    @Test
    fun testStartEnabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            locationManager.startMonitoringPermissionsCompleted.await()
            locationManager.startMonitoringLocationEnabledCompleted.complete()
            locationManager.startMonitoringLocationCompleted.complete()
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
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
            permissionManager.stopMonitoringMock.verify(2)
        }
    }

    @Test
    fun testStartNoPermission() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Denied.Requestable(),
        false
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            locationManager.startMonitoringPermissionsCompleted.await()
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
            permissionManager.stopMonitoringMock.verify(2)
            locationManager.stopMonitoringPermissionsCompleted.await()
        }
    }

    @Test
    fun testStartNoPermissionAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        PermissionState.Denied.Requestable(),
        false
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted
            )
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
            awaitAll(
                locationManager.startMonitoringLocationEnabledCompleted,
                locationManager.startMonitoringLocationCompleted
            )
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testStartNoPermissionAutoRequestNoGPS() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        PermissionState.Denied.Requestable(),
        false
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted
            )
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
            locationManager.startMonitoringLocationEnabledCompleted.await()
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testStartNoGPS() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        false
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            locationManager.startMonitoringPermissionsCompleted.await()
            locationManager.startMonitoringLocationEnabledCompleted.await()
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testStartNoGPSAutoRequest() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        PermissionState.Allowed(),
        false
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted,
                locationManager.startMonitoringLocationEnabledCompleted
            )
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
            locationManager.startMonitoringLocationCompleted.await()
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testLocationChanged() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testMultipleLocationChanged() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testPermissionRevoked() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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

            awaitAll(
                locationManager.stopMonitoringLocationEnabledCompleted,
                locationManager.stopMonitoringLocationCompleted
            )
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
            permissionManager.stopMonitoringMock.verify(2)
            locationManager.stopMonitoringPermissionsCompleted.await()
        }
    }

    @Test
    fun testGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = true,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            awaitAll(
                locationManager.requestLocationEnableCompleted,
                locationManager.stopMonitoringLocationCompleted
            )
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    @Test
    fun testResumeFlow() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }

        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 4)
            permissionManager.stopMonitoringMock.verify(3)
            assertIs<LocationState.Enabled>(it)
            assertEquals(location1, it.location)
        }

        action {
            resetFlow()
        }

        mainAction {
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
            permissionManager.stopMonitoringMock.verify(4)
        }
    }

    @Test
    fun testResumeFlowPermissionDenied() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )

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
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted
            )
        }
    }

    @Test
    fun testResumeFlowGPSDisabled() = testLocationState(
        LocationPermission(background = false, precise = false),
        autoRequestPermission = false,
        autoEnableLocations = false,
        PermissionState.Allowed(),
        true
    ) {
        test {
            yieldMultiple(6)
            permissionManager.startMonitoringMock.verify(eq(PermissionStateRepo.defaultMonitoringInterval), 2)
            permissionManager.stopMonitoringMock.verify(1)
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
            permissionManager.stopMonitoringMock.verify(2)
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )

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
            awaitAll(
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
            )
        }
    }

    private fun testLocationState(
        locationPermission: LocationPermission,
        autoRequestPermission: Boolean,
        autoEnableLocations: Boolean,
        initialPermissionState: PermissionState.Known<LocationPermission>,
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

class MockLocationStateRepoBuilder(private val permissions: Permissions) :
    LocationStateRepo.Builder {

    lateinit var locationManager: MockLocationManager

    override fun create(
        locationPermission: LocationPermission,
        autoRequestPermission: Boolean,
        autoEnableLocations: Boolean,
        coroutineContext: CoroutineContext
    ): LocationStateRepo {
        return LocationStateRepo(
            locationPermission,
            permissions,
            autoRequestPermission,
            autoEnableLocations,
            object : BaseLocationManager.Builder {

                override fun create(
                    locationPermission: LocationPermission,
                    permissions: Permissions,
                    autoRequestPermission: Boolean,
                    autoEnableLocations: Boolean,
                    locationStateRepo: LocationStateRepo
                ): BaseLocationManager {
                    locationManager = MockLocationManager(
                        locationPermission,
                        permissions,
                        autoRequestPermission,
                        autoEnableLocations,
                        locationStateRepo
                    )
                    return locationManager
                }
            },
            coroutineContext
        )
    }
}

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
