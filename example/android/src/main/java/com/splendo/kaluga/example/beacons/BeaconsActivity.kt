package com.splendo.kaluga.example.beacons

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.splendo.kaluga.beacons.BeaconMonitor
import com.splendo.kaluga.beacons.BeaconScanner
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.location.LocationActivity
import kotlinx.android.synthetic.main.activity_beacons.*

@SuppressLint("SetTextI18n")
class BeaconsActivity : AppCompatActivity(R.layout.activity_beacons) {

    private val beaconMonitor by lazy { BeaconMonitor(BeaconScanner(applicationContext)) }

    private fun enableBeaconMonitor() {
        status.text = "Beacon status: unknown"
        beaconMonitor.subscribe(EXPECTED_BEACON_ID) { isPresent ->
            status.text = "Beacon status: ${if (isPresent) "found" else "not found"}"
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableBeaconMonitor()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LocationActivity.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onStop() {
        super.onStop()
        beaconMonitor.unsubscribe(EXPECTED_BEACON_ID)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            val resultLocation = if (Manifest.permission.ACCESS_FINE_LOCATION in permissions) {
                grantResults[permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)]
            } else {
                PackageManager.PERMISSION_DENIED
            }

            if (resultLocation == PackageManager.PERMISSION_GRANTED) {
                enableBeaconMonitor()
            } else {
                status.text = "You must grant the location permission for this example to work"
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_LOCATION = 1
        private const val EXPECTED_BEACON_ID = "f7826da6-4fa2-4e98-8024-bc5b71e0893e"
    }
}