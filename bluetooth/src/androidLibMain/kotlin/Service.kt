package com.splendo.kaluga.bluetooth

import android.bluetooth.BluetoothGattService

actual class Service(private val service: BluetoothGattService) : BaseService {

    override val uuid: UUID get() = UUID(service.uuid)

    override val peripheral: Device
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val characteristics: List<Characteristic>
        get() = service.characteristics.map { Characteristic(it) }
}