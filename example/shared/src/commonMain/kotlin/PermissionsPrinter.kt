package com.splendo.kaluga.example.shared

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.request
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class PermissionsPrinter(val permissions: Permissions) {

    fun printPermission(printer: (String) -> Unit) = runBlocking {
        launch(Dispatchers.Default) {
            val message = when (permissions[Permission.Bluetooth].first()) {
                is PermissionState.Allowed -> "Allowed"
                is PermissionState.Denied.Requestable -> "Denied but Requestable"
                is PermissionState.Denied.SystemLocked -> "Denied"
            }
            MainScope().launch {
                printer("Permission = $message")
            }
        }
    }

    fun printRequest(printer: (String) -> Unit) = runBlocking {
        launch(Dispatchers.Default) {
            val success = permissions[Permission.Bluetooth].request()
            MainScope().launch {
                    printer("Request = $success")
            }
        }
    }

}