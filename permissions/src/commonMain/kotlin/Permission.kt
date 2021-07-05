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
import com.splendo.kaluga.permissions.calendar.BaseCalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
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
typealias RepoFactory = (permission: Permission, coroutineContext: CoroutineContext) -> PermissionStateRepo<*>
open class PermissionsBuilder {

    private val builders = IsoMutableMap<KClassifier, BasePermissionsBuilder>()

    fun <T : BasePermissionsBuilder> register(builder: T, permission: KClassifier) : T {
        builders[permission] = builder
        return builder
    }

    operator fun get(permission: Permission) : BasePermissionsBuilder =
        builders[permission::class] ?: throw Error("The Builder for $permission was not registered")

    // FIXME: Repos factory probably should move somewhere else. Refactor it after separation
    private val repoFactories = IsoMutableMap<KClassifier, RepoFactory>()
    fun registerRepoFactory(permission: KClassifier, repoFactory: RepoFactory) {
        repoFactories[permission] = repoFactory
    }

    fun createPermissionStateRepo(permission: Permission, coroutineContext: CoroutineContext) : PermissionStateRepo<*> =
        repoFactories[permission::class]?.let { it(permission, coroutineContext)  } ?: throw Error("Permission state repo factory was not registered for $permission")
}

// ********************** Calendar *****************
fun PermissionsBuilder.registerCalendarPermission() =
    registerCalendarPermissionBuilder().also { builder ->
        registerRepoFactory(CalendarPermission::class) { permission, coroutineContext ->
            CalendarPermissionStateRepo(permission as CalendarPermission, builder as BaseCalendarPermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerCalendarPermissionBuilder() : CalendarPermissionManagerBuilder

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

// ********************** Location *****************
fun PermissionsBuilder.registerLocationPermission()  =
    registerLocationPermissionBuilder().also { builder ->
        registerRepoFactory(LocationPermission::class) { permission, coroutineContext ->
            LocationPermissionStateRepo(permission as LocationPermission, builder as BaseLocationPermissionManagerBuilder, coroutineContext)
        }
    }

internal expect fun PermissionsBuilder.registerLocationPermissionBuilder() : LocationPermissionManagerBuilder

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

/**
 * Manager to request the [PermissionStateRepo] of a given [Permission]
 * @param builder The [PermissionsBuilder] to build the [PermissionManager] associated with each [Permission]
 * @param coroutineContext The [CoroutineContext] to run permission checks from
 */
class Permissions(private val builder: PermissionsBuilder, private val coroutineContext: CoroutineContext = Dispatchers.Main) {

    private val permissionStateRepos: IsoMutableMap<Permission, PermissionStateRepo<*>> = IsoMutableMap()

    private fun <P : Permission> permissionStateRepo(permission: P) =
        permissionStateRepos[permission] ?: builder.createPermissionStateRepo(permission, coroutineContext).also { permissionStateRepos[permission] = it }

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
