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
 * [Permission] to access the Bluetooth scanner
 */
data object BluetoothPermission : Permission() {
    override val name: String = "Bluetooth"
}

/**
 * Registers a [BaseBluetoothPermissionManagerBuilder] and [PermissionStateRepo] for [BluetoothPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param bluetoothPermissionManagerBuilderBuilder method for creating a [BaseBluetoothPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseBluetoothPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseBluetoothPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerBluetoothPermission(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerBluetoothPermission(bluetoothPermissionManagerBuilderBuilder) { baseBluetoothPermissionManagerBuilder, coroutineContext ->
    BluetoothPermissionStateRepo(
        baseBluetoothPermissionManagerBuilder,
        monitoringInterval,
        settings,
        coroutineContext,
    )
}

/**
 * Registers a [BaseBluetoothPermissionManagerBuilder] and [PermissionStateRepo] for [BluetoothPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * Only one builder can be registered.
 * @param bluetoothPermissionManagerBuilderBuilder method for creating a [BaseBluetoothPermissionManagerBuilder] from a [PermissionContext]
 * @param stateRepoBuilder method for creating a [PermissionStateRepo] for [BluetoothPermission] given a [BaseBluetoothPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseBluetoothPermissionManagerBuilder] registered
 * @throws [com.splendo.kaluga.permissions.base.PermissionsBuilderError] if either the [BaseBluetoothPermissionManagerBuilder] or [PermissionStateRepo] have already been registered
 */
fun PermissionsBuilder.registerBluetoothPermission(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    stateRepoBuilder: (BaseBluetoothPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<BluetoothPermission>,
) = bluetoothPermissionManagerBuilderBuilder(context).also {
    register(it)
    registerPermissionStateRepoBuilder<BluetoothPermission> { _, coroutineContext ->
        stateRepoBuilder(it, coroutineContext)
    }
}

/**
 * Gets the [BaseBluetoothPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseBluetoothPermissionManagerBuilder] and [PermissionStateRepo] for [BluetoothPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param bluetoothPermissionManagerBuilderBuilder method for creating a [BaseBluetoothPermissionManagerBuilder] from a [PermissionContext]
 * @param monitoringInterval the [Duration] after which the system should poll for changes to the permission if automatic detection is impossible.
 * @param settings the [BasePermissionManager.Settings] to apply to any [BasePermissionManager] created using the registered builders.
 * @return the [BaseBluetoothPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerBluetoothPermissionIfNotRegistered(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    monitoringInterval: Duration = PermissionStateRepo.defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
) = registerBluetoothPermissionIfNotRegistered(bluetoothPermissionManagerBuilderBuilder) { baseBluetoothPermissionManagerBuilder, coroutineContext ->
    BluetoothPermissionStateRepo(
        baseBluetoothPermissionManagerBuilder,
        monitoringInterval,
        settings,
        coroutineContext,
    )
}

/**
 * Gets the [BaseBluetoothPermissionManagerBuilder] registered
 * If not yet registered, this will register a [BaseBluetoothPermissionManagerBuilder] and [PermissionStateRepo] for [BluetoothPermission] to the [PermissionsBuilder.register] and [PermissionsBuilder.registerPermissionStateRepoBuilder] respectively
 * @param bluetoothPermissionManagerBuilderBuilder method for creating a [BaseBluetoothPermissionManagerBuilder] from a [PermissionContext]
 * @param stateRepoBuilder method for creating a [PermissionStateRepo] for [BluetoothPermission] given a [BaseBluetoothPermissionManagerBuilder] and [CoroutineContext]
 * @return the [BaseBluetoothPermissionManagerBuilder] registered
 */
fun PermissionsBuilder.registerBluetoothPermissionIfNotRegistered(
    bluetoothPermissionManagerBuilderBuilder: (PermissionContext) -> BaseBluetoothPermissionManagerBuilder = ::BluetoothPermissionManagerBuilder,
    stateRepoBuilder: (BaseBluetoothPermissionManagerBuilder, CoroutineContext) -> PermissionStateRepo<BluetoothPermission>,
) = bluetoothPermissionManagerBuilderBuilder(context).also {
    registerOrGet(it)
    registerOrGetPermissionStateRepoBuilder<BluetoothPermission> { _, coroutineContext ->
        stateRepoBuilder(it, coroutineContext)
    }
}
