package com.splendo.kaluga.bluetooth

interface BaseDevice {
    val uuid: List<UUID>
}

expect class Device : BaseDevice {

}
