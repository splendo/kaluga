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
package com.splendo.kaluga.permissions

import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionManagerBuilder

actual data class PermissionsBuilder(
    actual val bluetoothPMBuilder: BluetoothPermissionManagerBuilder = BluetoothPermissionManagerBuilder(),
    actual val calendarPMBuilder: CalendarPermissionManagerBuilder = CalendarPermissionManagerBuilder(),
    actual val cameraPMBuilder: CameraPermissionManagerBuilder = CameraPermissionManagerBuilder(),
    actual val contactsPMBuilder: ContactsPermissionManagerBuilder = ContactsPermissionManagerBuilder(),
    actual val locationPMBuilder: LocationPermissionManagerBuilder = LocationPermissionManagerBuilder(),
    actual val microphonePMBuilder: MicrophonePermissionManagerBuilder = MicrophonePermissionManagerBuilder(),
    actual val notificationsPMBuilder: NotificationsPermissionManagerBuilder = NotificationsPermissionManagerBuilder(),
    actual val storagePMBuilder: StoragePermissionManagerBuilder = StoragePermissionManagerBuilder()
)
