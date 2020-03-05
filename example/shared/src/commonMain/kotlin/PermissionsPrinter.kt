package com.splendo.kaluga.example.shared

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.request
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PermissionsPrinter(val permissions: Permissions, private val permission: Permission) {

    fun printPermission(printer: (String) -> Unit) = MainScope().launch(MainQueueDispatcher) {
        val message = when (permissions[permission].first()) {
            is PermissionState.Allowed -> "Allowed"
            is PermissionState.Denied.Requestable -> "Denied but Requestable"
            is PermissionState.Denied.Locked -> "Denied"
        }
        printer("Permission = $message")
    }

    fun printRequest(printer: (String) -> Unit) = MainScope().launch(MainQueueDispatcher) {
        val success = permissions[permission].request()
        printer("Request = $success")
    }

}