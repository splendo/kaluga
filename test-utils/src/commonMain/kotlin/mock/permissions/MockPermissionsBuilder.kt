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

import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.permissions.PermissionManager
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
import kotlin.coroutines.CoroutineContext

class MockPermissionsBuilder : PermissionsBuilder() {

    private val _cameraPMManager = AtomicReference<MockPermissionManager<CameraPermission>?>(null)
    var cameraPMManager: MockPermissionManager<CameraPermission>?
        get() = _cameraPMManager.get()
        set(value) = _cameraPMManager.set(value)

    private val cameraPMBuilder: BaseCameraPermissionManagerBuilder = object : BaseCameraPermissionManagerBuilder {
        override fun create(repo: CameraPermissionStateRepo): PermissionManager<CameraPermission> {
            cameraPMManager = MockPermissionManager(repo)
            return cameraPMManager!!
        }
    }

    private val _contactsPMManager = AtomicReference<MockPermissionManager<ContactsPermission>?>(null)
    var contactsPMManager: MockPermissionManager<ContactsPermission>?
        get() = _contactsPMManager.get()
        set(value) = _contactsPMManager.set(value)

    private val contactsPMBuilder: BaseContactsPermissionManagerBuilder = object : BaseContactsPermissionManagerBuilder {
        override fun create(contacts: ContactsPermission, repo: ContactsPermissionStateRepo): PermissionManager<ContactsPermission> {
            contactsPMManager = MockPermissionManager(repo)
            return contactsPMManager!!
        }
    }

    private val _microphonePMManager = AtomicReference<MockPermissionManager<MicrophonePermission>?>(null)
    var microphonePMManager: MockPermissionManager<MicrophonePermission>?
        get() = _microphonePMManager.get()
        set(value) = _microphonePMManager.set(value)

    private val microphonePMBuilder: BaseMicrophonePermissionManagerBuilder = object : BaseMicrophonePermissionManagerBuilder {
        override fun create(repo: MicrophonePermissionStateRepo): PermissionManager<MicrophonePermission> {
            microphonePMManager = MockPermissionManager(repo)
            return microphonePMManager!!
        }
    }

    private val _notificationsPMManager = AtomicReference<MockPermissionManager<NotificationsPermission>?>(null)
    var notificationsPMManager: MockPermissionManager<NotificationsPermission>?
        get() = _notificationsPMManager.get()
        set(value) = _notificationsPMManager.set(value)

    private val notificationsPMBuilder: BaseNotificationsPermissionManagerBuilder = object : BaseNotificationsPermissionManagerBuilder {
        override fun create(notifications: NotificationsPermission, repo: NotificationsPermissionStateRepo): PermissionManager<NotificationsPermission> {
            notificationsPMManager = MockPermissionManager(repo)
            return notificationsPMManager!!
        }
    }

    private val _bluetoothPMManager = AtomicReference<MockPermissionManager<BluetoothPermission>?>(null)
    var bluetoothPMManager: MockPermissionManager<BluetoothPermission>?
        get() = _bluetoothPMManager.get()
        set(value) = _bluetoothPMManager.set(value)

    private val bluetoothPMBuilder: BaseBluetoothPermissionManagerBuilder = object : BaseBluetoothPermissionManagerBuilder {
        override fun create(repo: BluetoothPermissionStateRepo): PermissionManager<BluetoothPermission> {
            bluetoothPMManager = MockPermissionManager(repo)
            return bluetoothPMManager!!
        }
    }

    private val _locationPMManager = AtomicReference<MockPermissionManager<LocationPermission>?>(null)
    var locationPMManager: MockPermissionManager<LocationPermission>?
        get() = _locationPMManager.get()
        set(value) = _locationPMManager.set(value)

    private val locationPMBuilder: BaseLocationPermissionManagerBuilder = object : BaseLocationPermissionManagerBuilder {
        override fun create(location: LocationPermission, repo: LocationPermissionStateRepo): PermissionManager<LocationPermission> {
            return MockPermissionManager(repo).also {
                locationPMManager = it
            }
        }
    }

    private val _calendarPMManager = AtomicReference<MockPermissionManager<CalendarPermission>?>(null)
    var calendarPMManager: MockPermissionManager<CalendarPermission>?
        get() = _calendarPMManager.get()
        set(value) = _calendarPMManager.set(value)

    private val calendarPMBuilder: BaseCalendarPermissionManagerBuilder = object : BaseCalendarPermissionManagerBuilder {
        override fun create(calendar: CalendarPermission, repo: CalendarPermissionStateRepo): PermissionManager<CalendarPermission> {
            calendarPMManager = MockPermissionManager(repo)
            return calendarPMManager!!
        }
    }

    private val _storagePMManager = AtomicReference<MockPermissionManager<StoragePermission>?>(null)
    var storagePMManager: MockPermissionManager<StoragePermission>?
        get() = _storagePMManager.get()
        set(value) = _storagePMManager.set(value)

    private val storagePMBuilder: BaseStoragePermissionManagerBuilder = object : BaseStoragePermissionManagerBuilder {
        override fun create(storage: StoragePermission, repo: StoragePermissionStateRepo): PermissionManager<StoragePermission> {
            storagePMManager = MockPermissionManager(repo)
            return storagePMManager!!
        }
    }

    fun registerAllPermissionsBuilders() {
        register(bluetoothPMBuilder, BluetoothPermission::class).also { builder ->
            registerRepoFactory(BluetoothPermission::class) { permission, coroutineContext ->
                MockBluetoothPermissionStateRepo(builder, coroutineContext)
            }
        }
        register(locationPMBuilder, LocationPermission::class).also { builder ->
            registerRepoFactory(LocationPermission::class) { permission, coroutineContext ->
                MockLocationPermissionStateRepo(permission as LocationPermission, builder, coroutineContext)
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

    private class MockBluetoothPermissionStateRepo(
        builder: BaseBluetoothPermissionManagerBuilder,
        coroutineContext: CoroutineContext
    ) : BluetoothPermissionStateRepo(coroutineContext) {
        private val _permissionManager = AtomicReference<PermissionManager<BluetoothPermission>?>(null)
        override val permissionManager: PermissionManager<BluetoothPermission> by lazy { _permissionManager.get()!! }

        init {
            _permissionManager.set(builder.create(this))
        }
    }

    private class MockLocationPermissionStateRepo(
        location: LocationPermission,
        builder: BaseLocationPermissionManagerBuilder,
        coroutineContext: CoroutineContext
    ) : LocationPermissionStateRepo(coroutineContext) {
        private val _permissionManager = AtomicReference<PermissionManager<LocationPermission>?>(null)
        override val permissionManager: PermissionManager<LocationPermission> by lazy { _permissionManager.get()!! }

        init {
            _permissionManager.set(builder.create(location, this))
        }
    }
}
