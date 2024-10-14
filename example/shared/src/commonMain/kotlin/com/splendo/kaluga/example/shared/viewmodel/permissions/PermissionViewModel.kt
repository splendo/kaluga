/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.permissions

import com.splendo.kaluga.architecture.observable.InitializedObservable
import com.splendo.kaluga.architecture.observable.UninitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.observable.toUninitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val permissionsDispatcher = singleThreadDispatcher("PermissionsDispatcher")

class PermissionViewModel(private val permission: Permission) :
    BaseLifecycleViewModel(),
    KoinComponent {

    val title = permission.name
    private val permissionsBuilder: PermissionsBuilder by inject()

    private val permissions = Permissions(permissionsBuilder, coroutineScope.coroutineContext + permissionsDispatcher)
    val permissionStateMessage: UninitializedObservable<String> = permissions[permission]
        .map { permissionState ->
            when (permissionState) {
                is PermissionState.Allowed -> "permission_allowed".localized()
                is PermissionState.Denied.Requestable -> "permission_requestable".localized()
                is PermissionState.Denied.Locked -> "permission_denied".localized()
                is PermissionState.Initializing,
                is PermissionState.Deinitialized,
                is PermissionState.Uninitialized,
                -> "permission_unknown".localized()
            }
        }
        .toUninitializedObservable(coroutineScope)

    val showPermissionButton: UninitializedObservable<Boolean> = permissions[permission]
        .map { permissionState -> permissionState is PermissionState.Denied.Requestable }
        .toUninitializedObservable(coroutineScope)

    private val _requestMessage = MutableSharedFlow<String?>(0)
    val requestMessage: InitializedObservable<String?> = _requestMessage.toInitializedObservable(null, coroutineScope)

    fun requestPermission() {
        coroutineScope.launch {
            _requestMessage.emit((if (permissions.request(permission)) "permission_request_success" else "permission_request_failed").localized())
            _requestMessage.emit(null)
        }
    }
}
