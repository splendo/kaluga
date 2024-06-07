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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.permissions.base.BasePermissionStateRepo
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.notifications.NotificationsPermission
import com.splendo.kaluga.permissions.storage.StoragePermission
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import kotlin.coroutines.CoroutineContext

expect val mockPermissionContext: PermissionContext

class MockBasePermissionsBuilder<P : Permission> : BasePermissionsBuilder<P>

/**
 * Mock implementation of [PermissionsBuilder]
 * @param initialPermissionState The initial [MockPermissionManager.MockPermissionState] of all [MockPermissionManager]
 * @param monitoringInterval The interval between monitoring for permission changes
 * @param setupMocks If `true` automatically sets up all permission managers.
 */
class MockPermissionsBuilder(
    initialActiveState: MockPermissionState.ActiveState = MockPermissionState.ActiveState.ALLOWED,
    setupMocks: Boolean = true,
) : PermissionsBuilder(mockPermissionContext) {

    /**
     * List of created [PermissionStateRepo] for [CameraPermission]
     */
    val buildCameraStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<CameraPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [CameraPermission]
     */
    val cameraStateRepoBuilderMock = ::cameraStateRepoBuilder.mock()
    private fun cameraStateRepoBuilder(cameraPermission: CameraPermission, context: CoroutineContext): BasePermissionStateRepo<CameraPermission> =
        cameraStateRepoBuilderMock.call(cameraPermission, context)

    /**
     * List of created [PermissionStateRepo] for [ContactsPermission]
     */
    val buildContactsStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<ContactsPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [ContactsPermission]
     */
    val contactsStateRepoBuilderMock = ::contactsStateRepoBuilder.mock()
    private fun contactsStateRepoBuilder(contactsPermission: ContactsPermission, context: CoroutineContext): BasePermissionStateRepo<ContactsPermission> =
        contactsStateRepoBuilderMock.call(contactsPermission, context)

    /**
     * List of created [PermissionStateRepo] for [MicrophonePermission]
     */
    val buildMicrophoneStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<MicrophonePermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [MicrophonePermission]
     */
    val microphoneStateRepoBuilderMock = ::microphoneStateRepoBuilder.mock()
    private fun microphoneStateRepoBuilder(microphonePermission: MicrophonePermission, context: CoroutineContext): BasePermissionStateRepo<MicrophonePermission> =
        microphoneStateRepoBuilderMock.call(microphonePermission, context)

    /**
     * List of created [PermissionStateRepo] for [NotificationsPermission]
     */
    val buildNotificationsStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<NotificationsPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [NotificationsPermission]
     */
    val notificationsStateRepoBuilderMock = ::notificationsStateRepoBuilder.mock()
    private fun notificationsStateRepoBuilder(notificationsPermission: NotificationsPermission, context: CoroutineContext): BasePermissionStateRepo<NotificationsPermission> =
        notificationsStateRepoBuilderMock.call(notificationsPermission, context)

    /**
     * List of created [PermissionStateRepo] for [BluetoothPermission]
     */
    val buildBluetoothStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<BluetoothPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [BluetoothPermission]
     */
    val bluetoothStateRepoBuilderMock = ::bluetoothStateRepoBuilder.mock()
    private fun bluetoothStateRepoBuilder(bluetoothPermission: BluetoothPermission, context: CoroutineContext): BasePermissionStateRepo<BluetoothPermission> =
        bluetoothStateRepoBuilderMock.call(bluetoothPermission, context)

    /**
     * List of created [PermissionStateRepo] for [LocationPermission]
     */
    val buildLocationStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<LocationPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [LocationPermission]
     */
    val locationStateRepoBuilderMock = ::locationStateRepoBuilder.mock()
    private fun locationStateRepoBuilder(locationPermission: LocationPermission, context: CoroutineContext): BasePermissionStateRepo<LocationPermission> =
        locationStateRepoBuilderMock.call(locationPermission, context)

    /**
     * List of created [PermissionStateRepo] for [CalendarPermission]
     */
    val buildCalendarStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<CalendarPermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [CalendarPermission]
     */
    val calendarStateRepoBuilderMock = ::calendarStateRepoBuilder.mock()
    private fun calendarStateRepoBuilder(calendarPermission: CalendarPermission, context: CoroutineContext): BasePermissionStateRepo<CalendarPermission> =
        calendarStateRepoBuilderMock.call(calendarPermission, context)

    /**
     * List of created [PermissionStateRepo] for [StoragePermission]
     */
    val buildStorageStateRepos = concurrentMutableListOf<MockBasePermissionStateRepo<StoragePermission>>()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for creating [PermissionStateRepo] for [StoragePermission]
     */
    val storageStateRepoBuilderMock = ::storageStateRepoBuilder.mock()
    private fun storageStateRepoBuilder(storagePermission: StoragePermission, context: CoroutineContext): BasePermissionStateRepo<StoragePermission> =
        storageStateRepoBuilderMock.call(storagePermission, context)

    init {
        if (setupMocks) {
            bluetoothStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildBluetoothStateRepos.add(it)
                }
            }

            locationStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildLocationStateRepos.add(it)
                }
            }

            calendarStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildCalendarStateRepos.add(it)
                }
            }

            storageStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildStorageStateRepos.add(it)
                }
            }

            cameraStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildCameraStateRepos.add(it)
                }
            }

            contactsStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildContactsStateRepos.add(it)
                }
            }

            microphoneStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildMicrophoneStateRepos.add(it)
                }
            }

            notificationsStateRepoBuilderMock.on().doExecute { (permission, coroutineContext) ->
                MockBasePermissionStateRepo(permission, { MockPermissionState.Uninitialized(initialActiveState) }, setupMocks, coroutineContext).also {
                    buildNotificationsStateRepos.add(it)
                }
            }
        }
    }

    /**
     * Registers all [MockPermissionManager] and [MockPermissionStateRepo]
     */
    suspend fun registerAllPermissionsBuilders() {
        register(MockBasePermissionsBuilder<BluetoothPermission>())
        registerPermissionStateRepoBuilder(::bluetoothStateRepoBuilder)
        register(MockBasePermissionsBuilder<LocationPermission>())
        registerPermissionStateRepoBuilder(::locationStateRepoBuilder)
        register(MockBasePermissionsBuilder<CalendarPermission>())
        registerPermissionStateRepoBuilder(::calendarStateRepoBuilder)
        register(MockBasePermissionsBuilder<StoragePermission>())
        registerPermissionStateRepoBuilder(::storageStateRepoBuilder)
        register(MockBasePermissionsBuilder<CameraPermission>())
        registerPermissionStateRepoBuilder(::cameraStateRepoBuilder)
        register(MockBasePermissionsBuilder<ContactsPermission>())
        registerPermissionStateRepoBuilder(::contactsStateRepoBuilder)
        register(MockBasePermissionsBuilder<MicrophonePermission>())
        registerPermissionStateRepoBuilder(::microphoneStateRepoBuilder)
        register(MockBasePermissionsBuilder<NotificationsPermission>())
        registerPermissionStateRepoBuilder(::notificationsStateRepoBuilder)
    }
}
