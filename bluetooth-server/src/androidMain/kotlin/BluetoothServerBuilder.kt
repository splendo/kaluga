/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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
package com.splendo.kaluga.bluetooth.server

import android.bluetooth.BluetoothManager
import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [com.splendo.kaluga.bluetooth.server.BaseBluetoothServerBuilder]
 * @param applicationContext the [android.content.Context] in which Bluetooth should run
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder] and [com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder] registered.
 * @param permissionsBuilder a method for creating the [com.splendo.kaluga.permissions.base.Permissions] object to manage the Bluetooth permissions.
 */
actual class BluetoothServerBuilder(
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(PermissionContext(applicationContext)).apply {
                registerBluetoothPermissionIfNotRegistered()
                registerLocationPermissionIfNotRegistered()
            },
            coroutineContext = context,
        )
    },
) : BaseBluetoothServerBuilder {

    actual override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer {
        val settings = settingsBuilder(permissionsBuilder(coroutineContext))
        val manager = applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
        val initialState = manager?.let {
            AndroidServerState.AwaitingPermissions(
                it,
                BluetoothMonitor.Builder(applicationContext, manager.adapter).create(),
                settings.permissions[BluetoothPermission(BluetoothPermission.Type.Server)] as BluetoothPermissionStateRepo,
                KalugaBluetoothGattServerCallback(settings.logger, coroutineContext),
                applicationContext,
                settings.logger,
            )
        } ?: ServerState.NotSupported

        return DefaultBluetoothServer.DSL(settings, initialState, coroutineContext).apply(specs).build()
    }
}
