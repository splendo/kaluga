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

import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [com.splendo.kaluga.bluetooth.BaseBluetoothServerBuilder]
 * @param bundle the [NSBundle] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 */
actual class BluetoothServerBuilder(
    private val bundle: NSBundle = NSBundle.Companion.mainBundle,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(bundle).apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
) : BaseBluetoothServerBuilder {

    actual override suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings,
        coroutineContext: CoroutineContext,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer {
        val settings = settingsBuilder(permissionsBuilder(coroutineContext))
        val initialState = IOSServerState.AwaitingPermissions(
            settings.permissions[BluetoothPermission(BluetoothPermission.Type.Server)] as BluetoothPermissionStateRepo,
            KalugaCBPeripheralManagerDelegate(settings.logger, coroutineContext),
            settings.logger,
        )
        return BluetoothServer.DSL(settings, initialState, coroutineContext).apply(specs).build()
    }
}