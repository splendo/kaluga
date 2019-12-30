package com.splendo.kaluga.location.test
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

import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.Location.*
import com.splendo.kaluga.location.LocationFlowable
import com.splendo.kaluga.test.FlowableTest
import com.splendo.kaluga.log.debug
import kotlin.test.*

open class LocationFlowableTest : FlowableTest<Location>() {

    override val flowable: LocationFlowable = LocationFlowable()

    private val location1 = KnownLocation(latitude = 52.15, longitude = 4.4303, time = Time.MeasuredTime(1000), horizontalAccuracy = 1.0, verticalAccuracy = 1.0, altitude = 1.0, speed = 1.0, course = 1.0)
    private val location2 = KnownLocation(latitude = 52.079, longitude = 4.3413, time = Time.MeasuredTime(1000), horizontalAccuracy = 2.0, verticalAccuracy = 2.0, altitude = 2.0, speed = 2.0, course = 2.0)

    open suspend fun setLocationUnknown(reason:UnknownReason = UnknownReason.NOT_CLEAR) {
        flowable.setUnknownLocation(reason)
    }

    open suspend fun setLocation(location:KnownLocation) {
        debug("Send location directly to channel for test: $location")
        flowable.set(location)
    }

    open fun assertSameLocation(expected:Location, actual:Location) {
        assertEquals(expected, actual)
    }


    @Test
    fun testKnownLocation() = runBlockingWithFlow {
        action {
            setLocation(location1)
            debug("sent location2")
        }
        test {
            assertSameLocation(location1, it )
        }
    }

    @Test
    fun testSingleUnknownLocation() = runBlockingWithFlow {
        action {
            setLocationUnknown()
            debug("sent location")
        }
        test {
            debug("unknown? : $it")
            assertSameLocation(UnknownLocationWithNoLastLocation(reason = UnknownReason.NOT_CLEAR), it )
        }
    }

    @Test
    fun testSingleUnknownLocationWithReason()  = runBlockingWithFlow {
        action {
            setLocationUnknown(reason = UnknownReason.PERMISSION_DENIED)
            debug("sent location unknown")
        }
        test {
            assertSameLocation(UnknownLocationWithNoLastLocation(reason = UnknownReason.PERMISSION_DENIED), it )
        }
    }

    @Test
    fun testKnownLocationsFollowedByALocationUnknown()  = runBlockingWithFlow {
        action {
            setLocation(location1)
        }
        test {
            assertSameLocation(location1, it)
        }

        action {
            setLocation(location2)
        }
        test { assertNotEquals(location1, it) }
        action {
            setLocationUnknown()
        }
        test {
            assertSameLocation(
                UnknownLocationWithLastLocation(
                    location2,
                    UnknownReason.NOT_CLEAR
                ), it
            )
        }
    }
}