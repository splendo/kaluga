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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.DefaultScanner
import com.splendo.kaluga.bluetooth.scanner.WebDevicePickerSettings
import com.splendo.kaluga.bluetooth.scanner.defaultNotSupportedDisconnectGrace
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A default implementation of [BaseBluetoothClientBuilder]
 * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
 * Needs to have [com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder] registered.
 * @param scannerBuilder the [BaseScanner.Builder] for creating the [BaseScanner] to handle scanning
 */
actual class BluetoothClientBuilder(
    private val permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
        Permissions(
            PermissionsBuilder().apply {
                registerBluetoothPermissionIfNotRegistered()
            },
            context,
        )
    },
    private val scannerBuilder: BaseScanner.Builder = DefaultScanner.Builder(),
) : BaseBluetoothClientBuilder {

    /**
     * Constructor that creates a [BaseBluetoothClientBuilder] with a [DefaultScanner]
     * @param permissionsBuilder a method for creating the [Permissions] object to manage the Bluetooth permissions.
     * Needs to have [com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder] registered.
     * @param optionalServices the advertisement-independent service access allowlist passed to `requestDevice`.
     * Services that are not advertised (or only available after connecting) must be listed here to be reachable.
     * @param pickerSettings the [WebDevicePickerSettings] configuring the "Add Device" overlay
     * @param notSupportedDisconnectGrace how long a request rejected with `NotSupportedError` waits for the device to
     * disconnect, answering [GattResponse.DeviceUnavailable] if it does; see [DefaultScanner.Builder]
     */
    constructor(
        permissionsBuilder: suspend (CoroutineContext) -> Permissions = { context ->
            Permissions(
                PermissionsBuilder().apply {
                    registerBluetoothPermissionIfNotRegistered()
                },
                context,
            )
        },
        optionalServices: List<UUID>,
        pickerSettings: WebDevicePickerSettings = WebDevicePickerSettings(),
        notSupportedDisconnectGrace: Duration = defaultNotSupportedDisconnectGrace,
    ) : this(permissionsBuilder, DefaultScanner.Builder(optionalServices, pickerSettings, notSupportedDisconnectGrace))

    actual override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient = DefaultBluetoothClient(
        { scannerContext ->
            scannerSettingsBuilder(permissionsBuilder(scannerContext))
        },
        scannerBuilder,
        coroutineContext,
    )
}
