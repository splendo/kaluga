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

import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.PermissionStateRepo
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.calendar.BaseCalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermission
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.MockPermissionStateRepo

class MockPermissionsBuilder : PermissionsBuilder() {
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

    lateinit var bluetoothPMManager: MockPermissionManager<BluetoothPermission>
    private val bluetoothPMBuilder: BaseBluetoothPermissionManagerBuilder = object : BaseBluetoothPermissionManagerBuilder {
        override fun create(repo: BluetoothPermissionStateRepo): PermissionManager<BluetoothPermission> {
            bluetoothPMManager = MockPermissionManager(repo)
            return bluetoothPMManager
        }
    }

    lateinit var locationPMManager: MockPermissionManager<LocationPermission>
    private val locationPMBuilder: BaseLocationPermissionManagerBuilder = object : BaseLocationPermissionManagerBuilder {
        override fun create(location: LocationPermission, repo: LocationPermissionStateRepo): PermissionManager<LocationPermission> {
            locationPMManager = MockPermissionManager(repo)
            return locationPMManager
        }
    }

    lateinit var calendarPMManager: MockPermissionManager<CalendarPermission>
    private val calendarPMBuilder: BaseCalendarPermissionManagerBuilder = object : BaseCalendarPermissionManagerBuilder {
        override fun create(calendar: CalendarPermission, repo: CalendarPermissionStateRepo): PermissionManager<CalendarPermission> {
            calendarPMManager = MockPermissionManager(repo)
            return calendarPMManager
        }
    }

    lateinit var storagePMManager: MockPermissionManager<StoragePermission>
    private val storagePMBuilder: BaseStoragePermissionManagerBuilder = object : BaseStoragePermissionManagerBuilder {
        override fun create(storage: StoragePermission, repo: StoragePermissionStateRepo): PermissionManager<StoragePermission> {
            storagePMManager = MockPermissionManager(repo)
            return storagePMManager
        }
    }

    fun registerAllPermissionsBuilders() {
        register(bluetoothPMBuilder, BluetoothPermission::class).also { builder ->
            registerRepoFactory(BluetoothPermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<BluetoothPermission>()
            }
        }
        register(locationPMBuilder, LocationPermission::class).also { builder ->
            registerRepoFactory(LocationPermission::class) { permission, coroutineContext ->
                LocationPermissionStateRepo(permission as LocationPermission, builder, coroutineContext)
            }
        }
        register(calendarPMBuilder, CalendarPermission::class).also { builder ->
            registerRepoFactory(CalendarPermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<CalendarPermission>()
            }
        }
        register(storagePMBuilder, StoragePermission::class).also { builder ->
            registerRepoFactory(StoragePermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<StoragePermission>()
            }
        }
        register(cameraPMBuilder, CameraPermission::class).also { builder ->
            registerRepoFactory(CameraPermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<CameraPermission>()
            }
        }
        register(contactsPMBuilder, ContactsPermission::class).also { builder ->
            registerRepoFactory(ContactsPermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<ContactsPermission>()
            }
        }
        register(microphonePMBuilder, MicrophonePermission::class).also { builder ->
            registerRepoFactory(MicrophonePermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<MicrophonePermission>()
            }
        }
        register(notificationsPMBuilder, NotificationsPermission::class).also { builder ->
            registerRepoFactory(NotificationsPermission::class) { permission, coroutineContext ->
                MockPermissionStateRepo<NotificationsPermission>()
            }
        }
    }
}
