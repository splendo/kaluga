package com.splendo.kaluga.example.beacons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.splendo.kaluga.beacons.BeaconMonitor
import com.splendo.kaluga.beacons.BeaconScanner
import com.splendo.kaluga.example.R

class BeaconsActivity : AppCompatActivity(R.layout.activity_beacons) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BeaconMonitor(BeaconScanner()).scan(EXPECTED_BEACON_ID)
    }

    companion object {
        private const val EXPECTED_BEACON_ID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e"
    }
}