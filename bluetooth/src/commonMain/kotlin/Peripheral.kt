package com.splendo.kaluga.bluetooth

enum class ConnectionState {
    CONNECTED,
    CONNECTING,
    DISCONNECTED,
    DISCONNECTING
}

interface BasePeripheral {

    interface Delegate {
        fun bluetoothPeripheralDidChangeConnectionState(peripheral: Peripheral, connectionState: ConnectionState, error: Error?)
        fun bluetoothPeripheralDidDiscoverServices(peripheral: Peripheral, error: Error?) {}
        fun bluetoothPeripheralDidDiscoverCharacteristics(peripheral: Peripheral, service: Service, error: Error?) {}
        fun bluetoothPeripheralDidWriteValueFor(peripheral: Peripheral, characteristic: Characteristic, error: Error?) {}
        fun bluetoothPeripheralOnDescriptorWrite(peripheral: Peripheral, error: Error?) {}
        fun bluetoothPeripheralOnDescriptorRead(peripheral: Peripheral, error: Error?) {}
        fun bluetoothPeripheralDidUpdateValue(peripheral: Peripheral, characteristic: Characteristic, error: Error?) {}
        fun bluetoothPeripheralDidUpdateNotificationStateFor(peripheral: Peripheral, characteristic: Characteristic, error: Error?) {}
        fun bluetoothPeripheralRssiUpdate(peripheral: Peripheral, rssi: Int, error: Error?) {}
    }

    val services: List<Service>
    val delegates: MutableList<Delegate>
    fun getName(): String?
    fun getConnectionState(): ConnectionState
    fun connect(): Boolean
    fun disconnect()
    fun refreshDeviceCache(): Boolean
    fun readRssi(): Boolean
    fun discoverServices(uuids: List<UUID>? = null)
    fun discoverCharacteristics(uuids: List<UUID>? = null)
    fun read(characteristic: Characteristic)
    fun setNotifyValue(isNotifying: Boolean, characteristic: Characteristic, descriptorUUID: UUID? = null)
    fun write(data: ByteArray, characteristic: Characteristic, withResponse: Boolean = false): PeripheralWriteResponse
}

expect class Peripheral : BasePeripheral {

}

expect class PeripheralWriteResponse
