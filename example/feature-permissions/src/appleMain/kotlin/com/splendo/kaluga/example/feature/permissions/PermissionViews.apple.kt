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

import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.storage.StoragePermission

internal object BluetoothPermissionView : PermissionView("Bluetooth", "Bluetooth") {
    override val permission = BluetoothPermission(BluetoothPermission.Type.Client(useForLocation = USE_LOCATION_FOR_BLUETOOTH))
}
internal object CalendarPermissionView : PermissionView("Calendar", "Calendar") {
    override val permission = CalendarPermission(allowWrite = true)
}
internal object CameraPermissionView : PermissionView("Camera", "Camera") {
    override val permission = CameraPermission
}
internal object ContactsPermissionView : PermissionView("Contacts", "Contacts") {
    override val permission = ContactsPermission(allowWrite = true)
}
internal object LocationPermissionView : PermissionView("Location", "Location") {
    override val permission = LocationPermission(background = true, precise = true)
}
internal object MicrophonePermissionView : PermissionView("Microphone", "Microphone") {
    override val permission = MicrophonePermission
}
internal object NotificationsPermissionView : PermissionView("Notifications", "Notifications") {
    override val permission = NotificationsPermission(notificationOptions)
}
internal object StoragePermissionView : PermissionView("Storage", "Storage") {
    override val permission = StoragePermission(allowWrite = true)
}

actual val availablePermissionViews: List<PermissionView> = listOf(
    BluetoothPermissionView,
    CalendarPermissionView,
    CameraPermissionView,
    ContactsPermissionView,
    LocationPermissionView,
    MicrophonePermissionView,
    NotificationsPermissionView,
    StoragePermissionView,
)
