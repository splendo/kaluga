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
import com.splendo.kaluga.bluetooth.server.BaseBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerDSL
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.permissions.base.Permissions
import kotlin.coroutines.CoroutineContext

/**
 * Builder class for creating both [BluetoothClient] (client) and [BluetoothServer].
 */
interface BaseBluetoothBuilder :
    BaseBluetoothClientBuilder,
    BaseBluetoothServerBuilder {

    @Deprecated("Use createClient instead", replaceWith = ReplaceWith("createClient(scannerSettingsBuilder, coroutineContext)"))
    fun create(
        scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings = { BaseScanner.Settings(it) },
        coroutineContext: CoroutineContext = defaultBluetoothClientDispatcher,
    ) = createClient(scannerSettingsBuilder, coroutineContext)
}

/**
 * A default implementation of [BaseBluetoothBuilder]
 */
expect class BluetoothBuilder : BaseBluetoothBuilder {
    override fun createClient(scannerSettingsBuilder: (Permissions) -> BaseScanner.Settings, coroutineContext: CoroutineContext): BluetoothClient
    override suspend fun createServer(settingsBuilder: (Permissions) -> ServerSettings, coroutineContext: CoroutineContext, specs: BluetoothServerDSL.() -> Unit): BluetoothServer
}

@Deprecated("Renamed to BluetoothClient", ReplaceWith("BluetoothClient"))
typealias Bluetooth = BluetoothClient
