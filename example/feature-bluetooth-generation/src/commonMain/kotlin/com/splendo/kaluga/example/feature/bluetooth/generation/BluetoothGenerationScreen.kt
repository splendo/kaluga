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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.splendo.kaluga.bluetooth.device.stringValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GenerationScreen(viewModel: GenerationViewModel = koinViewModel()) {
    var tab by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = tab) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Client") })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Server") })
        }
        when (tab) {
            0 -> ClientTab(viewModel)
            else -> ServerTab(viewModel)
        }
    }
}

@Composable
private fun ClientTab(viewModel: GenerationViewModel) {
    val selected by viewModel.selected.collectAsState()
    val connecting by viewModel.connecting.collectAsState()
    val error by viewModel.error.collectAsState()
    val current = selected
    when {
        current != null -> {
            Column(Modifier.fillMaxSize()) {
                TextButton(onClick = { viewModel.deselect() }, modifier = Modifier.padding(horizontal = 8.dp)) { Text("‹ Devices") }
                ClientView(viewModel(key = current.key) { ClientViewModel(current.client) })
            }
        }

        connecting -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator()
                    Text("Connecting…")
                }
            }
        }

        else -> {
            DisposableEffect(Unit) {
                viewModel.startScanning()
                onDispose { viewModel.stopScanning() }
            }
            val scanned by viewModel.scannedDevices.collectAsState()
            val message = error
            Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Devices", style = MaterialTheme.typography.titleMedium)
                if (message != null) {
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
                Button(onClick = { viewModel.selectSimulated() }, modifier = Modifier.fillMaxWidth()) { Text("Simulated") }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(scanned) { device ->
                        Button(onClick = { viewModel.connect(device) }, modifier = Modifier.fillMaxWidth()) {
                            Text(device.identifier.stringValue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ServerTab(viewModel: GenerationViewModel) {
    val reading by viewModel.reading.collectAsState()
    val name by viewModel.name.collectAsState()
    val lastThreshold by viewModel.lastThresholdWritten.collectAsState()
    val liveSubscribers by viewModel.liveSubscriberCount.collectAsState()
    val statusSubscribers by viewModel.statusSubscriberCount.collectAsState()
    var liveInput by remember { mutableStateOf("0") }
    var statusInput by remember { mutableStateOf("0") }

    DisposableEffect(Unit) {
        viewModel.startServer()
        onDispose { viewModel.stopServer() }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = reading.toString(),
            onValueChange = { it.toIntOrNull()?.let(viewModel::setReading) },
            label = { Text("reading (served on read)") },
        )
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::setName,
            label = { Text("name (descriptor)") },
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = liveInput, onValueChange = { liveInput = it }, label = { Text("live value") }, modifier = Modifier.weight(1f))
            Button(onClick = { liveInput.toShortOrNull()?.let(viewModel::pushLive) }) { Text("Notify") }
        }
        Text("live subscribers (real + simulated): $liveSubscribers")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = statusInput, onValueChange = { statusInput = it }, label = { Text("status value") }, modifier = Modifier.weight(1f))
            Button(onClick = { statusInput.toShortOrNull()?.let(viewModel::pushStatus) }) { Text("Indicate") }
        }
        Text("status subscribers (real + simulated): $statusSubscribers")
        Text("last threshold written = ${lastThreshold ?: "-"}")
    }
}
