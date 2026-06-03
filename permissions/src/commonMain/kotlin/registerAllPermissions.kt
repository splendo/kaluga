/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
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
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.time.Duration

/**
 * Registers every permission available on the current platform on the given [PermissionsBuilder].
 *
 * The set of permissions registered depends on the target:
 * - iOS, macOS, Android: bluetooth, calendar, camera, contacts, location, microphone, notifications, storage
 * - tvOS: bluetooth, microphone, notifications, storage
 * - watchOS: bluetooth, calendar, contacts, microphone, notifications
 */
expect fun PermissionsBuilder.registerAllPermissions(
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
)

/**
 * Registers every permission available on the current platform on the given [PermissionsBuilder]
 * unless they have been registered already. See [registerAllPermissions] for the set per target.
 */
expect suspend fun PermissionsBuilder.registerAllPermissionsNotRegistered(
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
)
