package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class BluetoothManager(permissions: Permissions, stateRepoAccesor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope) : BaseBluetoothManager(permissions, stateRepoAccesor, coroutineScope)  {

    class Builder(private val permissions: Permissions) : BaseBluetoothManager.Builder {

        override fun create(stateRepoAccessor: StateRepoAccesor<BluetoothState>, coroutineScope: CoroutineScope): BluetoothManager {
            return BluetoothManager(permissions, stateRepoAccessor, coroutineScope)
        }
    }

    private class CentralManagerDelegate(val bluetoothManager: BluetoothManager) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            super.centralManager(central, didDiscoverPeripheral, advertisementData, RSSI)

            bluetoothManager.discoverPeripheral(didDiscoverPeripheral)
        }

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            when (central.state) {
                CBCentralManagerStatePoweredOn -> bluetoothManager.bluetoothEnabled()
                else -> bluetoothManager.bluetoothDisabled()
            }
        }

    }

    private var centralManager: CBCentralManager = CBCentralManager(CentralManagerDelegate(this), dispatch_get_main_queue())

    override fun scanForDevices(filter: Set<UUID>) {
        val uuids = filter.map { it.uuid }
        centralManager.scanForPeripheralsWithServices(uuids, null)
    }

    override fun stopScanning() {
        centralManager.stopScan()
    }

    override fun startMonitoringBluetooth() {
    }

    override fun stopMonitoringBluetooth() {
    }

    private fun discoverPeripheral(peripheral: CBPeripheral) {
        launch { when (val state = stateRepoAccesor.currentState()) {
            is BluetoothState.Scanning -> {
                val device = Device(peripheral)
                state.discoverDevices(device)
            }
            else -> state.logError(Error("Discovered Device while not scanning"))
        } }
    }


}