package com.splendo.kaluga.bluetooth.device

expect class Identifier

interface DeviceInfo {
    val name: String?
    val identifier: Identifier
    val advertisementData: AdvertisementData
}

expect class DeviceInfoHolder : DeviceInfo {

}