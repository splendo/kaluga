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
import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceWrapper
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceHolder
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.location.LocationEnabledMonitor
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings

actual class Scanner internal constructor(
    private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter(),
    private val scanSettings: ScanSettings = defaultScanSettings,
    private val context: Context = ApplicationHolder.applicationContext,
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableBluetooth: Boolean,
    stateRepo: StateRepo<ScanningState>,
    private val coroutineScope: CoroutineScope
) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, stateRepo, coroutineScope) {

    class Builder(
        private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
        private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter(),
        private val scanSettings: ScanSettings = defaultScanSettings,
        private val context: Context = ApplicationHolder.applicationContext
    ) : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableBluetooth: Boolean,
            scanningStateRepo: StateRepo<ScanningState>,
            coroutineScope: CoroutineScope
        ): BaseScanner {
            return Scanner(bluetoothScanner, bluetoothAdapter, scanSettings, context, permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo, coroutineScope)
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
            val error = when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> Pair("Already Started", false)
                SCAN_FAILED_SCANNING_TOO_FREQUENTLY -> Pair("Scanning Too Frequently", true)
                SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> Pair("Out of Resources", true)
                SCAN_FAILED_INTERNAL_ERROR -> Pair("Internal Error", true)
                SCAN_FAILED_FEATURE_UNSUPPORTED -> Pair("Feature Unsupported", true)
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> Pair("Registration Failed", true)
                else -> Pair("Reason Unknown", true)
            }

            launch(MainQueueDispatcher) {
                stateRepo.peekState().logError(Error(error.first))
                if (error.second) {
                    stateRepo.takeAndChangeState { state ->
                        when (state) {
                            is ScanningState.Enabled.Scanning -> state.stopScanning
                            else -> state.remain
                        }
                    }
                }
            }
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (callbackType == ScanSettings.CALLBACK_TYPE_MATCH_LOST) return
            handleScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach {
                handleScanResult(it)
            }
        }

        private fun handleScanResult(scanResult: ScanResult) {
            val advertisementData = AdvertisementData(scanResult)
            val deviceHolder = DeviceHolder(DefaultDeviceWrapper(scanResult.device))

            handleDeviceDiscovered(deviceHolder.identifier, scanResult.rssi, advertisementData) {
                val deviceInfo = DeviceInfoImpl(deviceHolder, scanResult.rssi, advertisementData)
                Device(connectionSettings, deviceInfo, deviceConnectionManagerBuilder, coroutineScope)
            }
        }
    }

    private val bluetoothAvailabilityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                        BluetoothAdapter.STATE_ON -> checkBluetoothEnabledChanged()
                        BluetoothAdapter.STATE_OFF -> checkBluetoothEnabledChanged()
                    }
                }
            }
        }
    }

    private val deviceConnectionManagerBuilder = DeviceConnectionManager.Builder(context)
    private val locationEnabledMonitor = LocationEnabledMonitor(context, Permission.Location(precise = true, background = false)) { checkBluetoothEnabledChanged() }
    private var shouldEnableLocation: Boolean = false

    override suspend fun scanForDevices(filter: Set<UUID>) {
        bluetoothScanner.startScan(filter.map { ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build() }, scanSettings, callback)
    }

    override suspend fun stopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    override fun startMonitoringBluetooth() {
        context.registerReceiver(bluetoothAvailabilityBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun stopMonitoringBluetooth() {
        context.unregisterReceiver(bluetoothAvailabilityBroadcastReceiver)
    }

    override suspend fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true && locationEnabledMonitor.isLocationEnabled()

    override suspend fun requestBluetoothEnable() {
        shouldEnableLocation = !locationEnabledMonitor.isLocationEnabled()
        if (bluetoothAdapter?.isEnabled != true) {
            bluetoothAdapter?.enable()
        } else if (shouldEnableLocation) {
            shouldEnableLocation = false
            locationEnabledMonitor.requestLocationEnable()
        }
    }

    private fun checkBluetoothEnabledChanged() {
        launch(MainQueueDispatcher) {
            val willEnableLocation = shouldEnableLocation
            shouldEnableLocation = false
            when {
                isBluetoothEnabled() -> bluetoothEnabled()
                willEnableLocation -> locationEnabledMonitor.requestLocationEnable()
                else -> bluetoothDisabled()
            }
        }
    }
}
