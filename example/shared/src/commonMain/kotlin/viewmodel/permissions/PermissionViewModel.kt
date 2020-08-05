/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.request
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PermissionViewModel(private val permissions: Permissions, private val permission: Permission) : BaseViewModel() {

    val permissionStateMessage: Observable<String> = permissions[permission]
        .map { permissionState ->
            when (permissionState) {
                is PermissionState.Allowed -> "permission_allowed".localized()
                is PermissionState.Denied.Requestable -> "permission_requestable".localized()
                is PermissionState.Denied.Locked -> "permission_denied".localized()
            }
        }
        .toObservable(coroutineScope)

    val showPermissionButton: Observable<Boolean> = permissions[permission]
        .map { permissionState -> permissionState is PermissionState.Denied.Requestable }
        .toObservable(coroutineScope)

    private val _requestMessage = HotFlowable<String?>(null)
    val requestMessage: Observable<String?> = _requestMessage.toObservable(coroutineScope)

    fun requestPermission() {
        coroutineScope.launch {
            _requestMessage.set((if (permissions[permission].request()) "permission_request_success" else "permission_request_failed").localized())
            _requestMessage.set(null)
        }
    }
}
