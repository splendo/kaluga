package com.splendo.kaluga.example

/*
Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.LocationServices
import com.splendo.kaluga.alerts.AlertBuilder
import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.example.shared.helloAndroid
import com.splendo.kaluga.example.shared.helloCommon
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationFlowable
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
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
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
                info.text = "You must grant the location permission for this example to work"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.show_alert -> {
                showAlert()
                true
            }
            R.id.dismissible_alert -> {
                showDismissibleAlert()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAlert() {
        AlertBuilder(this)
            .setTitle("Hello, Kaluga")
            .setPositiveButton("OK") { println("OK pressed") }
            .setNegativeButton("Cancel") { println("Cancel pressed") }
            .setNeutralButton("Details") { println("Details pressed") }
            .create()
            .show()
    }

    private fun showDismissibleAlert() {

        val presenter = AlertBuilder(this)
            .setTitle("Hello")
            .setMessage("Wait for 3 sec...")
            .setPositiveButton("OK") {}
            .create()

        presenter.show()

        Handler().postDelayed({
            presenter.dismiss()
        }, 3000)
    }

    private fun flowLocation() {
        val client = LocationServices.getFusedLocationProviderClient(
            this@LocationActivity
        )
        location.setFusedLocationProviderClient(client)
        
        LocationPrinter(location).printTo {
            info.text = "location: $it"
            info.animate().withEndAction {
                info.animate().setDuration(10000).alpha(0.12f).start()
            }.alpha(1f).setDuration(100).start()

        }
    }
}
