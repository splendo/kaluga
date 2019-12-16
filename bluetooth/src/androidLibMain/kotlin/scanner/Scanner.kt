package com.splendo.kaluga.bluetooth.scanner

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.*

actual class Scanner(private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
                     private val scanSettings: ScanSettings = defaultScanSettings,
                     permissions: Permissions,
                     private val context: Context,
                     coroutineScope: CoroutineScope,
                     stateRepoAccesor: StateRepoAccesor<ScanningState>) : BaseScanner(permissions, stateRepoAccesor, coroutineScope) {

    class Builder(private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
                  private val scanSettings: ScanSettings = defaultScanSettings,
                  private val permissions: Permissions,
                  private val context: Context) : BaseScanner.Builder {

        override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner {
            return Scanner(bluetoothScanner, scanSettings, permissions, context, coroutineScope, stateRepoAccessor)
        }
    }

    private class BluetoothScannerCallback(private val context: Context, private val stateRepoAccesor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : ScanCallback(), CoroutineScope by coroutineScope {

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
                stateRepoAccesor.currentState().logError(Error(error.first))
                if (error.second) {
                    when (val state = stateRepoAccesor.currentState()) {
                        is ScanningState.Scanning -> state.stopScanning()
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
                when (val state = stateRepoAccesor.currentState()) {
                    is ScanningState.Scanning -> {
                        val devices = results.map {
                            val advertisementData = AdvertisementData(it.scanRecord)
                            Device(it.device, advertisementData, context)
                        }
                        state.discoverDevices(*devices.toTypedArray())
                    }
                    else -> state.logError(Error("Discovered Device while not scanning"))
                }
            }
        }

    }

    companion object {
        val defaultScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
            .setNumOfMatches(ScanSettings.MATCH_NUM_FEW_ADVERTISEMENT)
            .build()
    }

    private val callback = BluetoothScannerCallback(context, stateRepoAccesor, this)
    private val broadcastReceiver = AvailabilityReceiver(this)

    override fun scanForDevices(filter: Set<UUID>) {
        bluetoothScanner.startScan(filter.map { ScanFilter.Builder().setServiceUuid(it.uuid).build() }, scanSettings, callback)
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

}

private class AvailabilityReceiver(private val bluetoothScanner: BaseScanner) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                        BluetoothAdapter.STATE_ON -> bluetoothScanner.bluetoothEnabled()
                        BluetoothAdapter.STATE_OFF -> bluetoothScanner.bluetoothDisabled()
                    }
            }
        }
    }

}