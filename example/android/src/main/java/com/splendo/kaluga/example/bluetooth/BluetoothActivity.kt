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
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BluetoothActivity : AppCompatActivity(R.layout.activity_bluetooth) {

    companion object {
        val bluetooth: Bluetooth = BluetoothBuilder().create()
    }
    private val bluetoothAdapter = BluetoothAdapter(bluetooth, lifecycle)

    private lateinit var isScanning: LiveData<Boolean>

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        devices_list.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        devices_list.adapter = bluetoothAdapter

        lifecycle.coroutineScope.launchWhenResumed {
            isScanning = bluetooth.isScanning().asLiveData()
            isScanning.observe(this@BluetoothActivity, Observer { invalidateOptionsMenu() })
        }

        lifecycle.coroutineScope.launchWhenResumed {
            bluetooth.devices().collect{ devices ->
                bluetoothAdapter.bluetoothDevices = devices
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bluetooth_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean = runBlocking {
        val scanning = isScanning.value ?: false
        menu?.forEach { item ->
            when(item.itemId) {
                R.id.start_scanning -> item.isVisible = !scanning
                R.id.stop_scanning -> item.isVisible = scanning
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.start_scanning -> {
                lifecycle.coroutineScope.launch {
                    bluetooth.startScanning()
                }
                true
            }
            R.id.stop_scanning -> {
                lifecycle.coroutineScope.launch {
                    bluetooth.stopScanning()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
