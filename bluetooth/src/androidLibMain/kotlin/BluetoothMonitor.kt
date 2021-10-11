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

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.DefaultServiceMonitor
import com.splendo.kaluga.base.ServiceMonitor
import com.splendo.kaluga.base.monitor.ServiceMonitorState
import com.splendo.kaluga.logging.debug
import kotlin.coroutines.CoroutineContext

internal typealias SystemServiceState = Int

actual interface BluetoothMonitor : ServiceMonitor {

    /**
     * Builder for [BluetoothMonitor]. When is used from simulator, [LocationManager] is returned as **null**.
     * In that case the state will be emitted as [ServiceMonitorState.NotSupported] and no [BroadcastReceiver] will be registered.
     * @param context [Context] used to register the [BroadcastReceiver].
     * @param adapter [BluetoothAdapter] in order to define whether the service is active or not, or if service is not supported at all.
     */
    actual class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val adapter: BluetoothAdapter?
    ) {
        /**
         * Builder's create method.
         * @param coroutineContext [CoroutineContext] used to define the coroutine context where code will run.
         * @return [DefaultServiceMonitor]
         */
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

    private val isUnauthorized: Boolean
        get() = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_DENIED

    private val availabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                updateState(state)
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
                   isUnauthorizedOrDefault(ServiceMonitorState.Initialized.Enabled)
                } else {
                    isUnauthorizedOrDefault(ServiceMonitorState.Initialized.Disabled)
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


    private fun updateState(state: SystemServiceState) {
        launchTakeAndChangeState {
            {
                when (state) {
                    BluetoothAdapter.STATE_ON ->
                        isUnauthorizedOrDefault(ServiceMonitorState.Initialized.Enabled)
                    BluetoothAdapter.STATE_OFF -> {
                        isUnauthorizedOrDefault(ServiceMonitorState.Initialized.Disabled)
                    }
                    BluetoothAdapter.STATE_TURNING_ON,
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        ServiceMonitorState.Initialized.Disabled
                    }
                    else -> {
                        ServiceMonitorState.NotInitialized
                    }
                }
            }
        }
    }

    private fun isUnauthorizedOrDefault(default: ServiceMonitorState) =
        if (isUnauthorized) {
            ServiceMonitorState.Initialized.Unauthorized
        } else {
            default
        }
}
