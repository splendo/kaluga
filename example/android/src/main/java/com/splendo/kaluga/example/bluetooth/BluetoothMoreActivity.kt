/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.bluetooth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.activity_bluetooth_more.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.fixedRateTimer

class BluetoothMoreActivity : AppCompatActivity(R.layout.activity_bluetooth_more) {

    companion object {
        const val IDENTIFIER_KEY = "IDENTIFIER_KEY"
        private const val rssi_frequency = 1000L
    }

    private lateinit var identifier: Identifier
    private var timer: Timer? = null

    val device = lazy{BluetoothActivity.bluetooth.devices()[identifier]}

    @InternalCoroutinesApi
    fun <T> handleDevice(transform: suspend (Flow<Device?>) -> Flow<T>, collector: (T) -> Unit)  {
        lifecycle.coroutineScope.launchWhenResumed {
            transform(device.value).collect{collector(it)}
        }
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        identifier = intent.extras?.getString(IDENTIFIER_KEY) ?: run {
            finish()
            return
        }

        identifier_field.text = identifier

        lifecycle.coroutineScope.launch {
            device.value.info().map { it.name }.asLiveData().observe({lifecycle}) { name.text = it ?: getText(R.string.bluetooth_no_name) }
            device.value.rssi().asLiveData().observe({lifecycle}) {rssi.text = getString(R.string.rssi).format(it)}
            device.value.distance().asLiveData().observe({lifecycle}) {distance_field.text = getString(R.string.distance).format(it)}
            device.value.state().map{deviceState -> when(deviceState) {
                is DeviceState.Disconnecting -> R.string.bluetooth_disconneting
                is DeviceState.Disconnected -> R.string.bluetooth_disconnected
                is DeviceState.Connected.Discovering -> R.string.bluetooth_discovering
                is DeviceState.Connected -> R.string.bluetooth_connected
                is DeviceState.Connecting -> R.string.bluetooth_connecting
                is DeviceState.Reconnecting -> R.string.bluetooth_reconnecting
            }}.asLiveData().observe({lifecycle}) {status.text = getString(it)}

            service_list.adapter = BluetoothServiceAdapter(device.value.services(), lifecycle)
        }
    }

    override fun onResume() {
        super.onResume()
        timer?.cancel()
        timer = fixedRateTimer(period = rssi_frequency) {
            lifecycle.coroutineScope.launch {
                device.value.updateRssi()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }
}
