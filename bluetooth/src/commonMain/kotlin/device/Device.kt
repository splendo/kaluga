package com.splendo.kaluga.bluetooth.device


expect class Identifier

interface BaseDevice {
    val name: String?
    val identifier: Identifier
    val advertisementData: AdvertisementData
}

expect class Device : BaseDevice {

}