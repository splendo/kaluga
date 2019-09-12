package com.splendo.mpp.location

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.splendo.mpp.location.test.LocationFlowableTest
import com.splendo.mpp.runBlocking
import com.splendo.mpp.util.debug
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Rule
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockedLocationFlowableTest:LocationFlowableTest() {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    lateinit var client:FusedLocationProviderClient

    // we want to set an initial location, but not have it be included in the test. So we filter it from the test.
    var allowInFilter = false
    override val filter: suspend (Location) -> Boolean = {
        debug("allow $allowInFilter for $it")
        allowInFilter }

    override fun assertSameLocation(expected: Location, actual: Location) {
        if (expected is Location.KnownLocation && actual is Location.KnownLocation) {
            assertEquals(expected.longitude, actual.longitude)
            assertEquals(expected.latitude, actual.latitude)
            // ignore other attributes since they can get mangled by FusedLocationProvider mocking
        }
        else if (expected is Location.UnknownLocation) {
            // we have no control over the previous location being known, so accept any unknown location
            assertTrue(actual is Location.UnknownLocation)
        } else {
            super.assertSameLocation(expected, actual)
        }
    }
    private fun filterCheck() {
        if (!allowInFilter) {
            debug("not allowing locations, wait 0,5s")
            Thread.sleep(500) // filter out initial locations
            allowInFilter = true
            debug("now allowing locations")
        }
    }

    override suspend fun setLocation(location: Location.KnownLocation) {
        filterCheck()
        debug("Mock location $location")

        setFusedLocation(location.latitude, location.longitude)
    }

    private suspend fun setFusedLocation(latitude:Double, longitude:Double) {
        val fusedLocation = android.location.Location("mock")
        fusedLocation.latitude = latitude
        fusedLocation.longitude = longitude
        fusedLocation.accuracy = 1f
        fusedLocation.time = System.currentTimeMillis()
        fusedLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        client.setMockLocation(fusedLocation).await()
        client.flushLocations().await()
    }

    override suspend fun setLocationUnknown(reason: Location.UnknownReason) {
        filterCheck()
        // mocking play services location framework on android only supports known locations
         super.setLocationUnknown(reason)
    }

    @Before
    fun mockLocation() {
         client = LocationServices.getFusedLocationProviderClient(
            ApplicationProvider.getApplicationContext() as Context
         )

        runBlocking {
            client.setMockMode(true).await()
            setFusedLocation(0.0,0.0)
        }

        flowable.setFusedLocationProviderClient(client)
    }
}