/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.permissions.notifications

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionState


actual class NotificationsPermissionManager(
    stateRepo: NotificationsPermissionStateRepo
) : PermissionManager<Permission.Notifications>(stateRepo) {

    override suspend fun requestPermission() {
        // Android always has permissions, so no need to request
    }

    override fun initializeState(): PermissionState<Permission.Notifications> {
        // Android always has permissions, so always allowed
        return PermissionState.Allowed(this)
    }

    override fun startMonitoring(interval: Long) {
        // Android always has permissions, so no need to monitor
    }

    override fun stopMonitoring() {
    }


}

actual class NotificationsPermissionManagerBuilder() : BaseNotificationsPermissionManagerBuilder {

    override fun create(repo: NotificationsPermissionStateRepo): NotificationsPermissionManager {
        return NotificationsPermissionManager(repo)
    }

}

