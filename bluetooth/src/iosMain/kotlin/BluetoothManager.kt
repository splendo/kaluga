package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.runBlocking
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue

actual class BluetoothManager : BaseBluetoothManager()  {

    private class BluetoothManagerDelegate(val bluetoothManager: BluetoothManager) : NSObject(), CBCentralManagerDelegateProtocol {

        override fun centralManagerDidUpdateState(central: CBCentralManager) {

        }

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            super.centralManager(central, didDiscoverPeripheral, advertisementData, RSSI)

            bluetoothManager.discoverPeripheral(didDiscoverPeripheral)
        }

    }

    private val delegate = BluetoothManagerDelegate(this)
    private val centralManager = CBCentralManager(delegate, dispatch_get_main_queue())

    suspend fun scan(filter: Set<CBUUID> = emptySet()) {
        scan(filter.map { UUID(it) }.toSet())
    }

    override fun handleStartScanning(filter: Set<UUID>) {
        val uuids = filter.map { it.uuid }
        centralManager.scanForPeripheralsWithServices(uuids, null)
    }

    override fun handleStopScanning() {
        centralManager.stopScan()
    }

    private fun discoverPeripheral(peripheral: CBPeripheral) {
        val device = Device(peripheral)
        runBlocking { discoveredDevice(device) }
    }
}