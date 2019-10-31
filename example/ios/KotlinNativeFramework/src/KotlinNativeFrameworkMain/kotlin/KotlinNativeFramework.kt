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
import com.splendo.kaluga.log.Logger
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertInterface
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.alerts.AlertActionHandler
import com.splendo.kaluga.loadingIndicator.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSBundle
import platform.UIKit.UILabel
import ru.pocketbyte.hydra.log.HydraLog
import platform.UIKit.UIViewController

class KotlinNativeFramework {
    private val loc = LocationFlowable()

    fun hello() = com.splendo.kaluga.example.shared.helloCommon()

    // expose a dependency to Swift as an example
    fun logger(): ru.pocketbyte.hydra.log.Logger = HydraLog.logger

    fun makeAlert(from: UIViewController, title: String? = null, message: String? = null, actions: List<Alert.Action>): AlertInterface {
        return AlertBuilder(from)
                .setTitle(title)
                .setMessage(message)
                .addActions(actions)
                .create()
    }

    fun loadingIndicator(view: UIViewController): LoadingIndicator {
        return IOSLoadingIndicator
            .Builder()
            .setView(view)
            .create()
    }

    fun location(label: UILabel, locationManager: CLLocationManager) {
        loc.addCLLocationManager(locationManager)
        LocationPrinter(loc).printTo {
            label.text = it
        }
        debug("proceed executing after location coroutines")

    }

    fun permissions(nsBundle: NSBundle): Permissions {
        return Permissions.Builder()
                .bundle(nsBundle)
                .build()
    }

}
