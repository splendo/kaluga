package com.splendo.mpp.location.test

import com.splendo.mpp.location.Location
import com.splendo.mpp.location.Location.*
import com.splendo.mpp.location.LocationFlowable
import com.splendo.mpp.test.FlowableTest
import com.splendo.mpp.util.debug
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