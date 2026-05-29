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

package com.splendo.kaluga.example.shared.viewmodel.permissions

import com.splendo.kaluga.example.shared.di.USE_LOCATION_FOR_BLUETOOTH
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.notifications.NotificationOptions
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.storage.StoragePermission
import kotlinx.serialization.Serializable

@Serializable
enum class PermissionView(val title: String) {
    Bluetooth("Bluetooth"),
    Calendar("Calendar"),
    Camera("Camera"),
    Contacts("Contacts"),
    Location("Location"),
    Microphone("Microphone"),
    Notifications("Notifications"),
    Storage("Storage"),
    ;

    val permission: Permission get() = when (this) {
        Bluetooth -> BluetoothPermission(BluetoothPermission.Type.Client(useForLocation = USE_LOCATION_FOR_BLUETOOTH))
        Calendar -> CalendarPermission(allowWrite = true)
        Camera -> CameraPermission
        Contacts -> ContactsPermission(allowWrite = true)
        Location -> LocationPermission(background = true, precise = true)
        Microphone -> MicrophonePermission
        Notifications -> NotificationsPermission(notificationOptions)
        Storage -> StoragePermission(allowWrite = true)
    }
}

expect val notificationOptions: NotificationOptions
