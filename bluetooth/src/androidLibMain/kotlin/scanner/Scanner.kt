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
import com.splendo.kaluga.logging.warn
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionContext
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.Dispatchers
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
    // private val locationManager = LocationManager.Builder(applicationContext)
    //     .create(
    //         permission = LocationPermission(precise = true, background = false),
    //         permissions = Permissions(PermissionsBuilder()),
    //         autoRequestPermission = true,
    //         autoEnableLocations = true
    //     ) // { checkBluetoothEnabledChanged() }


    val locationStateRepo = LocationStateRepoBuilder().create(
        locationPermission = LocationPermission(precise = true, background = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        coroutineContext = stateRepo.coroutineContext
    )

    val locationManager = LocationManager(
        context = applicationContext,
        locationPermission = LocationPermission(precise = true, background = false),
        autoRequestPermission = true,
        autoEnableLocations = false,
        locationStateRepo = locationStateRepo,
        permissions = Permissions(
            PermissionsBuilder(PermissionContext(applicationContext)).apply { registerLocationPermission() },
            Dispatchers.Main
        )
    )

    // private var isLocationEnabled: Boolean = locationEnabledMonitor.isServiceEnabled
    private var shouldEnableLocation: Boolean = false

    override suspend fun scanForDevices(filter: Set<UUID>) {
        error("KalugaScanner") { "() scanForDevices($filter)" }
        bluetoothScanner.startScan(filter.map {
            ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build()
        }, scanSettings, callback)
    }

    override suspend fun stopScanning() {
        error("KalugaScanner") { "stopScanning()" }
        bluetoothScanner.stopScan(callback)
    }

    override fun startMonitoringBluetooth() {
        warn("KalugaScanner") { "startMonitoringBluetooth()" }
        locationEnabledMonitor.startMonitoring()
        applicationContext.registerReceiver(bluetoothAvailabilityBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun stopMonitoringBluetooth() {
        warn("KalugaScanner") { "stopMonitoringBluetooth()" }
        locationEnabledMonitor.stopMonitoring()
        applicationContext.unregisterReceiver(bluetoothAvailabilityBroadcastReceiver)
    }

    override suspend fun isPermitted(): Boolean {
        return super.isPermitted() && locationPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    override suspend fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled == true && locationEnabledMonitor.isServiceEnabled

    override suspend fun requestBluetoothEnable() {
        shouldEnableLocation = !locationEnabledMonitor.isServiceEnabled
        if (bluetoothAdapter?.isEnabled != true) {
            bluetoothAdapter?.enable()
        } else if (shouldEnableLocation) {
            shouldEnableLocation = false
            locationManager.requestLocationEnable()
            // locationEnabledMonitor.requestLocationEnable()
        }
    }

    private fun checkBluetoothEnabledChanged() =
        launch(stateRepo.coroutineContext) {
            val willEnableLocation = shouldEnableLocation
            shouldEnableLocation = false
            when {
                isBluetoothEnabled() -> bluetoothEnabled()
                // willEnableLocation -> {} locationEnabledMonitor.requestLocationEnable()
                else -> bluetoothDisabled()
            }
        }
}
