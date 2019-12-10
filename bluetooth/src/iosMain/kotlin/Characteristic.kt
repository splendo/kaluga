package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.toByteArray
import platform.CoreBluetooth.CBCharacteristic

actual class Characteristic(val characteristic: CBCharacteristic) : BaseCharacteristic {

    override val uuid: BaseUUID
        get() = UUID(characteristic.UUID)

    override fun getIsNotifying(): Boolean {
        return characteristic.isNotifying
    }

    override fun getData(): ByteArray? {
        return characteristic.value?.toByteArray()
    }
}

