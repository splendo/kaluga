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
import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.example.shared.PermissionsPrinter
import com.splendo.kaluga.hud.IOSHUD
import com.splendo.kaluga.location.LocationFlowable
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.notifications.*
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

class KNPermissionsFramework {
    private val permissions = Permissions(PermissionsBuilder())

    fun permissionStatusBluetooth(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Bluetooth, alertBuilder)
    }

    fun permissionRequestBluetooth(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Bluetooth, alertBuilder)
    }

    fun permissionStatusContacts(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Contacts(), alertBuilder)
    }

    fun permissionStatusCalendar(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Calendar(), alertBuilder)
    }

    fun permissionRequestCalendar(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Calendar(), alertBuilder)
    }

    fun permissionStatusCamera(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Camera, alertBuilder)
    }

    fun permissionRequestCamera(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Camera, alertBuilder)
    }

    fun permissionRequestContacts(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Contacts(), alertBuilder)
    }

    fun permissionStatusLocation(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Location(background=true, precise=true), alertBuilder)
    }

    fun permissionRequestLocation(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Location(background=true, precise=true), alertBuilder)
    }

    fun permissionStatusMicrophone(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Microphone, alertBuilder)
    }

    fun permissionRequestMicrophone(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Microphone, alertBuilder)
    }

    fun permissionStatusNotifications(alertBuilder: AlertBuilder, options: UNAuthorizationOptions) {
        val notificationOptions = NotificationOptions(options)
        permissionStatus(Permission.Notifications(notificationOptions), alertBuilder)
    }

    fun permissionRequestNotifications(alertBuilder: AlertBuilder, options: UNAuthorizationOptions) {
        val notificationOptions = NotificationOptions(options)
        permissionRequest(Permission.Notifications(notificationOptions), alertBuilder)
    }

    fun permissionStatusStorage(alertBuilder: AlertBuilder) {
        permissionStatus(Permission.Storage(), alertBuilder)
    }

    fun permissionRequestStorage(alertBuilder: AlertBuilder) {
        permissionRequest(Permission.Storage(), alertBuilder)
    }

    private fun permissionStatus(permission: Permission, alertBuilder: AlertBuilder) {
        PermissionsPrinter(permissions, permission).printPermission { message ->
            showTempAlert("Status", message, alertBuilder)
        }
    }

    private fun permissionRequest(permission: Permission, alertBuilder: AlertBuilder) {
        PermissionsPrinter(permissions, permission).printRequest { message ->
            showTempAlert("Request", message, alertBuilder)
        }
    }

    private fun showTempAlert(title: String, message: String, alertBuilder: AlertBuilder) {
        val coroutine = MainScope().launch(MainQueueDispatcher) {
            alertBuilder.buildAlert {
                setTitle(title)
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

//    fun permissions(nsBundle: NSBundle) = Permissions
//        .Builder(nsBundle)
//        .build()
}
