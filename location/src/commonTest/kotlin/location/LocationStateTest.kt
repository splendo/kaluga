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

import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.flow.Flowable
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.test.FlowTestBlock
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitAll

class LocationStateTest : FlowableTest<LocationState>() {

    companion object {
        private val location1 = Location.KnownLocation(
            latitude = 52.15,
            longitude = 4.4303,
            time = Location.Time.MeasuredTime(1000),
            horizontalAccuracy = 1.0,
            verticalAccuracy = 1.0,
            altitude = 1.0,
            speed = 1.0,
            course = 1.0)
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

    private val testCoroutine = SupervisorJob()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + testCoroutine)

    private val permissionsBuilder: MockPermissionsBuilder = MockPermissionsBuilder()
    private val permissions = Permissions(permissionsBuilder, coroutineContext = coroutineScope.coroutineContext)
    private val locationStateRepoBuilder = MockLocationStateRepoBuilder(permissions)

    private lateinit var permissionManager: MockPermissionManager<Permission.Location>
    private lateinit var locationManager: MockLocationManager

    lateinit var locationStateRepo:LocationStateRepo

    @AfterTest
    override fun afterTest() {
        super.afterTest()
        permissions.clean()
    }

    @Test
    fun testStartEnabled() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            locationManager.startMonitoringPermissionsCompleted.await()
            locationManager.startMonitoringLocationEnabledCompleted.complete()
            locationManager.startMonitoringLocationCompleted.complete()
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }

        resetFlow()

        awaitAll(
                permissionManager.hasStoppedMonitoring,
                locationManager.stopMonitoringPermissionsCompleted,
                locationManager.stopMonitoringLocationCompleted,
                locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testStartNoPermission() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Denied.Requestable()
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            locationManager.startMonitoringPermissionsCompleted.await()
            assertTrue(it is LocationState.Disabled.NotPermitted)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted
        )
    }

    @Test
    fun testStartNoPermissionAutoRequest() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Denied.Requestable()
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted,
                permissionManager.hasRequestedPermission
            )
            assertTrue(it is LocationState.Disabled.NotPermitted)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }
        action {
            locationManager.locationEnabled = true
            permissionManager.setPermissionAllowed()
        }
        test {
            awaitAll(
                locationManager.startMonitoringLocationEnabledCompleted,
                locationManager.startMonitoringLocationCompleted
            )
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testStartNoPermissionAutoRequestNoGPS() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Denied.Requestable()
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted,
                permissionManager.hasRequestedPermission
            )
            assertTrue(it is LocationState.Disabled.NotPermitted)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }
        action {
            permissionManager.setPermissionAllowed()
        }
        test {
            locationManager.startMonitoringLocationEnabledCompleted.await()
            assertTrue(it is LocationState.Disabled.NoGPS)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS), it.location)
        }

        resetFlow()

        awaitAll (
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testStartNoGPS() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = false
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            locationManager.startMonitoringPermissionsCompleted.await()
            locationManager.startMonitoringLocationEnabledCompleted.await()
            assertTrue(it is LocationState.Disabled.NoGPS)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testStartNoGPSAutoRequest() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = true) {
        assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = false
        test {
            assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
            awaitAll(
                locationManager.startMonitoringPermissionsCompleted,
                locationManager.startMonitoringLocationEnabledCompleted
            )
            assertTrue(it is LocationState.Disabled.NoGPS)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS), it.location)
        }
        action {
            locationManager.locationEnabled = true
            locationManager.handleLocationEnabledChanged()
        }
        test {
            locationManager.startMonitoringLocationCompleted.await()
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testLocationChanged() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        action {
            locationManager.handleLocationChanged(location2)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location2, it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted)
    }

    @Test
    fun testMultipleLocationChanged() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(listOf(location1, location2))
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location2, it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted)
    }

    @Test
    fun testPermissionRevoked() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        action {
            permissionManager.setPermissionDenied()
        }
        test {

            awaitAll(
                permissionManager.hasRequestedPermission,
                locationManager.stopMonitoringLocationEnabledCompleted,
                locationManager.stopMonitoringLocationCompleted
            )
            assertTrue(it is LocationState.Disabled.NotPermitted)
            assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted
        )
    }

    @Test
    fun testGPSDisabled() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = true) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        action {
            locationManager.locationEnabled = false
            locationManager.handleLocationEnabledChanged()
        }
        test {
            awaitAll(
                locationManager.requestLocationEnableCompleted,
                locationManager.stopMonitoringLocationCompleted
            )
            assertTrue(it is LocationState.Disabled.NoGPS)
            assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.NO_GPS), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testResumeFlow() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )

        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        
        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    @Test
    fun testResumeFlowPermissionDenied() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true

        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }
        
        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )

        permissionManager.currentState = PermissionState.Denied.Locked()

        test {
            assertTrue(it is LocationState.Disabled.NotPermitted)
            assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted
        )
    }

    @Test
    fun testResumeFlowGPSDisabled() = testLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false) {
        permissionManager.currentState = PermissionState.Allowed()
        locationManager.locationEnabled = true

        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
        }
        action {
            locationManager.handleLocationChanged(location1)
        }
        test {
            assertTrue(it is LocationState.Enabled)
            assertEquals(location1, it.location)
        }

        resetFlow()

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )

        locationManager.locationEnabled = false

        test {
            assertTrue(it is LocationState.Disabled.NoGPS)
            assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.NO_GPS), it.location)
        }

        awaitAll(
            permissionManager.hasStoppedMonitoring,
            locationManager.stopMonitoringPermissionsCompleted,
            locationManager.stopMonitoringLocationEnabledCompleted
        )
    }

    private fun testLocationState(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean, test: FlowTestBlock<LocationState>) {

        // Make sure permissionState has been created as it may break the tests otherwise
        permissions[locationPermission]

        locationStateRepo = locationStateRepoBuilder.create(locationPermission, autoRequestPermission, autoEnableLocations, coroutineScope.coroutineContext)
        locationManager = locationStateRepoBuilder.locationManager
        permissionManager = permissionsBuilder.locationPMManager
        
        testWithFlow(test)
    }

    override fun flowable(): Flowable<LocationState> {
        return locationStateRepo.flowable
    }
}

class MockLocationStateRepoBuilder(private val permissions: Permissions) : LocationStateRepo.Builder {

    lateinit var locationManager: MockLocationManager

    override fun create(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean, coroutineContext: CoroutineContext): LocationStateRepo {
        return LocationStateRepo(locationPermission, permissions, autoRequestPermission, autoEnableLocations, object : BaseLocationManager.Builder {

            override fun create(
                locationPermission: Permission.Location,
                permissions: Permissions,
                autoRequestPermission: Boolean,
                autoEnableLocations: Boolean,
                locationStateRepo: LocationStateRepo
            ): BaseLocationManager {
                locationManager = MockLocationManager(locationPermission, permissions, autoRequestPermission, autoEnableLocations, locationStateRepo)
                return locationManager
            }
        }, coroutineContext)
    }
}

class MockLocationManager(
    locationPermission: Permission.Location,
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

    val _locationEnabled = AtomicBoolean(false)
    var locationEnabled:Boolean
        get() = _locationEnabled.value
        set(it) { _locationEnabled.value = it }

    val startMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    val stopMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    val startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    val stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    val requestLocationEnableCompleted = EmptyCompletableDeferred()
    val startMonitoringLocationCompleted = EmptyCompletableDeferred()
    val stopMonitoringLocationCompleted = EmptyCompletableDeferred()

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        startMonitoringPermissionsCompleted.complete()
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        stopMonitoringPermissionsCompleted.complete()
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
