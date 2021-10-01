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
import com.splendo.kaluga.base.DefaultServiceMonitor
import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitorState
import com.splendo.kaluga.logging.debug
import kotlin.coroutines.CoroutineContext

actual interface BluetoothMonitor : ServiceMonitor {

    actual class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val adapter: BluetoothAdapter?
    ) {
        actual fun create(coroutineContext: CoroutineContext): DefaultServiceMonitor {
            return DefaultBluetoothMonitor(
                applicationContext = context,
                bluetoothAdapter = adapter,
                coroutineContext = coroutineContext
            )
        }
    }
}

class DefaultBluetoothMonitor internal constructor(
    private val applicationContext: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext), BluetoothMonitor {

    private val availabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                launchTakeAndChangeState {
                    when (state) {
                        BluetoothAdapter.STATE_ON,
                        BluetoothAdapter.STATE_TURNING_ON -> {
                            { ServiceMonitorState.Initialized.Enabled }
                        }
                        BluetoothAdapter.STATE_OFF,
                        BluetoothAdapter.STATE_TURNING_OFF -> {
                            { ServiceMonitorState.Initialized.Disabled }
                        }
                        else -> {
                            { ServiceMonitorState.NotSupported }
                        }
                    }
                }
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON") // BluetoothAdapter can still be null on initialization
    override fun startMonitoring() {
        super.startMonitoring()
        if (bluetoothAdapter == null) {
            debug(TAG) { "BluetoothAdapter not supported." }
            launchTakeAndChangeState { { ServiceMonitorState.NotSupported } }
            return
        }
        debug(TAG) { "Register broadcast receiver with applicationContext = $applicationContext and bluetoothAdapter = $bluetoothAdapter" }
        applicationContext.registerReceiver(
            availabilityBroadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )

        launchTakeAndChangeState { // Force first emission
            {
                if (bluetoothAdapter.isEnabled) {
                    ServiceMonitorState.Initialized.Enabled
                } else {
                    ServiceMonitorState.Initialized.Disabled
                }
            }
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    override fun stopMonitoring() {
        super.stopMonitoring()
        debug(TAG) { "Unregister broadcast receiver with applicationContext = $applicationContext and bluetoothAdapter = $bluetoothAdapter" }
        if (bluetoothAdapter != null) {
            applicationContext.unregisterReceiver(availabilityBroadcastReceiver)
        }
    }
}
