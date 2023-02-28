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
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.calendar.registerCalendarPermission
import com.splendo.kaluga.permissions.calendar.registerCalendarPermissionIfNotRegistered
import com.splendo.kaluga.permissions.camera.registerCameraPermission
import com.splendo.kaluga.permissions.camera.registerCameraPermissionIfNotRegistered
import com.splendo.kaluga.permissions.contacts.registerContactsPermission
import com.splendo.kaluga.permissions.contacts.registerContactsPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermission
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermissionIfNotRegistered
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermissionIfNotRegistered
import com.splendo.kaluga.permissions.storage.registerStoragePermission
import com.splendo.kaluga.permissions.storage.registerStoragePermissionIfNotRegistered
import kotlin.time.Duration

fun PermissionsBuilder.registerAllPermissions(
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) {
    registerBluetoothPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerCalendarPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerCameraPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerContactsPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerLocationPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerMicrophonePermission(monitoringInterval = monitoringInterval, settings = settings)
    registerNotificationsPermission(monitoringInterval = monitoringInterval, settings = settings)
    registerMicrophonePermission(monitoringInterval = monitoringInterval, settings = settings)
    registerStoragePermission(monitoringInterval = monitoringInterval, settings = settings)
}

suspend fun PermissionsBuilder.registerAllPermissionsNotRegistered(
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) {
    registerBluetoothPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerCalendarPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerCameraPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerContactsPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerLocationPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerMicrophonePermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerNotificationsPermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerMicrophonePermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
    registerStoragePermissionIfNotRegistered(monitoringInterval = monitoringInterval, settings = settings)
}
