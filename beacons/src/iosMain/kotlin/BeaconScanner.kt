package com.splendo.kaluga.beacons

actual class BeaconScanner {
    actual fun addListener(beaconId: String, listener: Listener) {
        println("BeaconScanner.addListener on iOS")
    }

    actual fun removeListener(beaconId: String) {
        println("BeaconScanner.removeListener on iOS")
    }
}