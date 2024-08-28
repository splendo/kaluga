/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.notifications.NotificationOptions
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.registerAllPermissionsNotRegistered
import com.splendo.kaluga.permissions.storage.StoragePermission
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Serializable
enum class PermissionView(val title: String) {
    Bluetooth("permissions_bluetooth".localized()),
    Calendar("permissions_calendar".localized()),
    Camera("permissions_camera".localized()),
    Contacts("permissions_contacts".localized()),
    Location("permissions_location".localized()),
    Microphone("permissions_microphone".localized()),
    Notifications("permissions_notifications".localized()),
    Storage("permissions_storage".localized()),
    ;

    val permission: Permission get() = when (this) {
        Bluetooth -> BluetoothPermission
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

class PermissionsListNavigationAction(permissionView: PermissionView) :
    SingleValueNavigationAction<PermissionView>(
        permissionView,
        NavigationBundleSpecType.SerializedType(PermissionView.serializer()),
    )

class PermissionsListViewModel(navigator: Navigator<PermissionsListNavigationAction>) :
    NavigatingViewModel<PermissionsListNavigationAction>(navigator),
    KoinComponent {

    val permissions = observableOf(
        listOf(
            PermissionView.Bluetooth,
            PermissionView.Calendar,
            PermissionView.Camera,
            PermissionView.Contacts,
            PermissionView.Location,
            PermissionView.Microphone,
            PermissionView.Notifications,
            PermissionView.Storage,
        ),
    )
    private val permissionsBuilder: PermissionsBuilder by inject()

    fun onPermissionPressed(permissionView: PermissionView) {
        coroutineScope.launch {
            permissionsBuilder.registerAllPermissionsNotRegistered(
                settings = BasePermissionManager.Settings(
                    logger = RestrictedLogger(RestrictedLogLevel.None),
                ),
            )
            navigator.navigate(PermissionsListNavigationAction(permissionView))
        }
    }
}
