package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder

interface BaseService {
    val uuid: UUID
    val peripheral: DeviceInfoHolder
    val characteristics: List<Characteristic>
}

expect class Service : BaseService