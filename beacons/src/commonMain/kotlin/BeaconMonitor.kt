package com.splendo.kaluga.beacons

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

class BeaconMonitor(private val scanner: BeaconScanner) {

    fun subscribe(beaconId: String, listener: (isPresent: Boolean) -> Unit) = scanner.addListener(beaconId, object : Listener {
        override fun onStateUpdate(beaconId: String, isPresent: Boolean) = listener(isPresent)
    })

    fun unsubscribe(beaconId: String) = scanner.removeListener(beaconId)
}



