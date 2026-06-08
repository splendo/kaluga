/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor

internal fun hasWebBluetooth(): Boolean = js("typeof navigator !== 'undefined' && !!navigator.bluetooth")

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled, shared by the JS family (js + wasmJs).
 */
actual interface BluetoothMonitor : ServiceMonitor {

    /**
     * Builder for creating a [BluetoothMonitor]
     */
    actual class Builder {

        /**
         * Creates the [BluetoothMonitor]
         * @return the [BluetoothMonitor] created
         */
        actual fun create(): BluetoothMonitor = DefaultBluetoothMonitor()
    }
}

/**
 * Default [BluetoothMonitor] for the JS family. Web Bluetooth's powered-on/adapter state is only
 * available asynchronously (`navigator.bluetooth.getAvailability()`), so this uses the presence of the
 * Web Bluetooth API as a synchronous proxy for "enabled" — sufficient to gate the scanner, which
 * surfaces the real availability when the user opens the device picker.
 */
class DefaultBluetoothMonitor :
    DefaultServiceMonitor(),
    BluetoothMonitor {

    override val isServiceEnabled: Boolean get() = hasWebBluetooth()

    override fun monitoringDidStart() {
        // `startMonitoring` already calls `updateState()`; Web Bluetooth API presence does not change at runtime.
    }

    override fun monitoringDidStop() {}
}
