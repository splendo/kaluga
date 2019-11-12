package com.splendo.kaluga.beacons

expect class BeaconScanner {

    fun addListener(beaconId: String, listener: Listener)

    fun removeListener(beaconId: String)
}