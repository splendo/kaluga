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

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.ParcelUuid
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.*

actual class Scanner internal constructor(private val autoEnableBluetooth: Boolean,
                                          private val bluetoothScanner: BluetoothLeScannerCompat,
                                          private val bluetoothAdapter: BluetoothAdapter,
                                          private val scanSettings: ScanSettings = defaultScanSettings,
                                          permissions: Permissions,
                                          private val connectionSettings: ConnectionSettings,
                                          private val context: Context,
                                          stateRepo: StateRepo<ScanningState>
) : BaseScanner(permissions, stateRepo) {

    class Builder(private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
                  override val autoEnableBluetooth: Boolean,
                  private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter(),
                  private val scanSettings: ScanSettings = defaultScanSettings,
                  private val permissions: Permissions,
                  private val connectionSettings: ConnectionSettings,
                  private val context: Context = ApplicationHolder.applicationContext) : BaseScanner.Builder {

        override fun create(stateRepo: StateRepo<ScanningState>): Scanner {
            return Scanner(autoEnableBluetooth, bluetoothScanner, bluetoothAdapter, scanSettings, permissions, connectionSettings, context, stateRepo)
        }
    }

    companion object {
        val defaultScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setNumOfMatches(ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT)
            .build()
    }

    private val callback = object : ScanCallback() {

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            val error = when(errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> Pair("Already Started", false)
                SCAN_FAILED_SCANNING_TOO_FREQUENTLY -> Pair("Scanning Too Frequently", true)
                SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> Pair("Out of Resources", true)
                SCAN_FAILED_INTERNAL_ERROR -> Pair("Internal Error", true)
                SCAN_FAILED_FEATURE_UNSUPPORTED -> Pair("Feature Unsupported", true)
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> Pair("Registration Failed", true)
                else -> Pair("Reason Unknown", true)
            }

            launch {
                stateRepo.peekState().logError(Error(error.first))
                if (error.second) {
                    stateRepo.takeAndChangeState { state ->
                        when (state) {
                            is ScanningState.Enabled.Scanning -> state.stopScanning()
                            else -> state.remain
                        }
                    }
                }
            }

        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            receiveResults(listOf(result))
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)

            receiveResults(results)
        }

        private fun receiveResults(results: List<ScanResult>) {
            launch {
                stateRepo.takeAndChangeState { state ->
                    when (state) {
                        is ScanningState.Enabled.Scanning -> {
                            val devices = results.map {
                                val advertisementData = AdvertisementData(it.scanRecord)
                                val deviceInfoHolder = DeviceInfoHolder(DefaultDeviceWrapper(it.device), advertisementData)
                                Device(connectionSettings, deviceInfoHolder, it.rssi, DeviceConnectionManager.Builder(context))
                            }
                            state.discoverDevices(*devices.toTypedArray())
                        }
                        else -> {
                            state.logError(Error("Discovered Device while not scanning"))
                            state.remain
                        }
                    }
                }
            }
        }

    }
    private val broadcastReceiver = AvailabilityReceiver(this)

    override fun scanForDevices(filter: Set<UUID>) {
        bluetoothScanner.startScan(filter.map { ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build() }, scanSettings, callback)
    }

    override fun stopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    override fun startMonitoringBluetooth() {
        context.registerReceiver(broadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun stopMonitoringBluetooth() {
        context.unregisterReceiver(broadcastReceiver)
    }

    internal fun notifyBluetoothDisabledAndAutoconnectIfRequired() {
        bluetoothDisabled()
        if (autoEnableBluetooth)
            bluetoothAdapter.enable()
    }

}

private class AvailabilityReceiver(private val bluetoothScanner: Scanner) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                        BluetoothAdapter.STATE_ON -> bluetoothScanner.bluetoothEnabled()
                        BluetoothAdapter.STATE_OFF -> bluetoothScanner.notifyBluetoothDisabledAndAutoconnectIfRequired()
                    }
            }
        }
    }

}