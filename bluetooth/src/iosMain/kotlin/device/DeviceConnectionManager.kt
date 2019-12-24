package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.toNSData
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.scanner.Scanner
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.darwin.NSObject

internal actual class DeviceConnectionManager(private val scanner: Scanner, private val cbCentralManager: CBCentralManager, reconnectionAttempts: Int, deviceInfoHolder: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>) : BaseDeviceConnectionManager(reconnectionAttempts, deviceInfoHolder, repoAccessor), CoroutineScope by repoAccessor.s {

    val peripheral = deviceInfoHolder.peripheral
    private var currentAction: DeviceAction? = null
    private val notifyingCharacteristics = emptyMap<String, Characteristic>().toMutableMap()

    class Builder(val scanner: Scanner, private val cbCentralManager: CBCentralManager) : BaseDeviceConnectionManager.Builder {
        override fun create(reconnectionAttempts: Int, deviceInfo: DeviceInfoHolder, repoAccessor: StateRepoAccesor<DeviceState>): DeviceConnectionManager {
            return DeviceConnectionManager(scanner, cbCentralManager, reconnectionAttempts, deviceInfo, repoAccessor)
        }
    }

    private class PeripheralDelegate(val connectionManager: DeviceConnectionManager) : NSObject(), CBPeripheralDelegateProtocol {



    }

    init {
        peripheral.delegate = PeripheralDelegate(this)
    }

    override suspend fun connect() {
        cbCentralManager.connectPeripheral(peripheral, null)
    }

    override suspend fun discoverServices() {
        peripheral.discoverServices(null)
    }

    override suspend fun disconnect() {
        cbCentralManager.cancelPeripheralConnection(peripheral)
    }

    override suspend fun readRssi() {
        peripheral.readRSSI()
    }

    override suspend fun performAction(action: DeviceAction): Boolean {
        currentAction = action
        when(action) {
            is DeviceAction.Read.Characteristic -> peripheral.readValueForCharacteristic(action.characteristic.characteristic)
            is DeviceAction.Read.Descriptor -> peripheral.readValueForDescriptor(action.descriptor.descriptor)
            is DeviceAction.Write.Characteristic -> {
                action.newValue?.toNSData()?.let {
                    peripheral.writeValue(it, action.characteristic.characteristic, CBCharacteristicWriteWithResponse)
                }
            }
            is DeviceAction.Write.Descriptor -> {
                action.newValue?.toNSData()?.let {
                    peripheral.writeValue(it, action.descriptor.descriptor)
                }
            }
            is DeviceAction.Notification -> {
                val uuid = action.characteristic.uuid.uuidString
                if (action.enable) {
                    notifyingCharacteristics[uuid] = action.characteristic
                } else notifyingCharacteristics.remove(uuid)
                peripheral.setNotifyValue(action.enable, action.characteristic.characteristic)
                // Action always completes. Launch in separate coroutine to make sure this action can be completed
                launch {
                    when(val state = repoAccessor.currentState()){
                        is DeviceState.Connected.HandlingAction -> state.actionCompleted()
                    }
                }
            }
        }
        return true
    }

    internal fun didConnect() {
        launch {
            when (val state = repoAccessor.currentState()) {
                is DeviceState.Connecting -> state.didConnect()
                is DeviceState.Reconnecting -> state.didConnect()
                is DeviceState.Connected -> {}
                else -> disconnect()
            }
        }
    }

    internal fun didDisconnect() {
        launch {
            when (val state = repoAccessor.currentState()) {
                is DeviceState.Reconnecting -> {
                    if (state.attempt < reconnectionAttempts) {
                        state.retry()
                        return@launch
                    }
                }
                is DeviceState.Connected -> {
                    if (reconnectionAttempts > 0) {
                        state.reconnect()
                        return@launch
                    }
                }
            }
            currentAction = null
            repoAccessor.currentState().didDisconnect()
        }
    }

}

