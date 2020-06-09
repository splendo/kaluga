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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete
import kotlinx.coroutines.launch
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    private val permissionsBuilder = MockPermissionsBuilder()
    private val permissions = Permissions(permissionsBuilder)
    private val locationStateRepoBuilder = MockLocationStateRepoBuilder(permissions)
    lateinit var locationStateRepo: LocationStateRepo

    private val permissionManager: MockPermissionManager<Permission.Location>
        get() {
        return permissionsBuilder.locationPMManager
    }
    private val locationManager: MockLocationManager get() {
        return locationStateRepoBuilder.locationManager
    }

    @BeforeTest
    fun setup() {
        super.beforeTest()
    }

    @AfterTest
    fun tearDown() {
        super.afterTest()
    }

    @Test
    fun testStartEnabled() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        permissionManager.reset()
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                locationManager.startMonitoringLocationEnabledCompleted.complete()
                locationManager.startMonitoringLocationCompleted.complete()
                assertTrue(it is LocationState.Enabled)
                assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
    }

    @Test
    fun testStartNoPermission() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                assertTrue(it is LocationState.Disabled.NotPermitted)
                assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
    }

    @Test
    fun testStartNoPermissionAutoRequest() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                permissionManager.hasRequestedPermission.await()
                assertTrue(it is LocationState.Disabled.NotPermitted)
                assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
            }
            action {
                locationManager.locationEnabled = true
                permissionManager.setPermissionAllowed()
            }
            test {
                locationManager.startMonitoringLocationEnabledCompleted.await()
                locationManager.startMonitoringLocationCompleted.await()
                assertTrue(it is LocationState.Enabled)
                assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
    }

    @Test
    fun testStartNoPermissionAutoRequestNoGPS() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Denied.Requestable(permissionManager)
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                permissionManager.hasRequestedPermission.await()
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testStartNoGPS() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = false
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                locationManager.startMonitoringLocationEnabledCompleted.await()
                assertTrue(it is LocationState.Disabled.NoGPS)
                assertEquals(Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NO_GPS), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testStartNoGPSAutoRequest() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = true)
        assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
        assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = false
        testWithFlow {
            assertFalse(permissionManager.hasStartedMonitoring.isCompleted)
            assertFalse(permissionManager.hasStoppedMonitoring.isCompleted)
            test {
                assertEquals(PermissionStateRepo.defaultMonitoringInterval, permissionManager.hasStartedMonitoring.getCompleted())
                locationManager.startMonitoringPermissionsCompleted.await()
                locationManager.startMonitoringLocationEnabledCompleted.await()
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testLocationChanged() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        testWithFlow {
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testMultipleLocationChanged() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        testWithFlow {
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testPermissionRevoked() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = true, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        testWithFlow {
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
                permissionManager.hasRequestedPermission.await()
                locationManager.stopMonitoringLocationEnabledCompleted.await()
                locationManager.stopMonitoringLocationCompleted.await()
                assertTrue(it is LocationState.Disabled.NotPermitted)
                assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
    }

    @Test
    fun testGPSDisabled() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = true)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        testWithFlow {
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
                locationManager.requestLocationEnableCompleted.await()
                locationManager.stopMonitoringLocationCompleted.await()
                assertTrue(it is LocationState.Disabled.NoGPS)
                assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.NO_GPS), it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testResumeFlow() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        FlowTest(locationStateRepo.flowable).testWithFlow {
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
        permissionManager.reset()
        locationManager.reset()
        FlowTest(locationStateRepo.flowable).testWithFlow {
            test {
                assertTrue(it is LocationState.Enabled)
                assertEquals(location1, it.location)
            }
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    @Test
    fun testResumeFlowPermissionDenied() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
        FlowTest(locationStateRepo.flowable).testWithFlow {
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
        }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()

        permissionManager.reset()
        locationManager.reset()
        permissionManager.currentState = PermissionState.Denied.Locked(permissionManager)
        FlowTest(locationStateRepo.flowable).testWithFlow {
            test {
                assertTrue(it is LocationState.Disabled.NotPermitted)
                assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.PERMISSION_DENIED), it.location)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
    }

    @Test
    fun testResumeFlowGPSDisabled() = runBlocking {
        setupLocationState(Permission.Location(background = false, precise = false), autoRequestPermission = false, autoEnableLocations = false)
        permissionManager.currentState = PermissionState.Allowed(permissionManager)
        locationManager.locationEnabled = true
            FlowTest(locationStateRepo.flowable).testWithFlow {
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
            }
        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()

        permissionManager.reset()
        locationManager.reset()
        locationManager.locationEnabled = false
        FlowTest(locationStateRepo.flowable).testWithFlow {
            test {
                assertTrue(it is LocationState.Disabled.NoGPS)
                assertEquals(Location.UnknownLocation.WithLastLocation(location1, Location.UnknownLocation.Reason.NO_GPS), it.location)
            }
        }

        permissionManager.hasStoppedMonitoring.await()
        locationManager.stopMonitoringPermissionsCompleted.await()
        locationManager.stopMonitoringLocationEnabledCompleted.await()
    }

    private fun setupLocationState(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean) {
        locationStateRepo = locationStateRepoBuilder.create(locationPermission, autoRequestPermission, autoEnableLocations)
        flowable.complete(locationStateRepo.flowable)
        // Make sure permissionState has been created as it may break the tests otherwise
        permissions[locationPermission]
        locationManager.reset()
    }
}

class MockLocationStateRepoBuilder(private val permissions: Permissions) : LocationStateRepo.Builder {

    lateinit var locationManager: MockLocationManager

    override fun create(locationPermission: Permission.Location, autoRequestPermission: Boolean, autoEnableLocations: Boolean): LocationStateRepo {
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
        })
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

    var locationEnabled: Boolean = false

    var startMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    var stopMonitoringPermissionsCompleted = EmptyCompletableDeferred()
    var startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    var stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
    var requestLocationEnableCompleted = EmptyCompletableDeferred()
    var startMonitoringLocationCompleted = EmptyCompletableDeferred()
    var stopMonitoringLocationCompleted = EmptyCompletableDeferred()

    fun reset() {
        startMonitoringPermissionsCompleted = EmptyCompletableDeferred()
        stopMonitoringPermissionsCompleted = EmptyCompletableDeferred()
        startMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
        stopMonitoringLocationEnabledCompleted = EmptyCompletableDeferred()
        requestLocationEnableCompleted = EmptyCompletableDeferred()
        startMonitoringLocationCompleted = EmptyCompletableDeferred()
        stopMonitoringLocationCompleted = EmptyCompletableDeferred()
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
