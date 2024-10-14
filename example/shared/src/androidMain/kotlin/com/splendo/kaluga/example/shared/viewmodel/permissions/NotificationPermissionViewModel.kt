/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermissionIfNotRegistered
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val permissionsDispatcher = singleThreadDispatcher("NotificationPermissionsDispatcher")

class NotificationPermissionViewModel :
    BaseLifecycleViewModel(),
    KoinComponent {

    companion object {
        private val permission = NotificationsPermission(notificationOptions)
    }

    private val permissionsBuilder: PermissionsBuilder by inject()
    private val permissions = Permissions(permissionsBuilder, coroutineScope.coroutineContext + permissionsDispatcher)

    val hasPermission by lazy {
        permissions[permission]
            .map { permissionState -> permissionState is PermissionState.Allowed }
            .toInitializedObservable(false, coroutineScope)
    }

    init {
        permissionsBuilder.registerNotificationsPermissionIfNotRegistered(
            settings = BasePermissionManager.Settings(
                logger = RestrictedLogger(RestrictedLogLevel.None),
            ),
        )
    }

    fun requestPermission() {
        coroutineScope.launch {
            permissions.request(permission)
        }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}
