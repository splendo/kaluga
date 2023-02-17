/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toCollection
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class KnownLocationTest {

    @Test
    fun testKnownLocation() = runBlocking {
        val now = DefaultKalugaDate.now()
        val knownLocation = Location.KnownLocation(1.0, 1.0, time = now)
        val locationFlow = flowOf(
            Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR),
            knownLocation,
            Location.UnknownLocation.WithLastLocation(knownLocation, Location.UnknownLocation.Reason.NOT_CLEAR)
        ).known()
        val collectedLocations = mutableListOf<Location.KnownLocation?>()
        locationFlow.toCollection(collectedLocations)
        assertEquals(3, collectedLocations.size)
        assertNull(collectedLocations[0])
        assertEquals(knownLocation, collectedLocations[1])
        assertEquals(knownLocation, collectedLocations[2])
    }

    @Test
    fun testExpiredKnownLocation() = runBlocking {
        val now = DefaultKalugaDate.epoch(10.seconds)
        val knownLocation = Location.KnownLocation(1.0, 1.0, time = now)
        val locationFlow = flowOf(
            knownLocation.copy(time = DefaultKalugaDate.epoch()),
            knownLocation
        ).known(maxAge = 5.seconds) { now }
        val collectedLocations = mutableListOf<Location.KnownLocation?>()
        locationFlow.toCollection(collectedLocations)
        assertEquals(3, collectedLocations.size)
        assertNull(collectedLocations[0])
        assertEquals(knownLocation, collectedLocations[1])
        assertNull(collectedLocations[2])
    }
}
