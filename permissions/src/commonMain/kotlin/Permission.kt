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

import com.splendo.kaluga.basepermissions.Permission
import com.splendo.kaluga.basepermissions.PermissionsBuilder
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationOptions
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo

/**
 * Permission to access the users Camera
 */
object CameraPermission : Permission()

/**
 * Permission to access the users Contacts
 * @param allowWrite If `true` writing to the contacts is permitted
 */
data class ContactsPermission(val allowWrite: Boolean = false) : Permission()

/**
 * Permission to access the users Location
 * @param background If `true` scanning for location in the background is permitted
 * @param precise If `true` precise location scanning is permitted
 */
data class LocationPermission(val background: Boolean = false, val precise: Boolean = false) : Permission()

/**
 * Permission to access the users Microphone
 */
object MicrophonePermission : Permission()

/**
 * Permission to access the users Notifications.
 * @param options The [NotificationOptions] determining the type of notifications that can be accessed
 */
data class NotificationsPermission(val options: NotificationOptions? = null) : Permission()

/**
 * Permission to access the users device storage.
 * On iOS this corresponds to the Photos permission
 * @param allowWrite If `true` writing to the storage is permitted
 */
data class StoragePermission(val allowWrite: Boolean = false) : Permission()

// ********************** Camera *****************
fun PermissionsBuilder.registerCameraPermission() =
    registerCameraPermissionBuilder().also { builder ->
        registerRepoFactory(CameraPermission::class) { _, coroutineContext ->
            CameraPermissionStateRepo(builder as BaseCameraPermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerCameraPermissionBuilder() : CameraPermissionManagerBuilder

// ********************** Contacts *****************
fun PermissionsBuilder.registerContactsPermission() =
    registerContactsPermissionBuilder().also { builder ->
        registerRepoFactory(ContactsPermission::class) { permission, coroutineContext ->
            ContactsPermissionStateRepo(permission as  ContactsPermission, builder as BaseContactsPermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerContactsPermissionBuilder() : ContactsPermissionManagerBuilder

// ********************** Microphone *****************
fun PermissionsBuilder.registerMicrophonePermission() =
    registerMicrophonePermissionBuilder().also { builder ->
        registerRepoFactory(MicrophonePermission::class) { _, coroutineContext ->
            MicrophonePermissionStateRepo(builder as BaseMicrophonePermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerMicrophonePermissionBuilder() : MicrophonePermissionManagerBuilder

// ********************** Notifications *****************
fun PermissionsBuilder.registerNotificationsPermission() =
    registerNotificationsPermissionBuilder().also { builder ->
        registerRepoFactory(NotificationsPermission::class) { permission, coroutineContext ->
            NotificationsPermissionStateRepo(permission as NotificationsPermission, builder as BaseNotificationsPermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerNotificationsPermissionBuilder() : NotificationsPermissionManagerBuilder

// ********************** Storage *****************
fun PermissionsBuilder.registerStoragePermission() =
    registerStoragePermissionBuilder().also { builder ->
        registerRepoFactory(StoragePermission::class) { permission, coroutineContext ->
            StoragePermissionStateRepo(permission as StoragePermission, builder as BaseStoragePermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerStoragePermissionBuilder() : StoragePermissionManagerBuilder
// ***************************************
