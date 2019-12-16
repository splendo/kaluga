package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.typedMap
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.*
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class BluetoothScanner(permissions: Permissions, stateRepoAccesor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : BaseBluetoothScanner(permissions, stateRepoAccesor, coroutineScope)  {

    class Builder(private val permissions: Permissions) : BaseBluetoothScanner.Builder {

        override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): BluetoothScanner {
            return BluetoothScanner(permissions, stateRepoAccessor, coroutineScope)
        }
    }

    private class CentralManagerDelegate(val bluetoothScanner: BluetoothScanner) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            super.centralManager(central, didDiscoverPeripheral, advertisementData, RSSI)

            bluetoothScanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap())
        }

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            when (central.state) {
                CBCentralManagerStatePoweredOn -> bluetoothScanner.bluetoothEnabled()
                else -> bluetoothScanner.bluetoothDisabled()
            }
        }

    }

    private val centralManagerDelegate = CentralManagerDelegate(this)
    private val mainCentralManager = CBCentralManager(null, dispatch_get_main_queue())
    private val centralManagers = emptyList<CBCentralManager>().toMutableList()
    private var devicesMap = emptyMap<Identifier, Device>().toMutableMap()

    override fun scanForDevices(filter: Set<UUID>) {
        devicesMap.clear()

        if (filter.isEmpty()) {
            val centralManager = CBCentralManager(centralManagerDelegate, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            centralManager.scanForPeripheralsWithServices(null, null)
        }

        filter.map { it.uuid }.forEach {
            val centralManager = CBCentralManager(centralManagerDelegate, dispatch_get_main_queue())
            centralManagers.add(centralManager)
            centralManager.scanForPeripheralsWithServices(listOf(it), null)
        }
    }

    override fun stopScanning() {
        centralManagers.forEach {
            when(it.state) {
                CBCentralManagerStatePoweredOn -> it.stopScan()
            }
        }
        centralManagers.clear()
    }

    override fun startMonitoringBluetooth() {
        devicesMap.clear()
        mainCentralManager.delegate = centralManagerDelegate
    }

    override fun stopMonitoringBluetooth() {
        devicesMap.clear()
        mainCentralManager.delegate = null
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>) {
        if (central == mainCentralManager)
            return
        // Since multiple managers may discover device, make sure even is only triggered once
        if (devicesMap.containsKey(peripheral.identifier))
            return
        launch {
            when (val state = stateRepoAccesor.currentState()) {
                is ScanningState.Scanning -> {
                    val advertisementData = AdvertisementData(advertisementDataMap)
                    val device = Device(peripheral, central, advertisementData)
                    devicesMap[device.identifier] = device
                    state.discoverDevices(device)
                }
                else -> state.logError(Error("Discovered Device while not scanning"))
            }
        }
    }


}