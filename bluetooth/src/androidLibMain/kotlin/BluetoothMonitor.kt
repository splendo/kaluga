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

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.core.content.ContextCompat
import androidx.core.os.BuildCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.service.DefaultServiceMonitor
import com.splendo.kaluga.service.ServiceMonitor
import com.splendo.kaluga.service.ServiceMonitorState
import kotlin.coroutines.CoroutineContext

internal typealias SystemServiceState = Int

/**
 * A [ServiceMonitor] that monitors whether Bluetooth is enabled
 */
actual interface BluetoothMonitor {

    /**
     * Builder for creating a [BluetoothMonitor]
     * @param context the [Context] in which Bluetooth should run
     * @param adapter the [BluetoothAdapter] to use to access Bluetooth
     */
    actual class Builder(
        private val context: Context = ApplicationHolder.applicationContext,
        private val adapter: BluetoothAdapter?
    ) {

        constructor() : this(
            ApplicationHolder.applicationContext,
            (ApplicationHolder.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter ?: throw IllegalArgumentException(
                "BluetoothAdapter should not be null, please check your device capabilities."
            )
        )

        /**
         * Creates the [BluetoothMonitor]
         * @return the [BluetoothMonitor] created
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

/**
 * A default implementation of [BluetoothMonitor]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param bluetoothAdapter the [BluetoothAdapter] to use to access Bluetooth
 */
class DefaultBluetoothMonitor internal constructor(
    private val applicationContext: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
    coroutineContext: CoroutineContext
) : DefaultServiceMonitor(coroutineContext = coroutineContext), BluetoothMonitor {

    /**
     * This property is checking whether [Manifest.permission.ACCESS_COARSE_LOCATION] is granted or not because it is
     * used by android system in order to discover nearby devices.
     * Also [Manifest.permission.BLUETOOTH] is a normal protection level permission, that means that [ContextCompat.checkSelfPermission]` method will always return granted.
     */
    private val isUnauthorized: Boolean
        get() {
            val locationIsDenied = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
            // Android 12 requires no more to check the BLUETOOTH permissions since COARSE_LOCATION is enough
            return if (Build.VERSION.SDK_INT >= VERSION_CODES.S) {
                locationIsDenied
            } else {
                val bluetoothIsDenied = ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_DENIED
                locationIsDenied || bluetoothIsDenied
            }
        }

    private val registeredReceivers = ArrayList<BroadcastReceiver>()

    private val availabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                updateState(state)
            }
        }
    }

    override fun monitoringDidStart() {
        super.monitoringDidStart()
        if (bluetoothAdapter == null) {
            debug(TAG) { "BluetoothAdapter not supported." }
            launchTakeAndChangeState { { ServiceMonitorStateImpl.NotSupported } }
            return
        }
        debug(TAG) { "Register broadcast receiver with applicationContext = $applicationContext and bluetoothAdapter = $bluetoothAdapter" }
        applicationContext.registerReceiver(
            availabilityBroadcastReceiver,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )

        registeredReceivers.add(availabilityBroadcastReceiver)

        launchTakeAndChangeState { // Force first emission
            {
                if (bluetoothAdapter.isEnabled) {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Enabled)
                } else {
                    isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Disabled)
                }
            }
        }
    }

    override fun monitoringDidStop() {
        debug(TAG) { "Unregister broadcast receiver with applicationContext = $applicationContext and bluetoothAdapter = $bluetoothAdapter" }
        if (bluetoothAdapter != null && registeredReceivers.contains(availabilityBroadcastReceiver)) {
            registeredReceivers.clear()
            applicationContext.unregisterReceiver(availabilityBroadcastReceiver)
        }
    }

    private fun updateState(state: SystemServiceState) {
        launchTakeAndChangeState {
            {
                when (state) {
                    BluetoothAdapter.STATE_ON ->
                        isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Enabled)
                    BluetoothAdapter.STATE_OFF ->
                        isUnauthorizedOrDefault(ServiceMonitorStateImpl.Initialized.Disabled)
                    BluetoothAdapter.STATE_TURNING_ON,
                    BluetoothAdapter.STATE_TURNING_OFF ->
                        ServiceMonitorStateImpl.Initialized.Disabled
                    else ->
                        ServiceMonitorStateImpl.NotInitialized
                }
            }
        }
    }

    private fun isUnauthorizedOrDefault(default: ServiceMonitorState) =
        if (isUnauthorized) {
            ServiceMonitorStateImpl.Initialized.Unauthorized
        } else {
            default as ServiceMonitorStateImpl
        }
}
