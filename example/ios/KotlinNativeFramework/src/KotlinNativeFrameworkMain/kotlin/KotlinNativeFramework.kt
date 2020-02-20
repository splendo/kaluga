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
import com.splendo.kaluga.example.shared.PermissionsPrinter
import com.splendo.kaluga.example.shared.AlertPresenter
import com.splendo.kaluga.example.shared.HudPresenter
import com.splendo.kaluga.location.LocationFlowable
import com.splendo.kaluga.log.Logger
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionManagerBuilder
import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertInterface
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.alerts.AlertActionHandler
import com.splendo.kaluga.hud.IOSHUD
import com.splendo.kaluga.base.MainQueueDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSBundle
import platform.UIKit.UILabel
import ru.pocketbyte.hydra.log.HydraLog
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
    fun logger(): ru.pocketbyte.hydra.log.Logger = HydraLog.logger

    fun location(label: UILabel, locationManager: CLLocationManager) {
        val location = LocationFlowable.Builder(locationManager).create()
        LocationPrinter(location).printTo {
            label.text = it
        }
        debug("proceed executing after location coroutines")
    }

    private val permissions = Permissions(BluetoothPermissionManagerBuilder())

    fun permissionStatus(alertBuilder: AlertBuilder) {
        PermissionsPrinter(permissions).printPermission { message ->
            val coroutine = MainScope().launch(MainQueueDispatcher) {
                alertBuilder.buildAlert {
                    setTitle("Status")
                    setMessage(message)
                    setPositiveButton("OK")
                }.show()
            }
            MainScope().launch(MainQueueDispatcher) {
                delay(3 * 1_000)
                coroutine.cancel()
            }
        }
    }

    fun permissionRequest(alertBuilder: AlertBuilder) {
        PermissionsPrinter(permissions).printRequest { message ->
            val coroutine = MainScope().launch(MainQueueDispatcher) {
                alertBuilder.buildAlert {
                    setTitle("Request Succeeded")
                    setMessage(message)
                    setPositiveButton("OK")
                }.show()
            }
            MainScope().launch(MainQueueDispatcher) {
                delay(3 * 1_000)
                coroutine.cancel()
            }
        }
    }
}
