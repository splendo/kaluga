package com.splendo.kaluga.example.location

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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.location.LocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_main.info
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class LocationActivity : AppCompatActivity(R.layout.activity_location) {

    private var locationRequest: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enable_background.setOnClickListener {
            startService(Intent(applicationContext, LocationBackgroundService::class.java))
        }

        disable_background.setOnClickListener {
            stopService(Intent(applicationContext, LocationBackgroundService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        locationRequest?.cancel()
        locationRequest = lifecycle.coroutineScope.launch {
            val locationManagerBuilder = LocationManager.Builder(this@LocationActivity, inBackground = false)
            val locationPermissionStateRepo = LocationPermissionStateRepo(Permission.Location(
                background = false,
                precise = true
            ), LocationPermissionManagerBuilder(this@LocationActivity))
            val locationStateRepo = LocationStateRepo(locationPermissionStateRepo,
                autoRequestPermission = true,
                autoEnableLocations = true,
                locationManagerBuilder = locationManagerBuilder
            )
            val printer = LocationPrinter(locationStateRepo, this)
            printer.printTo {
                info.text = it
                info.animate().withEndAction {
                    info.animate().setDuration(10000).alpha(0.12f).start()
                }.alpha(1f).setDuration(100).start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationRequest?.cancel()
    }

}
