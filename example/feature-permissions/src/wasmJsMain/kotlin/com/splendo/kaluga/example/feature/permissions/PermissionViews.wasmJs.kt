/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.permissions

import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.notifications.NotificationsPermission

// The browser only exposes geolocation and notifications; the rest of the permission types have no
// Web API. `background`/`precise` are accepted but inert on the web.
internal object LocationPermissionView : PermissionView("Location", "Location") {
    override val permission = LocationPermission(background = false, precise = true)
}
internal object NotificationsPermissionView : PermissionView("Notifications", "Notifications") {
    override val permission = NotificationsPermission(notificationOptions)
}

actual val availablePermissionViews: List<PermissionView> = listOf(
    LocationPermissionView,
    NotificationsPermissionView,
)
