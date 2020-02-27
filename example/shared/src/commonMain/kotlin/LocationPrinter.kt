package com.splendo.kaluga.example.shared

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

import com.splendo.kaluga.location.*
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.splendo.kaluga.log.debug

class LocationPrinter(private val locationState: LocationStateRepo) {

    fun printTo(printer: (String) -> Unit) = runBlocking {
        MainScope().launch {
            debug("main..")
            locationState.flow().location().collect {location ->
                debug("collecting...")
                val toPrint = when(location) {
                    is Location.KnownLocation -> {
                        "${location.latitudeDMS} ${location.longitudeDMS}"
                    }
                    is Location.UnknownLocation -> {
                        val lastLocation = if (location is Location.UnknownLocation.WithLastLocation) {
                            " Last Known Location: ${location.lastKnownLocation
                                .latitudeDMS} ${location.lastKnownLocation.longitudeDMS}"
                        } else
                            ""
                        "Unknown Location. Reason: ${location.reason.name}${lastLocation}"
                    }
                }
                printer("Received location: $toPrint")
                debug("location: $toPrint")
            }
            debug("bye main")
        }
    }
}
