package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import android.os.ParcelUuid

actual class Characteristic(val characteristic: BluetoothGattCharacteristic) : BaseCharacteristic {

    override val uuid: UUID
        get() = UUID(ParcelUuid(characteristic.uuid))

    override fun getIsNotifying(): Boolean {
        return false
    }

    override fun getData(): ByteArray? {
        return characteristic.value
    }

}