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

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice.BOND_NONE
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.ParcelUuid
import android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.core.app.ActivityCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.base.utils.containsAny
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DefaultDeviceWrapper
import com.splendo.kaluga.bluetooth.device.PairedAdvertisementData
import com.splendo.kaluga.location.LocationMonitor
import com.splendo.kaluga.logging.e
import com.splendo.kaluga.permissions.base.PermissionState
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.service.EnableServiceActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings

/**
 * A default implementation of [BaseScanner]
 * @param applicationContext the [Context] in which Bluetooth should run
 * @param bluetoothScanner the [BluetoothLeScannerCompat] to use for scanning
 * @param bluetoothAdapter the [BluetoothAdapter] to use to access Bluetooth
 * @param scanSettings the [ScanSettings] to apply to the scanner
 * @param settings the [BaseScanner.Settings] to configure this scanner
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched. It is recommended to make this a dispatcher that can handle high frequency of events
 */
actual class DefaultScanner internal constructor(
    private val applicationContext: Context,
    private val bluetoothScanner: BluetoothLeScannerCompat,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val scanSettings: ScanSettings,
    settings: Settings,
    coroutineScope: CoroutineScope,
    scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
) : BaseScanner(settings, coroutineScope, scanningDispatcher) {

    /**
     * Builder for creating a [DefaultScanner]
     * @param applicationContext the [Context] in which Bluetooth should run
     * @param bluetoothScanner the [BluetoothLeScannerCompat] to use for scanning
     * @param bluetoothAdapter the [BluetoothAdapter] to use to access Bluetooth
     * @param scanSettings the [ScanSettings] to apply to the scanner
     */
    class Builder(
        private val applicationContext: Context = ApplicationHolder.applicationContext,
        private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
        private val bluetoothAdapter: BluetoothAdapter? = (applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter,
        private val scanSettings: ScanSettings = defaultScanSettings,
    ) : BaseScanner.Builder {

        override fun create(
            settings: Settings,
            coroutineScope: CoroutineScope,
            scanningDispatcher: CoroutineDispatcher,
        ): BaseScanner {
            return DefaultScanner(applicationContext, bluetoothScanner, bluetoothAdapter, scanSettings, settings, coroutineScope, scanningDispatcher)
        }
    }

    companion object {
        /**
         * Default [ScanSettings]
         */
        val defaultScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setNumOfMatches(ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT)
            .build()

        private val locationPermission = LocationPermission(background = false, precise = true)
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

            e { error.first }
            if (error.second) {
                eventChannel.trySend(Scanner.Event.FailedScanning)
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

        @SuppressLint("MissingPermission") // Lint complains even with permissions
        private fun handleScanResult(scanResult: ScanResult) {
            if (!settings.discoverBondedDevices && scanResult.device.bondState != BOND_NONE) return // ignore bonded devices
            val advertisementData = AdvertisementData(scanResult)
            val deviceWrapper = DefaultDeviceWrapper(scanResult.device)

            handleDeviceDiscovered(deviceWrapper, scanResult.rssi, advertisementData, deviceConnectionManagerBuilder)
        }
    }

    private val locationPermissionRepo get() = permissions[locationPermission]

    override val isSupported: Boolean = bluetoothAdapter != null
    private val deviceConnectionManagerBuilder = DefaultDeviceConnectionManager.Builder(applicationContext)
    override val bluetoothEnabledMonitor: BluetoothMonitor? = bluetoothAdapter?.let { BluetoothMonitor.Builder(applicationContext, it).create() }
    private val locationEnabledMonitor = LocationMonitor.Builder(applicationContext).create()

    override val permissionsFlow: Flow<List<PermissionState<*>>> get() = combine(
        bluetoothPermissionRepo.filterOnlyImportant(),
        locationPermissionRepo.filterOnlyImportant(),
    ) { bluetoothPermission, locationPermission ->
        listOf(bluetoothPermission, locationPermission)
    }
    override val enabledFlow: Flow<List<Boolean>> get() = combine(
        bluetoothEnabledMonitor?.isEnabled ?: flowOf(false),
        locationEnabledMonitor.isEnabled,
    ) { bluetoothEnabled, locationEnabled ->
        listOf(bluetoothEnabled, locationEnabled)
    }

    override suspend fun didStartScanning(filter: Filter) {
        bluetoothScanner.startScan(
            filter.map {
                ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build()
            },
            scanSettings,
            callback,
        )
    }

    override suspend fun didStopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    override suspend fun startMonitoringHardwareEnabled() {
        locationEnabledMonitor.startMonitoring()
        super.startMonitoringHardwareEnabled()
    }

    override suspend fun stopMonitoringHardwareEnabled() {
        locationEnabledMonitor.stopMonitoring()
        super.stopMonitoringHardwareEnabled()
    }

    override suspend fun isHardwareEnabled(): Boolean = super.isHardwareEnabled() && locationEnabledMonitor.isServiceEnabled

    @SuppressLint("MissingPermission") // Lint complains even with permissions
    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        if (!isSupported) return emptyList()
        return listOfNotNull(
            if (bluetoothAdapter?.isEnabled != true) {
                suspend {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        EnableServiceActivity.showEnableServiceActivity(
                            applicationContext,
                            hashCode().toString(),
                            Intent(ACTION_BLUETOOTH_SETTINGS),
                        ).await()
                    } else {
                        @Suppress("DEPRECATION")
                        bluetoothAdapter?.enable()
                    }
                    bluetoothEnabledMonitor!!.isEnabled.first { it }
                }
            } else {
                null
            },
            if (!locationEnabledMonitor.isServiceEnabled) {
                EnableServiceActivity.showEnableServiceActivity(
                    applicationContext,
                    hashCode().toString(),
                    Intent(ACTION_LOCATION_SOURCE_SETTINGS),
                )::await
            } else {
                null
            },
        )
    }

    @SuppressLint("MissingPermission") // Lint complains even with permissions
    override suspend fun retrievePairedDeviceDiscoveredEvents(
        withServices: Filter,
        connectionSettings: ConnectionSettings?,
    ): List<Scanner.DeviceDiscovered> {
        if (!isSupported) {
            return emptyList()
        }
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            Manifest.permission.BLUETOOTH
        }
        val result = ActivityCompat.checkSelfPermission(
            applicationContext,
            permission,
        )
        if (result != PackageManager.PERMISSION_GRANTED) {
            return emptyList()
        }
        return bluetoothAdapter?.bondedDevices
            ?.filter {
                // If no filter available return this device
                // Otherwise check if it contains any of given service uuid
                if (withServices.isEmpty()) {
                    true
                } else {
                    it.uuids?.map(ParcelUuid::getUuid).orEmpty().containsAny(withServices)
                }
            }
            ?.map { device ->
                val deviceWrapper = DefaultDeviceWrapper(device)
                val serviceUUIDs = device.uuids
                    ?.map(ParcelUuid::getUuid)
                    ?: withServices.toList() // fallback to filter, as it *must* contain one of them

                val advertisementData = PairedAdvertisementData(deviceWrapper.name, serviceUUIDs)
                Scanner.DeviceDiscovered(
                    identifier = deviceWrapper.identifier,
                    rssi = Int.MIN_VALUE,
                    advertisementData = advertisementData,
                    deviceCreator = getDeviceBuilder(deviceWrapper, Int.MIN_VALUE, advertisementData, deviceConnectionManagerBuilder, connectionSettings),
                )
            } ?: emptyList()
    }
}
