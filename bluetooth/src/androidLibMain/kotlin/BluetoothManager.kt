package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.*

actual class BluetoothManager(private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
                              private val scanSettings: ScanSettings = defaultScanSettings,
                              permissions: Permissions,
                              private val context: Context,
                              coroutineScope: CoroutineScope,
                              stateRepoAccesor: StateRepoAccesor<BluetoothState>) : BaseBluetoothManager(permissions, stateRepoAccesor), CoroutineScope by coroutineScope {

    class Builder(private val bluetoothScanner: BluetoothLeScannerCompat = BluetoothLeScannerCompat.getScanner(),
                  private val scanSettings: ScanSettings = defaultScanSettings,
                  private val permissions: Permissions,
                  private val context: Context) : BaseBluetoothManager.Builder {

        override fun create(stateRepoAccessor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope): BluetoothManager {
            return BluetoothManager(bluetoothScanner, scanSettings, permissions, context, coroutineScope, stateRepoAccessor)
        }
    }

    private class BluetoothScannerCallback(private val context: Context, private val stateRepoAccesor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope) : ScanCallback(), CoroutineScope by coroutineScope {

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
                        is BluetoothState.Scanning -> state.stopScanning()
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
                    is BluetoothState.Scanning -> {
                        val devices = results.map { Device(it.device, context) }
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
    private val broadcastReceiver = AvailabilityReceiver(stateRepoAccesor, this)

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

private class AvailabilityReceiver(private val stateRepoAccesor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope) : BroadcastReceiver(), CoroutineScope by coroutineScope {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                launch {
                    when (state) {
                        BluetoothAdapter.STATE_ON -> bluetoothEnabled()
                        BluetoothAdapter.STATE_OFF -> bluetoothDisabled()
                    }
                }
            }
        }
    }

    private suspend fun bluetoothEnabled() {
        when (val state = stateRepoAccesor.currentState()) {
            is BluetoothState.Disabled -> state.enable()
        }
    }

    private suspend fun bluetoothDisabled() {
        when (val state = stateRepoAccesor.currentState()) {
            is BluetoothState.Enabled -> state.disable()
        }
    }

}