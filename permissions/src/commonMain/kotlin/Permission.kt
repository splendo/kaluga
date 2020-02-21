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

import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.calendar.CalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.camera.CameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.ContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.storage.StoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest

sealed class Permission {
    object Bluetooth : Permission()
    data class Calendar(val allowWrite: Boolean) : Permission()
    object Camera : Permission()
    data class Contacts(val allowWrite: Boolean) : Permission()
    data class Location(val background: Boolean, val precise: Boolean) : Permission()
    object Microphone : Permission()
    data class Storage(val allowWrite: Boolean) : Permission()
}

interface BasePermissionsBuilder {

    val bluetoothPMBuilder: BluetoothPermissionManagerBuilder
    val calendarPMBuilder: CalendarPermissionManagerBuilder
    val cameraPMBuilder: CameraPermissionManagerBuilder
    val contactsPMBuilder: ContactsPermissionManagerBuilder
    val locationPMBuilder: LocationPermissionManagerBuilder
    val microphonePMBuilder: MicrophonePermissionManagerBuilder
    val storagePMBuilder: StoragePermissionManagerBuilder

}

expect class PermissionsBuilder : BasePermissionsBuilder

class Permissions(private val builder: PermissionsBuilder) {

    private val permissionStateRepos: MutableMap<Permission, PermissionStateRepo<*>> = mutableMapOf()

    operator fun get(permission: Permission): Flow<PermissionState<*>> {
        val permissionStateRepo = permissionStateRepos[permission] ?: createPermissionStateRepo(permission).also { permissionStateRepos[permission] = it }
        return permissionStateRepo.flow()
    }

    private fun createPermissionStateRepo(permission: Permission): PermissionStateRepo<*> {
        return when (permission) {
            is Permission.Bluetooth -> BluetoothPermissionStateRepo(builder.bluetoothPMBuilder)
            is Permission.Calendar -> CalendarPermissionStateRepo(permission, builder.calendarPMBuilder)
            is Permission.Camera -> CameraPermissionStateRepo(builder.cameraPMBuilder)
            is Permission.Contacts -> ContactsPermissionStateRepo(permission, builder.contactsPMBuilder)
            is Permission.Location -> LocationPermissionStateRepo(permission, builder.locationPMBuilder)
            is Permission.Microphone -> MicrophonePermissionStateRepo(builder.microphonePMBuilder)
            is Permission.Storage -> StoragePermissionStateRepo(permission, builder.storagePMBuilder)
        }
    }

}

suspend fun Flow<PermissionState<*>>.request() : Boolean {
    return this.transformLatest { state ->
        when (state) {
            is PermissionState.Allowed -> emit(true)
            is PermissionState.Denied.Requestable -> state.request()
            is PermissionState.Denied.SystemLocked -> emit(false)
        }
    }.first()
}