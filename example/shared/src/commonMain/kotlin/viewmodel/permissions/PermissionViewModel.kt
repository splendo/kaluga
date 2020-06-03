package com.splendo.kaluga.example.shared.viewmodel.permissions

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.request
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PermissionViewModel(private val permissions: Permissions, private val permission: Permission) : BaseViewModel() {

    val permissionStateMessage = permissions[permission]
        .map {permissionState ->
            when (permissionState) {
                is PermissionState.Allowed -> "permission_allowed"
                is PermissionState.Denied.Requestable -> "permission_requestable"
                is PermissionState.Denied.Locked -> "permission_denied"
            }
        }
        .toObservable(coroutineScope)

    val showPermissionButton = permissions[permission]
        .map { permissionState -> permissionState is PermissionState.Denied.Requestable }
        .toObservable(coroutineScope)

    private val _requestMessage = HotFlowable<String?>(null)
    val requestMessage = _requestMessage.toObservable(coroutineScope)

    fun requestPermission() {
        coroutineScope.launch {
            _requestMessage.set(if (permissions[permission].request()) "permission_request_success" else "permission_request_failed")
            _requestMessage.set(null)
        }
    }

}