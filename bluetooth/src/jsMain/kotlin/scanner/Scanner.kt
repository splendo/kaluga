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
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.scanner.BaseScanner.Settings
import com.splendo.kaluga.service.DefaultServiceMonitor
import kotlinx.coroutines.CoroutineScope

/**
 * Default implementation of [BaseScanner]
 * @param settings the [Settings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 */
actual class DefaultScanner(
    settings: Settings,
    coroutineScope: CoroutineScope
) : BaseScanner(settings, coroutineScope) {

    /**
     * Builder for creating a [DefaultScanner]
     */
    class Builder : BaseScanner.Builder {

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope,
        ): BaseScanner {
            return DefaultScanner(settings, coroutineScope)
        }
    }

    override val isSupported: Boolean = false
    override val bluetoothEnabledMonitor: DefaultServiceMonitor = BluetoothMonitor.Builder().create(coroutineContext)

    override suspend fun didStartScanning(filter: Set<UUID>) {
        TODO("Not yet implemented")
    }

    override suspend fun didStopScanning() {
        TODO("Not yet implemented")
    }

    override fun generateEnableSensorsActions(): List<EnableSensorAction> = emptyList()

    override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Set<UUID>): List<Scanner.Event.DeviceDiscovered> = TODO("Not yet implemented")
}
