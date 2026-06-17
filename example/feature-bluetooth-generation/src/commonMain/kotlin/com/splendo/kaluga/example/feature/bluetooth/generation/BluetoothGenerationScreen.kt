/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.bluetooth.generation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun GenerationMenuScreen(onClient: () -> Unit, onServer: () -> Unit, onSimulator: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pick a mode", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onClient, modifier = Modifier.fillMaxWidth()) { Text("Bluetooth Client") }
        Button(onClick = onServer, modifier = Modifier.fillMaxWidth()) { Text("Bluetooth Server") }
        Button(onClick = onSimulator, modifier = Modifier.fillMaxWidth()) { Text("Simulator") }
    }
}

class ClientModeViewModel(private val client: BluetoothClient) : ViewModel() {
    val devices = client.devices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _connected = MutableStateFlow<DemoDeviceClient?>(null)
    val connected = _connected.asStateFlow()

    fun startScanning() = client.startScanning(filter = setOf(RemoteDemoService.UUID))
    fun stopScanning() = client.stopScanning()

    fun connect(device: ConnectableDevice) = viewModelScope.launch {
        if (device.connect()) {
            _connected.value = DemoDeviceClient.bluetooth(client, device.identifier)
        }
    }
}

@Composable
internal fun ClientModeScreen(viewModel: ClientModeViewModel = koinViewModel()) {
    val devices by viewModel.devices.collectAsState()
    val connected by viewModel.connected.collectAsState()

    val api = connected
    if (api != null) {
        ClientView(viewModel { ClientViewModel(api) })
    } else {
        LaunchedScanning(viewModel)
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Scanning for KalugaDemo…", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(devices) { device ->
                    Button(onClick = { viewModel.connect(device) }, modifier = Modifier.fillMaxWidth()) {
                        Text(device.identifier.stringValue)
                    }
                }
            }
        }
    }
}

@Composable
private fun LaunchedScanning(viewModel: ClientModeViewModel) {
    DisposableEffect(Unit) {
        viewModel.startScanning()
        onDispose { viewModel.stopScanning() }
    }
}

@Composable
internal fun ServerModeScreen() {
    val builder = koinInject<BluetoothServerBuilder>()
    val delegate = koinInject<DemoDeviceServer.Delegate>()
    val state = koinInject<DemoServerState>()
    var server: DemoDeviceServer? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        server = DemoDeviceServer.bluetooth(builder, delegate)
    }
    val s = server
    if (s == null) {
        Text("Starting server…", Modifier.padding(16.dp))
    } else {
        ServerView(viewModel { ServerViewModel(s, state) })
    }
}

@Composable
internal fun SimulatorModeScreen() {
    val delegate = koinInject<DemoDeviceServer.Delegate>()
    val state = koinInject<DemoServerState>()
    val simServer = remember { DemoDeviceServer.simulated(delegate) }
    val simClient = remember { DemoDeviceClient.simulated(randomIdentifier(), simServer) }
    var tab by remember { mutableStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = tab) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Client") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Server") })
        }
        when (tab) {
            0 -> ClientView(viewModel(key = "simulatedClient") { ClientViewModel(simClient) })
            else -> ServerView(viewModel(key = "simulatedServer") { ServerViewModel(simServer, state) })
        }
    }
}
