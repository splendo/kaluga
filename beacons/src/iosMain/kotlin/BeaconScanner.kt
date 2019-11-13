package com.splendo.kaluga.beacons

import platform.CoreLocation.*
import platform.Foundation.*
import platform.darwin.NSObject

actual class BeaconScanner(private val locationManager: CLLocationManager) {

    private val listeners = mutableMapOf<String, Listener>()
    private val lastKnownState = mutableMapOf<String, Boolean>()

    init {
        locationManager.delegate = LocationDelegate()
    }

    actual fun addListener(beaconId: String, listener: Listener) {
        println("BeaconScanner.addListener on iOS")
        if (CLLocationManager.isMonitoringAvailableForClass(CLBeaconRegion)) {
            val regionUUID = NSUUID(beaconId)
            val region = CLBeaconRegion(regionUUID, beaconId)
            locationManager.startMonitoringForRegion(region)
            locationManager.startRangingBeaconsInRegion(region)
            listeners[beaconId.toLowerCase()] = listener
        }
    }

    actual fun removeListener(beaconId: String) {
        println("BeaconScanner.removeListener on iOS")
        val regionUUID = NSUUID(beaconId)
        val region = CLBeaconRegion(regionUUID, beaconId)
        listeners.remove(beaconId.toLowerCase())
        locationManager.stopRangingBeaconsInRegion(region)
        locationManager.stopMonitoringForRegion(region)
    }

    @Suppress("CONFLICTING_OVERLOADS")
    inner class LocationDelegate : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(manager: CLLocationManager, didRangeBeacons: List<*>, inRegion: CLBeaconRegion) {
            println("LocationDelegate.locationManager didRangeBeacons $didRangeBeacons inRegion $inRegion")
            val ids = didRangeBeacons.filterIsInstance<CLBeacon>().map { it.proximityUUID.toString().toLowerCase() }
            ids.subtract(lastKnownState.keys).forEach {
                if (lastKnownState[it] != true) {
                    lastKnownState[it] = true
                    listeners[it]?.onStateUpdate(it, true)
                }
            }
            lastKnownState.keys.subtract(ids).forEach {
                if (lastKnownState[it] != false) {
                    lastKnownState[it] = false
                    listeners[it]?.onStateUpdate(it, false)
                }
            }
        }
    }
}