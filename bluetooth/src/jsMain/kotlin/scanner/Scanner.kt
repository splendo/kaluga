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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.scanner.BaseScanner.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Default implementation of [BaseScanner]
 * @param settings the [Settings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
 */
actual class DefaultScanner(
    settings: Settings,
    coroutineScope: CoroutineScope,
    scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
) : BaseScanner(settings, coroutineScope, scanningDispatcher) {

    /**
     * Builder for creating a [DefaultScanner]
     */
    class Builder : BaseScanner.Builder {

        override fun create(settings: Settings, coroutineScope: CoroutineScope, scanningDispatcher: CoroutineDispatcher): BaseScanner {
            return DefaultScanner(settings, coroutineScope, scanningDispatcher)
        }
    }

    override val isSupported: Boolean = false
    override val bluetoothEnabledMonitor: BluetoothMonitor = BluetoothMonitor.Builder().create()

    override suspend fun didStartScanning(filter: Filter) {
        TODO("Not yet implemented")
    }

    override suspend fun didStopScanning() {
        TODO("Not yet implemented")
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> = emptyList()

    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered> {
        TODO("Not yet implemented")
    }
}
