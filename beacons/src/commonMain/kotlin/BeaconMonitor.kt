package com.splendo.kaluga.beacons

class BeaconMonitor(private val scanner: BeaconScanner) {

    fun subscribe(beaconId: String, callback: (isPresent: Boolean) -> Unit) = scanner.addListener(beaconId, object : Listener {
        override fun onStateUpdate(beaconId: String, isPresent: Boolean) = callback(isPresent)
    })

    fun unsubscribe(beaconId: String) = scanner.removeListener(beaconId)
}



