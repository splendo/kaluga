package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.Device

interface BaseService {
    val uuid: UUID
    val peripheral: Device
    val characteristics: List<Characteristic>
}

expect class Service : BaseService