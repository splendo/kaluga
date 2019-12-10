package com.splendo.kaluga.bluetooth

import platform.CoreBluetooth.CBUUID

actual data class UUID(val uuid: CBUUID) : BaseUUID {

    override val uuidString: String
        get() = uuid.UUIDString

    override val isValid: Boolean
        get() = true
}

