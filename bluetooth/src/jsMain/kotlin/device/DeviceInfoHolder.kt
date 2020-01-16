package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.UUID

actual typealias Identifier = UUID

actual class DeviceInfoHolder : DeviceInfo {

    override val name: String?
        get() = null
    override val identifier: Identifier
        get() = UUID("")
    override val advertisementData: AdvertisementData
        get() = AdvertisementData()
}