package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattCharacteristic

actual class Characteristic(val characteristic: BluetoothGattCharacteristic) : BaseCharacteristic {

    override val uuid: UUID
        get() = UUID(characteristic.uuid)

    override fun getIsNotifying(): Boolean {
        return false
    }

    override fun getData(): ByteArray? {
        return characteristic.value
    }

}