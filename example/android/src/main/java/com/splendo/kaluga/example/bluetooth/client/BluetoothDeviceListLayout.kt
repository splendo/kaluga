/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.bluetooth.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.NavHostComposableNavigator
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.BluetoothDeviceListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.DeviceDetails
import com.splendo.kaluga.resources.compose.Composable
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BluetoothDeviceListLayout(navHostController: StateFlow<NavHostController?>) {
    val viewmodel = koinViewModel<BluetoothDeviceListViewModel> {
        parametersOf(
            NavHostComposableNavigator<DeviceDetails>(navHostController) {
                it.next
            }
        )
    }

    ViewModelComposable(viewmodel) {

        var expanded by remember { mutableStateOf(false) }
        val isScanning by viewmodel.isScanning.state()
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Bluetooth Client") },
                    actions = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            if (isScanning) {
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Image(painterResource(R.drawable.ic_stop_circle), "Stop Scanning")
                                            Text("Stop Scanning")
                                        }
                                    },
                                    onClick = {
                                        viewmodel.onScanPressed()
                                        expanded = false
                                    }
                                )
                            } else {
                                DropdownMenuItem(
                                    text = {
                                        Row {
                                            Image(painterResource(R.drawable.ic_refresh_circle), "Start Scanning")
                                            Text("Start Scanning")
                                        }
                                    },
                                    onClick = {
                                        viewmodel.onScanPressed()
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                )

            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                val pairedDevices by viewmodel.pairedDevices.state()
                val scannedDevices by viewmodel.scannedDevices.state()
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Paired")
                    pairedDevices.forEach {
                        it.Layout()
                    }

                    Text("Scanned")
                    scannedDevices.forEach {
                        it.Layout()
                    }
                }
            }
        }
    }
}

@Composable
fun BluetoothDeviceListViewModel.DeviceViewModel.Layout() {
    val name by name.state()
    val rssi by rssi.state()
    val isTxPowerVisible by isTxPowerVisible.state()
    val txPower by txPower.state()
    val isConnectButtonVisible by isConnectButtonVisible.state()
    val connectButton by connectButton.state()
    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Text(name)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(identifierString)
                Text(rssi)
                if (isTxPowerVisible) {
                    Text(txPower)
                }
            }
        }
        if (isConnectButtonVisible) {
            connectButton.Composable()
        }
    }
}
