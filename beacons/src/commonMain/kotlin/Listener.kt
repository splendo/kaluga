package com.splendo.kaluga.beacons

interface Listener {
    fun onStateUpdate(beaconId: String, isPresent: Boolean)
}