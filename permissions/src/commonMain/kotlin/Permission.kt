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

import co.touchlab.stately.collections.IsoMutableMap
import com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.calendar.BaseCalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationOptions
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest
import kotlin.coroutines.CoroutineContext

/**
 * Permissions that can be requested by Kaluga
 */
sealed class Permission {
    /**
     * Permission to access the Bluetooth scanner
     */
    object Bluetooth : Permission()

    /**
     * Permission to access the users Calendar
     * @param allowWrite If `true` writing to the calendar is permitted
     */
    data class Calendar(val allowWrite: Boolean = false) : Permission()

    /**
     * Permission to access the users Camera
     */
    object Camera : Permission()
    /**
     * Permission to access the users Contacts
     * @param allowWrite If `true` writing to the contacts is permitted
     */
    data class Contacts(val allowWrite: Boolean = false) : Permission()

    /**
     * Permission to access the users Location
     * @param background If `true` scanning for location in the background is permitted
     * @param precise If `true` precise location scanning is permitted
     */
    data class Location(val background: Boolean = false, val precise: Boolean = false) : Permission()

    /**
     * Permission to access the users Microphone
     */
    object Microphone : Permission()

    /**
     * Permission to access the users Notifications.
     * @param options The [NotificationOptions] determining the type of notifications that can be accessed
     */
    data class Notifications(val options: NotificationOptions? = null) : Permission()

    /**
     * Permission to access the users device storage.
     * On iOS this corresponds to the Photos permission
     * @param allowWrite If `true` writing to the storage is permitted
     */
    data class Storage(val allowWrite: Boolean = false) : Permission()
}

interface BasePermissionsBuilder {
    val bluetoothPMBuilder: BaseBluetoothPermissionManagerBuilder
    val calendarPMBuilder: BaseCalendarPermissionManagerBuilder
    val cameraPMBuilder: BaseCameraPermissionManagerBuilder
    val contactsPMBuilder: BaseContactsPermissionManagerBuilder
    val locationPMBuilder: BaseLocationPermissionManagerBuilder
    val microphonePMBuilder: BaseMicrophonePermissionManagerBuilder
    val notificationsPMBuilder: BaseNotificationsPermissionManagerBuilder
    val storagePMBuilder: BaseStoragePermissionManagerBuilder
}

/**
 * Builder for providing the proper [PermissionManager] for each [Permission]
 */
expect class PermissionsBuilder : BasePermissionsBuilder

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [BasePermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(private val builder: BasePermissionsBuilder, private val coroutineContext: CoroutineContext = Dispatchers.Main) {

    private val permissionStateRepos: IsoMutableMap<Permission, PermissionStateRepo<*>> = IsoMutableMap()

    private fun <P : Permission> permissionStateRepo(permission: P) =
        permissionStateRepos.get(permission) ?: createPermissionStateRepo(permission, coroutineContext).also { permissionStateRepos[permission] = it }

    /**
     * Gets a [Flow] of [PermissionState] for a given [Permission]
     * @param permission The [Permission] for which the [PermissionState] flow should be provided
     * @return A [Flow] of [PermissionState] for the given [Permission]
     */
    operator fun <P : Permission> get(permission: P): Flow<PermissionState<out Permission>> {
        return permissionStateRepo(permission)
    }

    /**
     * Gets a the of [PermissionManager] for a given [Permission]
     * @param permission The [Permission] for which the [PermissionManager] should be returned
     * @return The [PermissionManager] for the given [Permission]
     */
    fun <P : Permission> getManager(permission: P): PermissionManager<out Permission> {
        return permissionStateRepo(permission).permissionManager
    }

    /**
     * Requests a [Permission]
     * @return `true` if the permission was granted, `false` otherwise.
     */
    suspend fun <P : Permission> request(p: P): Boolean {
        return get(p).request(getManager(p))
    }

    private fun createPermissionStateRepo(permission: Permission, coroutineContext: CoroutineContext = Dispatchers.Main): PermissionStateRepo<*> {
        return when (permission) {
            is Permission.Bluetooth -> BluetoothPermissionStateRepo(builder.bluetoothPMBuilder, coroutineContext)
            is Permission.Calendar -> CalendarPermissionStateRepo(permission, builder.calendarPMBuilder, coroutineContext)
            is Permission.Camera -> CameraPermissionStateRepo(builder.cameraPMBuilder, coroutineContext)
            is Permission.Contacts -> ContactsPermissionStateRepo(permission, builder.contactsPMBuilder, coroutineContext)
            is Permission.Location -> LocationPermissionStateRepo(permission, builder.locationPMBuilder, coroutineContext)
            is Permission.Microphone -> MicrophonePermissionStateRepo(builder.microphonePMBuilder, coroutineContext)
            is Permission.Notifications -> NotificationsPermissionStateRepo(permission, builder.notificationsPMBuilder, coroutineContext)
            is Permission.Storage -> StoragePermissionStateRepo(permission, builder.storagePMBuilder, coroutineContext)
        }
    }

    fun clean() {
        permissionStateRepos.values.forEach { it.cancel() }
        permissionStateRepos.clear()
    }
}

/**
 * Requests a [Permission] on a [Flow] of [PermissionState]
 * @return `true` if the permission was granted, `false` otherwise.
 */
suspend fun <P : Permission> Flow<PermissionState<out P>>.request(permissionManager: PermissionManager<out P>): Boolean {
    return this.transformLatest { state ->
        when (state) {
            is PermissionState.Allowed -> emit(true)
            is PermissionState.Denied.Requestable -> state.request(permissionManager)
            is PermissionState.Denied.Locked -> emit(false)
            is PermissionState.Unknown -> {}
        }
    }.first()
}
