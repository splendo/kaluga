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

package com.splendo.kaluga.example.ios

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertActionHandler
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.alerts.AlertInterface
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.example.shared.AlertPresenter
import com.splendo.kaluga.example.shared.HudPresenter
import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.hud.IOSHUD
import com.splendo.kaluga.location.LocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.notifications.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocationManager
import platform.UIKit.UILabel
import ru.pocketbyte.kydra.log.KydraLog

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

    fun location(label: UILabel, locationManager: CLLocationManager) {
        val locationStateRepo = LocationStateRepoBuilder(locationManager = locationManager)
            .create(Permission.Location(background = false, precise = true))

        MainScope().launch(MainQueueDispatcher) {
            LocationPrinter(locationStateRepo, this).printTo {
                label.text = it
            }
        }
        debug("proceed executing after location coroutines")
    }
}
