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

package com.splendo.kaluga.example.bluetooth

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.ActivityBluetoothBinding
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.DeviceDetails
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BluetoothActivity : KalugaViewModelActivity<BluetoothListViewModel>() {

    override val viewModel: BluetoothListViewModel by viewModel {
        parametersOf(
            ActivityNavigator<DeviceDetails> {
                NavigationSpec.Activity<BluetoothMoreActivity>()
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityBluetoothBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        val pairingAdapter = BluetoothAdapter(this)
        binding.pairedDevicesList.adapter = pairingAdapter
        binding.pairedDevicesList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val scanningAdapter = BluetoothAdapter(this)
        binding.scannedDevicesList.adapter = scanningAdapter
        binding.scannedDevicesList.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        viewModel.isScanning.observe {
            invalidateOptionsMenu()
        }

        viewModel.title.observe(::setTitle)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bluetooth_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean = runBlocking {
        val scanning = viewModel.isScanning.current
        menu?.forEach { item ->
            when (item.itemId) {
                R.id.start_scanning -> item.isVisible = !scanning
                R.id.stop_scanning -> item.isVisible = scanning
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.start_scanning,
            R.id.stop_scanning,
            -> {
                viewModel.onScanPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
