/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.permissions

import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.CameraPermission
import com.splendo.kaluga.permissions.ContactsPermission
import com.splendo.kaluga.permissions.MicrophonePermission
import com.splendo.kaluga.permissions.NotificationsPermission
import com.splendo.kaluga.permissions.StoragePermission
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import com.splendo.kaluga.test.MockPermissionManager

class MockPermissionsBuilder : PermissionsBuilder() {
    init {
        registerAllPermissionsBuilders()
    }

    private lateinit var cameraPMManager: MockPermissionManager<CameraPermission>
    private val cameraPMBuilder: BaseCameraPermissionManagerBuilder = object : BaseCameraPermissionManagerBuilder {
        override fun create(repo: CameraPermissionStateRepo): PermissionManager<CameraPermission> {
            cameraPMManager = MockPermissionManager(repo)
            return cameraPMManager
        }
    }

    private lateinit var contactsPMManager: MockPermissionManager<ContactsPermission>
    private val contactsPMBuilder: BaseContactsPermissionManagerBuilder = object : BaseContactsPermissionManagerBuilder {
        override fun create(contacts: ContactsPermission, repo: ContactsPermissionStateRepo): PermissionManager<ContactsPermission> {
            contactsPMManager = MockPermissionManager(repo)
            return contactsPMManager
        }
    }

    private lateinit var microphonePMManager: MockPermissionManager<MicrophonePermission>
    private val microphonePMBuilder: BaseMicrophonePermissionManagerBuilder = object : BaseMicrophonePermissionManagerBuilder {
        override fun create(repo: MicrophonePermissionStateRepo): PermissionManager<MicrophonePermission> {
            microphonePMManager = MockPermissionManager(repo)
            return microphonePMManager
        }
    }

    lateinit var notificationsPMManager: MockPermissionManager<NotificationsPermission>
    private val notificationsPMBuilder: BaseNotificationsPermissionManagerBuilder = object : BaseNotificationsPermissionManagerBuilder {
        override fun create(notifications: NotificationsPermission, repo: NotificationsPermissionStateRepo): PermissionManager<NotificationsPermission> {
            notificationsPMManager = MockPermissionManager(repo)
            return notificationsPMManager
        }
    }

    lateinit var storagePMManager: MockPermissionManager<StoragePermission>
    private val storagePMBuilder: BaseStoragePermissionManagerBuilder = object : BaseStoragePermissionManagerBuilder {
        override fun create(storage: StoragePermission, repo: StoragePermissionStateRepo): PermissionManager<StoragePermission> {
            storagePMManager = MockPermissionManager(repo)
            return storagePMManager
        }
    }

    private fun registerAllPermissionsBuilders() {
        register(cameraPMBuilder, CameraPermission::class)
        register(contactsPMBuilder, ContactsPermission::class)
        register(microphonePMBuilder, MicrophonePermission::class)
        register(notificationsPMBuilder, NotificationsPermission::class)
        register(storagePMBuilder, StoragePermission::class)
    }
}
