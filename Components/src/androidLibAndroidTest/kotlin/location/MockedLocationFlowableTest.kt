package com.splendo.kaluga.location
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import android.content.Context
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.splendo.kaluga.location.test.LocationFlowableTest
import com.splendo.kaluga.runBlocking
import com.splendo.kaluga.log.debug
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