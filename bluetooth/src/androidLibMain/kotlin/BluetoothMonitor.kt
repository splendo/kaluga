/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.logging.warn

actual class BluetoothMonitor internal constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val applicationContext: Context
) : ServiceMonitor()  {

    actual class Builder actual constructor() {
        actual fun create() = BluetoothMonitor(
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(),
            applicationContext = ApplicationHolder.applicationContext
        )
    }

    private val availabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                updateEnabledState()
            }
        }
    }

    override val isServiceEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun startMonitoring() {
        if (bluetoothAdapter == null) warn("BluetoothMonitor") { "bluetoothAdapter is null" }
        applicationContext.registerReceiver(
            availabilityBroadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )
        updateEnabledState()
    }

    override fun stopMonitoring() {
        applicationContext.unregisterReceiver(availabilityBroadcastReceiver)
        updateEnabledState()
    }

    private fun updateEnabledState() {
        _isEnabled.value = isServiceEnabled
    }
}
