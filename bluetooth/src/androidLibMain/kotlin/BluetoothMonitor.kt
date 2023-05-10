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

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled
 */
actual interface BluetoothMonitor : ServiceMonitor {

    /**
     * Builder for creating a [BluetoothMonitor]
     * @param context the [Context] in which Bluetooth should run
     * @param adapter the [BluetoothAdapter] to use to access Bluetooth
     */
    actual class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val adapter: BluetoothAdapter,
    ) {

        /**
         * Creates the [BluetoothMonitor]
         * @return the [BluetoothMonitor] created
         */
        actual fun create(): BluetoothMonitor {
            return DefaultBluetoothMonitor(
                applicationContext = context,
                bluetoothAdapter = adapter,
            )
        }
    }
}

/**
 * A default implementation of [BluetoothMonitor]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param bluetoothAdapter the [BluetoothAdapter] to use to access Bluetooth
 */
class DefaultBluetoothMonitor internal constructor(
    private val applicationContext: Context,
    private val bluetoothAdapter: BluetoothAdapter,
) : DefaultServiceMonitor(), BluetoothMonitor {

    private val availabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                updateState()
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON") // Can still be null on initialization
    override val isServiceEnabled: Boolean
        get() = if (bluetoothAdapter == null) {
            false
        } else {
            bluetoothAdapter.isEnabled
        }

    override fun monitoringDidStart() {
        applicationContext.registerReceiver(
            availabilityBroadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
        )
    }

    override fun monitoringDidStop() {
        applicationContext.unregisterReceiver(availabilityBroadcastReceiver)
    }
}
