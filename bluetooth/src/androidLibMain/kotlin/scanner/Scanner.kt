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
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.ParcelUuid
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceWrapper
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.location.EnableLocationActivity
import com.splendo.kaluga.location.LocationMonitor
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.state.StateRepo
import kotlinx.coroutines.Job
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
    private val applicationContext: Context,
    private val bluetoothScanner: BluetoothLeScannerCompat,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val scanSettings: ScanSettings,
    permissions: Permissions,
    connectionSettings: ConnectionSettings,
    autoRequestPermission: Boolean,
    autoEnableSensors: Boolean,
    stateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
) : BaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableSensors, stateRepo) {

    class Builder(
        private val applicationContext: Context = ApplicationHolder.applicationContext,
        private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
        private val bluetoothAdapter: BluetoothAdapter? = (applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter,
        private val scanSettings: ScanSettings = defaultScanSettings,
    ) : BaseScanner.Builder {

        override fun create(
            permissions: Permissions,
            connectionSettings: ConnectionSettings,
            autoRequestPermission: Boolean,
            autoEnableSensors: Boolean,
            scanningStateRepo: StateRepo<ScanningState, MutableStateFlow<ScanningState>>,
        ): BaseScanner {
            return Scanner(applicationContext, bluetoothScanner, bluetoothAdapter, scanSettings, permissions, connectionSettings, autoRequestPermission, autoEnableSensors, scanningStateRepo)
        }
    }

    companion object {
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

    private val locationPermissionRepo get() = permissions[locationPermission]

    override val isSupported: Boolean = bluetoothAdapter != null
    private val deviceConnectionManagerBuilder = DeviceConnectionManager.Builder(applicationContext)
    override val bluetoothEnabledMonitor: BluetoothMonitor? = bluetoothAdapter?.let { BluetoothMonitor.Builder(applicationContext, it).create() }
    private val locationEnabledMonitor = LocationMonitor.Builder(applicationContext).create()

    private val monitoringLocationPermissionsJob = AtomicReference<Job?>(null)
    private val monitoringLocationEnabledJob = AtomicReference<Job?>(null)

    override suspend fun scanForDevices(filter: Set<UUID>) {
        bluetoothScanner.startScan(
            filter.map {
                ScanFilter.Builder().setServiceUuid(ParcelUuid(it)).build()
            },
            scanSettings,
            callback
        )
    }

    override suspend fun stopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    override fun startMonitoringPermissions() {
        super.startMonitoringPermissions()
        if (monitoringLocationPermissionsJob.get() != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])

        if (monitoringLocationPermissionsJob.compareAndSet(null, job)) {
            launch(job) {
                locationPermissionRepo.collect { state ->
                    handlePermissionState(state, locationPermission)
                }
            }
        }
    }

    override fun stopMonitoringPermissions() {
        super.stopMonitoringPermissions()
        monitoringLocationPermissionsJob.get()?.let {
            if (monitoringLocationPermissionsJob.compareAndSet(it, null)) {
                it.cancel()
            }
        }
    }

    override fun startMonitoringSensors() {
        super.startMonitoringSensors()
        if (monitoringLocationEnabledJob.get() != null) return // optimization to skip making a job

        val job = Job(this.coroutineContext[Job])

        if (monitoringLocationEnabledJob.compareAndSet(null, job)) {
            locationEnabledMonitor.startMonitoring()
            launch(job) {
                locationEnabledMonitor.isEnabled.collect {
                    checkSensorsEnabledChanged()
                }
            }
        }
    }

    override fun stopMonitoringSensors() {
        super.stopMonitoringSensors()
        monitoringLocationEnabledJob.get()?.let {
            locationEnabledMonitor.stopMonitoring()
            if (monitoringLocationEnabledJob.compareAndSet(it, null))
                it.cancel()
        }
    }

    override suspend fun isPermitted(): Boolean {
        return super.isPermitted() && locationPermissionRepo.filterOnlyImportant().first() is PermissionState.Allowed
    }

    override suspend fun areSensorsEnabled(): Boolean = super.areSensorsEnabled() && locationEnabledMonitor.isServiceEnabled

    override fun generateEnableSensorsActions(): List<EnableSensorAction> {
        if (!isSupported) return emptyList()
        return listOfNotNull(
            if (bluetoothAdapter?.isEnabled != true) suspend {
                bluetoothAdapter?.enable()
                bluetoothEnabledMonitor!!.isEnabled.first { it }
            } else null,
            if (!locationEnabledMonitor.isServiceEnabled) {
                EnableLocationActivity.showEnableLocationActivity(applicationContext, hashCode().toString())::await
            } else null
        )
    }
}
