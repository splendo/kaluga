package com.splendo.mpp.example

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.LocationServices
import com.splendo.mpp.example.shared.helloAndroid
import com.splendo.mpp.location.Location
import com.splendo.mpp.location.LocationFlowable
import helloCommon
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_main.info

@SuppressLint("SetTextI18n")
class LocationActivity : AppCompatActivity() {

    private val location = LocationFlowable()

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            flowLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE);
        }

        info.text = helloAndroid() + " " + helloCommon()
    }

    companion object {const val LOCATION_PERMISSION_REQUEST_CODE = 1}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] === Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                flowLocation()
            } else {
                info.setText("You must grant the location permission for this example to work")
            }
        }
    }


    private fun flowLocation() {
        lifecycle.coroutineScope.launch {
            val client = LocationServices.getFusedLocationProviderClient(
                this@LocationActivity
            )

            location.set(Location.UnknownLocationWithNoLastLocation(Location.UnknownReason.NOT_CLEAR))
            location.setFusedLocationProviderClient(client)
            location.flow().collect { value ->
                info.text = "location: $value"
                info.animate().withEndAction {
                    info.animate().setDuration(10000).alpha(0.12f).start()
                }.alpha(1f).setDuration(100).start()

            }
        }
    }
}
