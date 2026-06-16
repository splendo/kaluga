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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import platform.Foundation.NSBundle
import kotlin.coroutines.CoroutineContext

/**
 * A default implementation of [BaseBluetoothClientBuilder]
 * @param bundle the [NSBundle] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * @param scannerBuilder the [BaseScanner.Builder] for creating the [BaseScanner] to handle scanning
 */
actual class BluetoothClientBuilder(
    private val bundle: NSBundle = NSBundle.mainBundle,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder(bundle).apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
    private val scannerBuilder: DefaultScanner.Builder = DefaultScanner.Builder(),
) : BaseBluetoothClientBuilder {

    actual override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient = DefaultBluetoothClient(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        scannerBuilder,
        coroutineContext,
    )
}
