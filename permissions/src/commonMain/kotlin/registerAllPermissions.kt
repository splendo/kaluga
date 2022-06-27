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

import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.calendar.registerCalendarPermission
import com.splendo.kaluga.permissions.camera.registerCameraPermission
import com.splendo.kaluga.permissions.contacts.registerContactsPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.storage.registerStoragePermission

fun PermissionsBuilder.registerAllPermissions(
    monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval
) {
    registerBluetoothPermission(monitoringInterval = monitoringInterval)
    registerCalendarPermission(monitoringInterval = monitoringInterval)
    registerCameraPermission(monitoringInterval = monitoringInterval)
    registerContactsPermission(monitoringInterval = monitoringInterval)
    registerLocationPermission(monitoringInterval = monitoringInterval)
    registerMicrophonePermission(monitoringInterval = monitoringInterval)
    registerNotificationsPermission(monitoringInterval = monitoringInterval)
    registerMicrophonePermission(monitoringInterval = monitoringInterval)
    registerStoragePermission(monitoringInterval = monitoringInterval)
}
