/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.location

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityLocationBinding
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.permissions.location.LocationPermission
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LocationActivity : KalugaViewModelActivity<LocationViewModel>() {

    companion object {
        private val locationPermission = LocationPermission(background = false, precise = true)
    }

    override val viewModel: LocationViewModel by viewModel {
        parametersOf(locationPermission)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLocationBinding.inflate(LayoutInflater.from(this), null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.enableBackground.setOnClickListener {
            startService(Intent(applicationContext, LocationBackgroundService::class.java))
        }

        binding.disableBackground.setOnClickListener {
            stopService(Intent(applicationContext, LocationBackgroundService::class.java))
        }

        viewModel.location.observeInitialized {
            val info = binding.info
            info.text = it
            info.animate().withEndAction {
                info.animate().setDuration(10000).alpha(0.12f).start()
            }.alpha(1f).setDuration(100).start()
        }
    }
}
