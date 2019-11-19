package com.splendo.kaluga.beacons

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class BeaconScanner {

    fun addListener(beaconId: String, listener: Listener)

    fun removeListener(beaconId: String)
}