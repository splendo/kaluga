package com.splendo.kaluga.bluetooth

import android.content.Context
import com.splendo.kaluga.base.runBlocking
import no.nordicsemi.android.support.v18.scanner.*

actual class BluetoothManager(private val scanSettings: ScanSettings = defaultScanSettings, private val context: Context) : BaseBluetoothManager() {

    private class BluetoothScannerCallback(val bluetoothManager: BluetoothManager) : ScanCallback() {

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

            bluetoothManager.scanFailedWithError(Error(error.first), error.second)
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            bluetoothManager.discoverDevice(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)

            results.forEach { bluetoothManager.discoverDevice(it) }
        }

    }

    companion object {
        val defaultScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setNumOfMatches(ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT)
            .build()
    }

    private val bluetoothScanner = BluetoothLeScannerCompat.getScanner()
    private val callback = BluetoothScannerCallback(this)

    override fun handleStartScanning(filter: Set<UUID>) {
        val scanFilter = filter.map { ScanFilter.Builder().setServiceUuid(it.uuid).build() }
        bluetoothScanner.startScan(scanFilter, scanSettings, callback)
    }

    override fun handleStopScanning() {
        bluetoothScanner.stopScan(callback)
    }

    private fun discoverDevice(scanResult: ScanResult) {
        val device = Device(scanResult.device, context)
        runBlocking { discoveredDevice(device) }
    }

}