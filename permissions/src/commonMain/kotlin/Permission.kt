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
import kotlin.reflect.KClassifier

/**
 * Permissions that can be requested by Kaluga
 */
abstract class Permission

/**
 * Permission to access the Bluetooth scanner
 */
object BluetoothPermission : Permission()

/**
 * Permission to access the users Calendar
 * @param allowWrite If `true` writing to the calendar is permitted
 */
data class CalendarPermission(val allowWrite: Boolean = false) : Permission()

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

interface BasePermissionsBuilder
/**
 * Builder for providing the proper [PermissionManager] for each [Permission]
 */
class PermissionsBuilder {
    private val builders = mutableMapOf<KClassifier, BasePermissionsBuilder>()

    fun register(builder: BasePermissionsBuilder, permission: KClassifier) {
        builders[permission] = builder
    }

    operator fun get(permission: Permission) : BasePermissionsBuilder =
        builders[permission::class] ?: throw Error("The Builder for $permission was not registered")
}

expect fun PermissionsBuilder.registerBluetoothBuilder()
expect fun PermissionsBuilder.registerCalendarPermissionBuilder()
expect fun PermissionsBuilder.registerCameraPermissionBuilder()
expect fun PermissionsBuilder.registerContactsPermissionBuilder()
expect fun PermissionsBuilder.registerLocationPermissionBuilder()
expect fun PermissionsBuilder.registerMicrophonePermissionBuilder()
expect fun PermissionsBuilder.registerNotificationsPermissionBuilder()
expect fun PermissionsBuilder.registerStoragePermissionBuilder()

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [PermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(private val builder: PermissionsBuilder, private val coroutineContext: CoroutineContext = Dispatchers.Main) {

    private val permissionStateRepos: IsoMutableMap<Permission, PermissionStateRepo<*>> = IsoMutableMap()

    private fun <P : Permission> permissionStateRepo(permission: P) =
        permissionStateRepos[permission] ?: createPermissionStateRepo(permission, coroutineContext).also { permissionStateRepos[permission] = it }

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
            is BluetoothPermission -> BluetoothPermissionStateRepo(builder[permission] as BaseBluetoothPermissionManagerBuilder, coroutineContext)
            is CalendarPermission -> CalendarPermissionStateRepo(permission, builder[permission] as BaseCalendarPermissionManagerBuilder, coroutineContext)
            is CameraPermission -> CameraPermissionStateRepo(builder[permission] as BaseCameraPermissionManagerBuilder, coroutineContext)
            is ContactsPermission -> ContactsPermissionStateRepo(permission, builder[permission] as BaseContactsPermissionManagerBuilder, coroutineContext)
            is LocationPermission -> LocationPermissionStateRepo(permission, builder[permission] as BaseLocationPermissionManagerBuilder, coroutineContext)
            is MicrophonePermission -> MicrophonePermissionStateRepo(builder[permission] as BaseMicrophonePermissionManagerBuilder, coroutineContext)
            is NotificationsPermission -> NotificationsPermissionStateRepo(permission, builder[permission] as BaseNotificationsPermissionManagerBuilder, coroutineContext)
            is StoragePermission -> StoragePermissionStateRepo(permission, builder[permission] as BaseStoragePermissionManagerBuilder, coroutineContext)
            else -> throw Error("the permission $permission is not supported yet")
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
