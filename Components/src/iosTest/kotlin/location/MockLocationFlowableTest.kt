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

import com.splendo.kaluga.location.Location.KnownLocation
import com.splendo.kaluga.location.test.LocationFlowableTest
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