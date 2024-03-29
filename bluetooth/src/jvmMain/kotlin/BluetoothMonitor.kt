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

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled
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
 * Default implementation of [BluetoothMonitor]
 */
class DefaultBluetoothMonitor : DefaultServiceMonitor(), BluetoothMonitor {

    override val isServiceEnabled: Boolean
        get() = TODO("Not yet implemented")

    override fun monitoringDidStart() {
        TODO("Not yet implemented")
    }

    override fun monitoringDidStop() {
        TODO("Not yet implemented")
    }
}
