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

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertActionHandler
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.alerts.AlertInterface
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.example.shared.AlertPresenter
import com.splendo.kaluga.example.shared.HudPresenter
import com.splendo.kaluga.hud.IOSHUD
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.splendo.kaluga.logging.debug
import platform.CoreLocation.CLLocationManager
import platform.UIKit.UILabel
import platform.UserNotifications.UNAuthorizationOptions
import ru.pocketbyte.kydra.log.KydraLog
import platform.UIKit.UIViewController

class KNAlertFramework {
    companion object {
        fun makeAlertPresenter(builder: AlertBuilder) = AlertPresenter(builder)
    }
}

class KNHudFramework {
    companion object {
        fun makeHudPresenter(builder: IOSHUD.Builder) = HudPresenter(builder)
    }
}

class KotlinNativeFramework {

    fun hello() = com.splendo.kaluga.example.shared.helloCommon()

    // expose a dependency to Swift as an example
    fun logger(): ru.pocketbyte.kydra.log.Logger = KydraLog.logger

//    fun permissions(nsBundle: NSBundle) = Permissions
//        .Builder(nsBundle)
//        .build()
}
