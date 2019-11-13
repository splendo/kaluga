package com.splendo.kaluga.beacons

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import org.altbeacon.beacon.BeaconConsumer

class SimpleBeaconConsumer(
    private val context: Context,
    private val onConnect: () -> Unit
) : BeaconConsumer {

    override fun getApplicationContext() = context

    override fun unbindService(p0: ServiceConnection?) = Unit

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int) = false

    override fun onBeaconServiceConnect() {
        onConnect()
    }
}