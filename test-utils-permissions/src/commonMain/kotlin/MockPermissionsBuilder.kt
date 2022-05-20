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

package com.splendo.kaluga.test.permissions

import co.touchlab.stately.collections.IsoMutableList
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.calendar.BaseCalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.calendar.registerCalendarPermission
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.camera.registerCameraPermission
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.registerContactsPermission
import com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermission
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermission
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import com.splendo.kaluga.permissions.storage.registerStoragePermission
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

expect val mockPermissionContext: PermissionContext

class MockPermissionsBuilder(
    initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.DENIED,
    monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval,
    setupMocks: Boolean = true
) : PermissionsBuilder(mockPermissionContext) {

    val createdCameraPermissionManagers = IsoMutableList<MockPermissionManager<CameraPermission>>()
    val createCameraPermissionManagerMock = ::createCameraPermissionManager.mock()
    private fun createCameraPermissionManager(repo: PermissionStateRepo<CameraPermission>): PermissionManager<CameraPermission> = createCameraPermissionManagerMock.call(repo)
    private val cameraPMBuilder: BaseCameraPermissionManagerBuilder = object : BaseCameraPermissionManagerBuilder {
        override fun create(repo: PermissionStateRepo<CameraPermission>): PermissionManager<CameraPermission> = createCameraPermissionManager(repo)
    }

    val buildCameraStateRepos = IsoMutableList<PermissionStateRepo<CameraPermission>>()
    val cameraStateRepoBuilderMock = ::cameraStateRepoBuilder.mock()
    private fun cameraStateRepoBuilder(builder: BaseCameraPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<CameraPermission> = cameraStateRepoBuilderMock.call(builder, context)

    val createdContactsPermissionManagers = IsoMutableList<MockPermissionManager<ContactsPermission>>()
    val createContactsPermissionManagerMock = ::createContactsPermissionManager.mock()
    private fun createContactsPermissionManager(contactsPermission: ContactsPermission, repo: PermissionStateRepo<ContactsPermission>): PermissionManager<ContactsPermission> = createContactsPermissionManagerMock.call(contactsPermission, repo)
    private val contactsPMBuilder: BaseContactsPermissionManagerBuilder = object : BaseContactsPermissionManagerBuilder {
        override fun create(contactsPermission: ContactsPermission, repo: PermissionStateRepo<ContactsPermission>): PermissionManager<ContactsPermission> = createContactsPermissionManager(contactsPermission, repo)
    }

    val buildContactsStateRepos = IsoMutableList<PermissionStateRepo<ContactsPermission>>()
    val contactsStateRepoBuilderMock = ::contactsStateRepoBuilder.mock()
    private fun contactsStateRepoBuilder(contactsPermission: ContactsPermission, builder: BaseContactsPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<ContactsPermission> = contactsStateRepoBuilderMock.call(contactsPermission, builder, context)

    val createdMicrophonePermissionManagers = IsoMutableList<MockPermissionManager<MicrophonePermission>>()
    val createMicrophonePermissionManagerMock = ::createMicrophonePermissionManager.mock()
    private fun createMicrophonePermissionManager(repo: PermissionStateRepo<MicrophonePermission>): PermissionManager<MicrophonePermission> = createMicrophonePermissionManagerMock.call(repo)
    private val microphonePMBuilder: BaseMicrophonePermissionManagerBuilder = object : BaseMicrophonePermissionManagerBuilder {
        override fun create(repo: PermissionStateRepo<MicrophonePermission>): PermissionManager<MicrophonePermission> = createMicrophonePermissionManager(repo)
    }

    val buildMicrophoneStateRepos = IsoMutableList<PermissionStateRepo<MicrophonePermission>>()
    val microphoneStateRepoBuilderMock = ::microphoneStateRepoBuilder.mock()
    private fun microphoneStateRepoBuilder(builder: BaseMicrophonePermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<MicrophonePermission> = microphoneStateRepoBuilderMock.call(builder, context)

    val createdNotificationsPermissionManagers = IsoMutableList<MockPermissionManager<NotificationsPermission>>()
    val createNotificationsPermissionManagerMock = ::createNotificationsPermissionManager.mock()
    private fun createNotificationsPermissionManager(notificationsPermission: NotificationsPermission, repo: PermissionStateRepo<NotificationsPermission>): PermissionManager<NotificationsPermission> = createNotificationsPermissionManagerMock.call(notificationsPermission, repo)
    private val notificationsPMBuilder: BaseNotificationsPermissionManagerBuilder = object : BaseNotificationsPermissionManagerBuilder {
        override fun create(notificationsPermission: NotificationsPermission, repo: PermissionStateRepo<NotificationsPermission>): PermissionManager<NotificationsPermission> = createNotificationsPermissionManager(notificationsPermission, repo)
    }

    val buildNotificationsStateRepos = IsoMutableList<PermissionStateRepo<NotificationsPermission>>()
    val notificationsStateRepoBuilderMock = ::notificationsStateRepoBuilder.mock()
    private fun notificationsStateRepoBuilder(notificationsPermission: NotificationsPermission, builder: BaseNotificationsPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<NotificationsPermission> = notificationsStateRepoBuilderMock.call(notificationsPermission, builder, context)

    val createdBluetoothPermissionManagers = IsoMutableList<MockPermissionManager<BluetoothPermission>>()
    val createBluetoothPermissionManagerMock = ::createBluetoothPermissionManager.mock()
    private fun createBluetoothPermissionManager(repo: PermissionStateRepo<BluetoothPermission>): PermissionManager<BluetoothPermission> = createBluetoothPermissionManagerMock.call(repo)
    private val bluetoothPMBuilder: BaseBluetoothPermissionManagerBuilder = object : BaseBluetoothPermissionManagerBuilder {
        override fun create(repo: PermissionStateRepo<BluetoothPermission>): PermissionManager<BluetoothPermission> = createBluetoothPermissionManager(repo)
    }

    val buildBluetoothStateRepos = IsoMutableList<PermissionStateRepo<BluetoothPermission>>()
    val bluetoothStateRepoBuilderMock = ::bluetoothStateRepoBuilder.mock()
    private fun bluetoothStateRepoBuilder(builder: BaseBluetoothPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<BluetoothPermission> = bluetoothStateRepoBuilderMock.call(builder, context)

    val createdLocationPermissionManagers = IsoMutableList<MockPermissionManager<LocationPermission>>()
    val createLocationPermissionManagerMock = ::createLocationPermissionManager.mock()
    private fun createLocationPermissionManager(locationPermission: LocationPermission, repo: PermissionStateRepo<LocationPermission>): PermissionManager<LocationPermission> = createLocationPermissionManagerMock.call(locationPermission, repo)
    private val locationPMBuilder: BaseLocationPermissionManagerBuilder = object : BaseLocationPermissionManagerBuilder {
        override fun create(locationPermission: LocationPermission, repo: PermissionStateRepo<LocationPermission>): PermissionManager<LocationPermission> = createLocationPermissionManager(locationPermission, repo)
    }

    val buildLocationStateRepos = IsoMutableList<PermissionStateRepo<LocationPermission>>()
    val locationStateRepoBuilderMock = ::locationStateRepoBuilder.mock()
    private fun locationStateRepoBuilder(locationPermission: LocationPermission, builder: BaseLocationPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<LocationPermission> = locationStateRepoBuilderMock.call(locationPermission, builder, context)

    val createdCalendarPermissionManagers = IsoMutableList<MockPermissionManager<CalendarPermission>>()
    val createCalendarPermissionManagerMock = ::createCalendarPermissionManager.mock()
    private fun createCalendarPermissionManager(calendarPermission: CalendarPermission, repo: PermissionStateRepo<CalendarPermission>): PermissionManager<CalendarPermission> = createCalendarPermissionManagerMock.call(calendarPermission, repo)
    private val calendarPMBuilder: BaseCalendarPermissionManagerBuilder = object : BaseCalendarPermissionManagerBuilder {
        override fun create(calendarPermission: CalendarPermission, repo: PermissionStateRepo<CalendarPermission>): PermissionManager<CalendarPermission> = createCalendarPermissionManager(calendarPermission, repo)
    }

    val buildCalendarStateRepos = IsoMutableList<PermissionStateRepo<CalendarPermission>>()
    val calendarStateRepoBuilderMock = ::calendarStateRepoBuilder.mock()
    private fun calendarStateRepoBuilder(calendarPermission: CalendarPermission, builder: BaseCalendarPermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<CalendarPermission> = calendarStateRepoBuilderMock.call(calendarPermission, builder, context)

    val createdStoragePermissionManagers = IsoMutableList<MockPermissionManager<StoragePermission>>()
    val createStoragePermissionManagerMock = ::createStoragePermissionManager.mock()
    private fun createStoragePermissionManager(storagePermission: StoragePermission, repo: PermissionStateRepo<StoragePermission>): PermissionManager<StoragePermission> = createStoragePermissionManagerMock.call(storagePermission, repo)
    private val storagePMBuilder: BaseStoragePermissionManagerBuilder = object : BaseStoragePermissionManagerBuilder {
        override fun create(storagePermission: StoragePermission, repo: PermissionStateRepo<StoragePermission>): PermissionManager<StoragePermission> = createStoragePermissionManager(storagePermission, repo)
    }

    val buildStorageStateRepos = IsoMutableList<PermissionStateRepo<StoragePermission>>()
    val storageStateRepoBuilderMock = ::storageStateRepoBuilder.mock()
    private fun storageStateRepoBuilder(storagePermission: StoragePermission, builder: BaseStoragePermissionManagerBuilder, context: CoroutineContext): PermissionStateRepo<StoragePermission> = storageStateRepoBuilderMock.call(storagePermission, builder, context)

    init {
        if (setupMocks) {
            createBluetoothPermissionManagerMock.on().doExecute { (repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdBluetoothPermissionManagers.add(it)
                }
            }
            bluetoothStateRepoBuilderMock.on().doExecute { (builder, coroutineContext) ->
                BluetoothPermissionStateRepo(builder, coroutineContext = coroutineContext).also {
                    buildBluetoothStateRepos.add(it)
                }
            }

            createLocationPermissionManagerMock.on().doExecute { (_, repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdLocationPermissionManagers.add(it)
                }
            }
            locationStateRepoBuilderMock.on().doExecute { (permission, builder, coroutineContext) ->
                LocationPermissionStateRepo(permission, builder, coroutineContext = coroutineContext).also {
                    buildLocationStateRepos.add(it)
                }
            }

            createCalendarPermissionManagerMock.on().doExecute { (_, repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdCalendarPermissionManagers.add(it)
                }
            }
            calendarStateRepoBuilderMock.on().doExecute { (permission, builder, coroutineContext) ->
                CalendarPermissionStateRepo(permission, builder, coroutineContext = coroutineContext).also {
                    buildCalendarStateRepos.add(it)
                }
            }

            createStoragePermissionManagerMock.on().doExecute { (_, repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdStoragePermissionManagers.add(it)
                }
            }
            storageStateRepoBuilderMock.on().doExecute { (permission, builder, coroutineContext) ->
                StoragePermissionStateRepo(permission, builder, coroutineContext = coroutineContext).also {
                    buildStorageStateRepos.add(it)
                }
            }

            createCameraPermissionManagerMock.on().doExecute { (repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdCameraPermissionManagers.add(it)
                }
            }
            cameraStateRepoBuilderMock.on().doExecute { (builder, coroutineContext) ->
                CameraPermissionStateRepo(builder, coroutineContext = coroutineContext).also {
                    buildCameraStateRepos.add(it)
                }
            }

            createContactsPermissionManagerMock.on().doExecute { (_, repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdContactsPermissionManagers.add(it)
                }
            }
            contactsStateRepoBuilderMock.on().doExecute { (permission, builder, coroutineContext) ->
                ContactsPermissionStateRepo(permission, builder, coroutineContext = coroutineContext).also {
                    buildContactsStateRepos.add(it)
                }
            }

            createMicrophonePermissionManagerMock.on().doExecute { (repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdMicrophonePermissionManagers.add(it)
                }
            }
            microphoneStateRepoBuilderMock.on().doExecute { (builder, coroutineContext) ->
                MicrophonePermissionStateRepo(builder, coroutineContext = coroutineContext).also {
                    buildMicrophoneStateRepos.add(it)
                }
            }

            createNotificationsPermissionManagerMock.on().doExecute { (_, repo) ->
                MockPermissionManager(repo, initialPermissionState, monitoringInterval).also {
                    createdNotificationsPermissionManagers.add(it)
                }
            }
            notificationsStateRepoBuilderMock.on().doExecute { (permission, builder, coroutineContext) ->
                NotificationsPermissionStateRepo(permission, builder, coroutineContext = coroutineContext).also {
                    buildNotificationsStateRepos.add(it)
                }
            }
        }
    }

    fun registerAllPermissionsBuilders() {
        registerBluetoothPermission({ bluetoothPMBuilder }, ::bluetoothStateRepoBuilder)
        registerLocationPermission({ locationPMBuilder }, ::locationStateRepoBuilder)
        registerCalendarPermission({ calendarPMBuilder }, ::calendarStateRepoBuilder)
        registerStoragePermission({ storagePMBuilder }, ::storageStateRepoBuilder)
        registerCameraPermission({ cameraPMBuilder }, ::cameraStateRepoBuilder)
        registerContactsPermission({ contactsPMBuilder }, ::contactsStateRepoBuilder)
        registerMicrophonePermission({ microphonePMBuilder }, ::microphoneStateRepoBuilder)
        registerNotificationsPermission({ notificationsPMBuilder }, ::notificationsStateRepoBuilder)
    }
}
