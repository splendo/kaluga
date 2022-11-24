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

package com.splendo.kaluga.permissions.bluetooth

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * Permission to access the Bluetooth scanner
 */
object BluetoothPermission : Permission() {
    override val name: String = "Bluetooth"
}

fun PermissionsBuilder.registerBluetoothPermission(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings()
) =
    registerBluetoothPermission(bluetoothPermissionManagerBuilderBuilder) { baseBluetoothPermissionManagerBuilder, coroutineContext ->
        BluetoothPermissionStateRepo(
            baseBluetoothPermissionManagerBuilder,
            monitoringInterval,
            settings,
            coroutineContext
        )
    }

fun PermissionsBuilder.registerBluetoothPermission(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    stateRepoBuilder: (BaseBluetoothPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<BluetoothPermission>
) = bluetoothPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<BluetoothPermission> { _, coroutineContext ->
        stateRepoBuilder(it, coroutineContext)
    }
}
