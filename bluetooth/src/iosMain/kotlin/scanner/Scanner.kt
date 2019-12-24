package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.base.typedMap
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.*
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.*
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class Scanner internal constructor(autoEnableBluetooth: Boolean,
                                          permissions: Permissions,
                                          stateRepoAccesor: StateRepoAccesor<ScanningState>,
                                          coroutineScope: CoroutineScope)
    : BaseScanner(permissions, stateRepoAccesor, coroutineScope)  {

    class Builder(override val autoEnableBluetooth: Boolean, private val permissions: Permissions) : BaseScanner.Builder {

        override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner {
            return Scanner(autoEnableBluetooth, permissions, stateRepoAccessor, coroutineScope)
        }
    }

    private class CentralManagerDelegate(val bluetoothScanner: Scanner) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            super.centralManager(central, didDiscoverPeripheral, advertisementData, RSSI)

            bluetoothScanner.discoverPeripheral(central, didDiscoverPeripheral, advertisementData.typedMap(), RSSI.intValue)
        }

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            when (central.state) {
                CBCentralManagerStatePoweredOn -> bluetoothScanner.bluetoothEnabled()
                else -> bluetoothScanner.bluetoothDisabled()
            }
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            super.centralManager(central, didConnectPeripheral)
        }

        override fun centralManager(central: CBCentralManager, willRestoreState: Map<Any?, *>) {
            super.centralManager(central, willRestoreState)
        }

    }

    private val centralManagerDelegate = CentralManagerDelegate(this)
    private val mainCentralManager: CBCentralManager
    private val centralManagers = emptyList<CBCentralManager>().toMutableList()
    private var connectionManagerMap = emptyMap<Identifier, DeviceConnectionManager>().toMutableMap()

    init {
        val options = mapOf<Any?, Any>(CBCentralManagerOptionShowPowerAlertKey to autoEnableBluetooth)
        mainCentralManager = CBCentralManager(null, dispatch_get_main_queue(), options)
    }

    override fun scanForDevices(filter: Set<UUID>) {
        connectionManagerMap.clear()

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
        connectionManagerMap.clear()
        mainCentralManager.delegate = centralManagerDelegate
    }

    override fun stopMonitoringBluetooth() {
        connectionManagerMap.clear()
        mainCentralManager.delegate = null
    }

    private fun discoverPeripheral(central: CBCentralManager, peripheral: CBPeripheral, advertisementDataMap: Map<String, Any>, rssi: Int) {
        if (central == mainCentralManager)
            return
        // Since multiple managers may discover device, make sure even is only triggered once
        if (connectionManagerMap.containsKey(peripheral.identifier))
            return
        launch {
            when (val state = stateRepoAccessor.currentState()) {
                is ScanningState.Enabled.Scanning -> {
                    val advertisementData = AdvertisementData(advertisementDataMap)
                    val deviceInfo = DeviceInfoHolder(peripheral, central, advertisementData)
                    val device = Device(0, deviceInfo, rssi, DeviceConnectionManager.Builder(this@Scanner, central))
                    connectionManagerMap[device.identifier] = device.deviceConnectionManager
                    state.discoverDevices(device)
                }
                else -> state.logError(Error("Discovered Device while not scanning"))
            }
        }
    }


}