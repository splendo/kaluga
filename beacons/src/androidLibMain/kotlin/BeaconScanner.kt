package com.splendo.kaluga.beacons

import android.content.Context
import android.os.RemoteException
import android.util.Log
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

actual class BeaconScanner(context: Context) {

    private val listeners = mutableMapOf<String, Listener>()

    private val lastKnownState = mutableMapOf<String, Boolean>()

    private val beaconManager by lazy {
        BeaconManager.getInstanceForApplication(context.applicationContext).apply {
            isRegionStatePersistenceEnabled = false
            beaconParsers.run {
                add(BeaconParser().setBeaconLayout(IBEACON_LAYOUT))
                add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
            }
            addRangeNotifier(::onRangeData)
        }
    }

    private val beaconConsumer = SimpleBeaconConsumer(context) {
        try {
            beaconManager.startRangingBeaconsInRegion(REGION_ANY)
        } catch (e: RemoteException) {
        }
    }

    actual fun addListener(beaconId: String, listener: Listener) {
        listeners[beaconId] = listener
        if (listeners.size == 1) {
            start()
        }
    }

    actual fun removeListener(beaconId: String) {
        listeners.remove(beaconId)
        lastKnownState.remove(beaconId)
        if (listeners.isEmpty()) {
            stop()
        }
    }

    @Synchronized
    private fun onRangeData(beacons: Collection<Beacon>, region: Region) {
        val ids = beacons.map { it.identifiers }.flatten().map { it.toString() }
        Log.d("BeaconScanner", "Found beacon IDs: ${ids.joinToString(",")}")
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

    private fun start() {
        beaconManager.bind(beaconConsumer)
    }

    private fun stop() {
        beaconManager.stopRangingBeaconsInRegion(REGION_ANY)
        beaconManager.unbind(beaconConsumer)
    }

    companion object {
        private const val IBEACON_LAYOUT = "m:2-3=215,i:4-19,i:20-21,i:22-23,p:24-24"
        private val REGION_ANY = Region("any-beacon", null, null, null)
    }
}