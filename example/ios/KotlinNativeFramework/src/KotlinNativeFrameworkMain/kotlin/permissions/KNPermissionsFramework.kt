package com.splendo.kaluga.example.ios.permissions

import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.example.shared.PermissionsPrinter
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.notifications.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.UserNotifications.UNAuthorizationOptions

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