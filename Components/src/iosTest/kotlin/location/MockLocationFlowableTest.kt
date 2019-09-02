package com.splendo.mpp.location

import com.splendo.mpp.location.Location.KnownLocation
import com.splendo.mpp.location.test.LocationFlowableTest
import kotlinx.cinterop.cValue
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSDate
import platform.Foundation.create
import kotlin.test.BeforeTest

class MockLocationFlowableTest: LocationFlowableTest() {


    lateinit var manager: CLLocationManager
    @BeforeTest
    fun setLocationManager() {
        manager = flowable.addCLLocationManager()

    }

    private fun clLocation(location: KnownLocation):CLLocation {

        location.apply {
            return CLLocation(
                coordinate =  cValue {
                    latitude = location.latitude
                    longitude = location.longitude
                },
                altitude = altitude ?: 0.0,
                verticalAccuracy = verticalAccuracy ?: 0.0,
                horizontalAccuracy = horizontalAccuracy ?: 0.0,
                speed = speed ?: 0.0,
                course = course ?: 0.0,
                timestamp = NSDate.create(timeIntervalSince1970 = time.ms / 1000.0))
        }

    }

    override suspend fun setLocation(location: KnownLocation) {
        manager.delegate!!.locationManager(manager = manager, didUpdateLocations = listOf (
            clLocation(location)
        ))
    }



}