package com.splendo.kaluga.bluetooth

import android.bluetooth.*
import android.content.Context
import android.os.Build
import com.splendo.kaluga.base.runOnMain
import com.splendo.kaluga.log.LogLevel
import com.splendo.kaluga.log.logger
import java.lang.Exception
import android.bluetooth.BluetoothGatt



actual class Peripheral(private val device: BluetoothDevice, private val context: Context) : BasePeripheral {

    companion object {
        val tag = "BTPeripheral"
        val refreshMethod = "refresh"
        val unpairMethod = "removeBond"
    }

    private var connection: BluetoothGatt? = null
    private var connectionState: ConnectionState = ConnectionState.DISCONNECTED
    private val connectionCallback = GattCallback(this)

    override val delegates: MutableList<BasePeripheral.Delegate> = emptyList<BasePeripheral.Delegate>().toMutableList()
    override val services: MutableList<Service> = emptyList<Service>().toMutableList()
    private val cache = CharacteristicCache()

    private var servicesToDiscover: List<UUID>? = null
    private var characteristicsToDiscover: List<UUID>? = null

    override fun getName(): String? {
        return device.name
    }

    override fun getConnectionState(): ConnectionState {
        return connectionState
    }

    override fun connect(): Boolean {
        // Unpair to prevent connection problems
        unpair()
        connection?.let { connection ->
            return connection.connect()
        } ?: run {
            val newConnection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                device.connectGatt(context, false, connectionCallback, BluetoothDevice.TRANSPORT_LE)
            } else {
                device.connectGatt(context, false, connectionCallback)
            }
            this.connection = newConnection
            return true
        }
    }

    override fun disconnect() {
        val connection = connection ?: return
        connection.disconnect()
        connection.close()
        this.connection = null
    }

    override fun refreshDeviceCache(): Boolean {
        return connection?.let {
            //workaround for https://code.google.com/p/android/issues/detail?id=81130
            try {
                val localMethod = it.javaClass.getMethod(refreshMethod, *arrayOfNulls(0))
                (localMethod.invoke(it, arrayOfNulls<Any>(0)) as Boolean)
            } catch (e: Exception) {
                logger().log(LogLevel.ERROR, tag, "Failed to refresh ${e.message}")
                false
            }
        } ?: false
    }

    override fun readRssi(): Boolean {
        return connection?.readRemoteRssi() ?: false
    }

    override fun discoverServices(uuids: List<UUID>?) {
        servicesToDiscover = uuids
        connection?.discoverServices()
    }

    override fun discoverCharacteristics(uuids: List<UUID>?) {
        characteristicsToDiscover = uuids
    }

    override fun read(characteristic: Characteristic) {
        connection?.readCharacteristic(characteristic.characteristic)
    }

    override fun setNotifyValue(isNotifying: Boolean, characteristic: Characteristic, descriptorUUID: UUID?) {
        val connection = connection ?: return

        val base = characteristic.characteristic

        val stateUpdatesUUID = descriptorUUID?.uuid ?: return
        val descriptor = base.getDescriptor(stateUpdatesUUID) ?: return
        val value = if (isNotifying) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        descriptor.value = value

        connection.setCharacteristicNotification(base, isNotifying)
        connection.writeDescriptor(descriptor)
    }

    override fun write(data: ByteArray, characteristic: Characteristic, withResponse: Boolean): PeripheralWriteResponse {
        val connection = connection ?: return PeripheralWriteResponse(false)
        val base = characteristic.characteristic

        base.value = data

        val writeType = if (withResponse) BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT else BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        base.writeType = writeType

        val didWrite = connection.writeCharacteristic(characteristic.characteristic)
        return PeripheralWriteResponse(didWrite)
    }

    private fun unpair() {
        if (device.bondState != BluetoothDevice.BOND_NONE) {
            try {
                device::class.java.getMethod(unpairMethod).invoke(device)
            } catch (e: Exception)  {
                logger().log(LogLevel.ERROR, tag, "Removing failed ${e.message}")
            }
        }
    }

    private fun notifyDelegates(action: (BasePeripheral.Delegate) ->  Unit) {
        delegates.forEach(action)
    }

    private fun notifyCharacteristic(characteristic: BluetoothGattCharacteristic, action: (characteristic: Characteristic, BasePeripheral.Delegate) -> Unit) {
        if (characteristicsToDiscover?.map { it.uuid }?.contains(characteristic.uuid) == false)
            return
        val btCharacteristic = cache.adapter(UUID(characteristic.uuid)) ?: return
        delegates.forEach { action(btCharacteristic, it) }

    }

    private class GattCallback(private val peripheral: Peripheral) : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            runOnMain {
                val error = processStatus(status, null) { peripheral.disconnect() }

                peripheral.connectionState = when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> ConnectionState.CONNECTED
                    BluetoothProfile.STATE_CONNECTING -> ConnectionState.CONNECTING
                    BluetoothProfile.STATE_DISCONNECTED -> ConnectionState.DISCONNECTED
                    BluetoothProfile.STATE_DISCONNECTING -> ConnectionState.DISCONNECTING
                    else -> ConnectionState.DISCONNECTED
                }

                peripheral.notifyDelegates { it.bluetoothPeripheralDidChangeConnectionState(peripheral, peripheral.connectionState, error) }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            runOnMain {
                super.onServicesDiscovered(gatt, status)
                peripheral.services.clear()
                peripheral.services.addAll( gatt?.services?.
                    filter { peripheral.servicesToDiscover?.contains(UUID(it.uuid)) ?: true }?.
                    map { Service(it) } ?: emptyList())

                peripheral.services.forEach { service ->
                    service.characteristics.forEach { peripheral.cache.cache(it) }
                }

                peripheral.notifyDelegates{it.bluetoothPeripheralDidDiscoverServices(peripheral, null)}
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            runOnMain {
                val error = processStatus(status)
                peripheral.notifyDelegates { it.bluetoothPeripheralOnDescriptorWrite(peripheral, error) }
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
            runOnMain {
                val error = processStatus(status)
                peripheral.notifyDelegates { it.bluetoothPeripheralOnDescriptorRead(peripheral, error) }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            runOnMain {
                characteristic?.let {
                    peripheral.notifyCharacteristic(it) {characteristic, delegate -> delegate.bluetoothPeripheralDidUpdateValue(peripheral, characteristic, null)}
                }
            }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            runOnMain {
                val error = processStatus(status)
                characteristic?.let {
                    peripheral.notifyCharacteristic(it) {characteristic, delegate -> delegate.bluetoothPeripheralDidWriteValueFor(peripheral, characteristic, error)}
                }
            }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            runOnMain {
                val error = processStatus(status)
                characteristic?.let {
                    peripheral.notifyCharacteristic(it) {characteristic, delegate -> delegate.bluetoothPeripheralDidUpdateValue(peripheral, characteristic, error)}
                }
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            runOnMain {
                super.onReadRemoteRssi(gatt, rssi, status)

                val error = processStatus(status)
                peripheral.notifyDelegates { it.bluetoothPeripheralRssiUpdate(peripheral, rssi, error) }
            }
        }

        fun processStatus(status: Int, onSuccess: (() -> Unit)? = null, onError: (() -> Unit)? = null): Error? {
            return when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    onSuccess?.let { it() }
                    null
                }
                else -> {
                    onError?.let { it() }
                    Error(status.toString())
                }
            }
        }
    }

}

actual data class PeripheralWriteResponse(val didWrite: Boolean)
