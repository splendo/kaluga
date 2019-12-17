package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattService
import android.os.ParcelUuid
import com.splendo.kaluga.bluetooth.device.DeviceInfoHolder

actual class Service(private val service: BluetoothGattService) : BaseService {

    override val uuid: UUID get() = UUID(ParcelUuid(service.uuid))

    override val peripheral: DeviceInfoHolder
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val characteristics: List<Characteristic>
        get() = service.characteristics.map { Characteristic(it) }
}