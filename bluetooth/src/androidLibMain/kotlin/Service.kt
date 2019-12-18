package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattService
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.state.StateRepoAccesor

actual class Service(private val service: BluetoothGattService, private val stateRepoAccesor: StateRepoAccesor<DeviceState>) : BaseService {

    override val uuid: UUID get() = UUID(ParcelUuid(service.uuid))

    override val characteristics: List<Characteristic>
        get() = service.characteristics.map { Characteristic(it, stateRepoAccesor) }
}