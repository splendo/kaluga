package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.typedList
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBService

actual class Service(private val service: CBService, private val stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseService {

    override val uuid: UUID get() = UUID(service.UUID)

    override val characteristics: List<Characteristic>
        get() = service.characteristics?.typedList<CBCharacteristic>()?.map { Characteristic(it, stateRepoAccesor) } ?: emptyList()
}