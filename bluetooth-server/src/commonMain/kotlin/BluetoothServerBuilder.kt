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

import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.permissions.base.Permissions
import kotlin.coroutines.CoroutineContext

internal val defaultBluetoothServerDispatcher by lazy {
    singleThreadDispatcher("BluetoothServer")
}

/**
 * Builder class for creating a [BluetoothServer].
 */
interface BaseBluetoothServerBuilder {

    /**
     * Creates a [BluetoothServer]
     * @param settingsBuilder a method for getting the [ServerSettings] to be used while scanning from a [CoroutineContext]
     * @param coroutineContext the [CoroutineContext] in which Bluetooth runs
     * @param specs the [BluetoothServerDSL] to build the [BluetoothServer] from
     * @return the created [BluetoothServer]
     */
    suspend fun createServer(
        settingsBuilder: (Permissions) -> ServerSettings = { ServerSettings(permissions = it) },
        coroutineContext: CoroutineContext = defaultBluetoothServerDispatcher,
        specs: BluetoothServerDSL.() -> Unit,
    ): BluetoothServer
}

/**
 * A default implementation of [BaseBluetoothServerBuilder]
 */
expect class BluetoothServerBuilder : BaseBluetoothServerBuilder {
    override suspend fun createServer(settingsBuilder: (Permissions) -> ServerSettings, coroutineContext: CoroutineContext, specs: BluetoothServerDSL.() -> Unit): BluetoothServer
}