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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest

sealed class Permission {
    object Bluetooth : Permission()
    object Calendar : Permission()
    object Camera : Permission()
    object Contacts : Permission()
    data class Location(val background: Boolean) : Permission()
    object Microphone : Permission()
    object Notifications : Permission()
    object Storage : Permission()
}

class Permissions(private val bluetoothPMBuilder: BluetoothPermissionManagerBuilder) {

    private val bluetoothPermissionStateRepo = BluetoothPermissionStateRepo(bluetoothPMBuilder)

    operator fun get(permission: Permission): Flow<PermissionState<*>> {
        return when (permission) {
            is Permission.Bluetooth -> bluetoothPermissionStateRepo.flow()
            else -> throw NotImplementedError()
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