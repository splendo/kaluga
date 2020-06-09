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

import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.location.LocationFlowable
import com.splendo.kaluga.logging.debug
import platform.CoreLocation.CLLocationManager
import platform.UIKit.UILabel
import ru.pocketbyte.kydra.log.KydraLog

class KotlinNativeFramework {

    fun hello() = com.splendo.kaluga.example.shared.helloCommon()

    // expose a dependency to Swift as an example
    fun logger(): ru.pocketbyte.kydra.log.Logger = KydraLog.logger

    fun location(label: UILabel, locationManager: CLLocationManager) {
        val location = LocationFlowable.Builder(locationManager).create()
        LocationPrinter(location).printTo {
            label.text = it
        }
        debug("proceed executing after location coroutines")
    }

}
