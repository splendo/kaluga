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

import android.content.Context
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A default implementation of [BaseBluetoothClientBuilder]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder] and [com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder] registered.
 * @param scannerBuilder the [BaseScanner.Builder] for creating the [BaseScanner] to handle scanning
 */
actual class BluetoothClientBuilder(
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = defaultPermissionsBuilder(applicationContext),
    private val scannerBuilder: BaseScanner.Builder = DefaultScanner.Builder(applicationContext = applicationContext),
) : BaseBluetoothClientBuilder {

    /**
     * Constructor that creates a [BaseBluetoothClientBuilder] with a [DefaultScanner]
     * @param applicationContext the [Context] in which Bluetooth should run
     * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
     * Needs to have [com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder] and [com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder] registered.
     * @param gattErrorDisconnectGrace how long a request the stack fails with `GATT_ERROR` (0x85) waits for the device to
     * disconnect, answering [GattResponse.DeviceUnavailable] if it does; see [DefaultScanner.Builder]
     */
    constructor(
        applicationContext: Context = ApplicationHolder.applicationContext,
        permissionsBuilder: suspend (CoroutineContext) -> Permissions = defaultPermissionsBuilder(applicationContext),
        gattErrorDisconnectGrace: Duration,
    ) : this(applicationContext, permissionsBuilder, DefaultScanner.Builder(applicationContext = applicationContext, gattErrorDisconnectGrace = gattErrorDisconnectGrace))

    actual override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient = DefaultBluetoothClient(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        scannerBuilder,
        coroutineContext,
    )
}

private fun defaultPermissionsBuilder(applicationContext: Context): suspend (CoroutineContext) -> Permissions = { context ->
    Permissions(
        PermissionsBuilder(PermissionContext(applicationContext)).apply {
            registerBluetoothPermissionIfNotRegistered()
            registerLocationPermissionIfNotRegistered()
        },
        coroutineContext = context,
    )
}
