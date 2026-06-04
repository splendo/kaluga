/*
 * Copyright 2026 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.permissions

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermissionIfNotRegistered
import kotlin.time.Duration

// The JS family (js + wasmJs) supports the permissions whose capabilities the browser exposes:
// geolocation (Permissions API) and notifications (Notification API). The per-permission modules are
// declared in the js/wasmJs dependency blocks, so this lives per-target rather than in webMain.
actual fun PermissionsBuilder.registerAllPermissions(monitoringInterval: Duration, settings: BasePermissionManager.Settings) {
    registerLocationPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerNotificationsPermission(monitoringInterval = monitoringInterval, settings = settings)
}

actual suspend fun PermissionsBuilder.registerAllPermissionsNotRegistered(monitoringInterval: Duration, settings: BasePermissionManager.Settings) {
    registerLocationPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerNotificationsPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
}
