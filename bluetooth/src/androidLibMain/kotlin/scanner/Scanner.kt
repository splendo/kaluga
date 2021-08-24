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
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceWrapper
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.location.LocationManager
import com.splendo.kaluga.location.LocationMonitor
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.logging.info
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
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
    private val applicationContext: Context = ApplicationHolder.applicationContext,
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableSensors: Boolean,
    stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableSensors, stateRepo) {

    class Builder(
        private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
        private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter(),
        private val scanSettings: ScanSettings = defaultScanSettings,
        private val applicationContext: Context = ApplicationHolder.applicationContext,
    ) : BaseScanner.Builder {

        /**
         * @param autoEnableSensors note this does not auto enable location at the moment, this is TODO
         */
        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableSensors: Boolean,
            scanningStateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
        ): BaseScanner {
            return Scanner(bluetoothScanner, bluetoothAdapter, scanSettings, applicationContext, permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo)
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

            launch(stateRepo.coroutineContext) {
                stateRepo.peekState().logError(Error(error.first))
                if (error.second) {
                    stateRepo.takeAndChangeState { state ->
                        when (state) {
                            is ScanningState.Initialized.Enabled.Scanning -> state.stopScanning
                            else -> state.remain()
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
            val deviceWrapper = DefaultDeviceWrapper(scanResult.device)

            handleDeviceDiscovered(deviceWrapper.identifier, scanResult.rssi, advertisementData) {
                val deviceInfo = DeviceInfoImpl(deviceWrapper, scanResult.rssi, advertisementData)
                Device(connectionSettings, deviceInfo, deviceConnectionManagerBuilder, stateRepo.coroutineContext)
            }
        }
    }

    private val locationPermissionRepo get() = permissions[LocationPermission(
        background = false,
        precise = true
    )]

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

    private val deviceConnectionManagerBuilder = DeviceConnectionManager.Builder(applicationContext)

    private val locationEnabledMonitor = LocationMonitor.Builder().create()

    private val locationStateRepo = LocationStateRepoBuilder().create(
        locationPermission = LocationPermission(precise = true, background = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        coroutineContext = stateRepo.coroutineContext
    )

    private val locationManager = LocationManager(
        context = applicationContext,
        locationPermission = LocationPermission(precise = true, background = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        locationStateRepo = locationStateRepo,
        permissions = permissions
    )

    override suspend fun scanForDevices(filter: Set<UUID>) {
        bluetoothScanner.startScan(filter.map {
            ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build()
        }, scanSettings, callback)
    }

    override suspend fun stopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    override fun startMonitoringBluetooth() {
        applicationContext.registerReceiver(bluetoothAvailabilityBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
        startMonitoringLocation()
    }

    override fun stopMonitoringBluetooth() {
        applicationContext.unregisterReceiver(bluetoothAvailabilityBroadcastReceiver)
        stopMonitoringLocation()
    }

    override suspend fun isPermitted(): Boolean = super.isPermitted() && isPreciseLocationPermitted()

    override suspend fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true && locationEnabledMonitor.isServiceEnabled

    override suspend fun requestBluetoothEnable() {
        val shouldEnableLocation = !locationEnabledMonitor.isServiceEnabled
        val shouldEnableBluetooth = bluetoothAdapter?.isEnabled != true
        info("KalugaScanner") { "requestBluetoothEnable(gps = $shouldEnableLocation, bt = $shouldEnableBluetooth)" }
        if (shouldEnableBluetooth) bluetoothAdapter?.enable()
        if (shouldEnableLocation) locationManager.requestLocationEnable()
    }

    private suspend fun isPreciseLocationPermitted(): Boolean {
        return locationPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    private fun startMonitoringLocation() {
        locationEnabledMonitor.startMonitoring()
        launch(stateRepo.coroutineContext) {
            locationEnabledMonitor.isEnabled.collect {
                checkBluetoothEnabledChanged()
            }
        }
    }

    private fun stopMonitoringLocation() {
        locationEnabledMonitor.stopMonitoring()
    }

    private fun checkBluetoothEnabledChanged() =
        launch(stateRepo.coroutineContext) {
            val shouldEnableLocation = !locationEnabledMonitor.isServiceEnabled
            when {
                isBluetoothEnabled() -> bluetoothEnabled()
                shouldEnableLocation -> locationManager.requestLocationEnable()
                else -> bluetoothDisabled()
            }
        }
}
