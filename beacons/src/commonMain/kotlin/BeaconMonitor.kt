package com.splendo.kaluga.beacons

class BeaconMonitor(private val scanner: BeaconScanner) {

    fun scan(beaconId: String) = scanner.addListener(beaconId, object : Listener {
        override fun onStateUpdate(beaconId: String, isPresent: Boolean) {
            print("BeaconMonitor.onStateUpdate $beaconId $isPresent")
        }
    })
}



