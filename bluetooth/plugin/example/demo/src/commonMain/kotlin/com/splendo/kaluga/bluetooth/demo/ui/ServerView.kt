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

package com.splendo.kaluga.bluetooth.demo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.bluetooth.demo.DemoDeviceServer
import com.splendo.kaluga.bluetooth.demo.DemoServerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServerViewModel(private val server: DemoDeviceServer, private val state: DemoServerState) : ViewModel() {

    val reading = state.reading
    val name = state.name
    val lastThresholdWritten = state.lastThresholdWritten

    val liveSubscribers = server.demoService.sensor.liveSubscribers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val statusSubscribers = server.demoService.config.statusSubscribers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setReading(value: Int) { state.reading.value = value }
    fun setName(value: String) { state.name.value = value }

    fun pushLive(value: Short) = viewModelScope.launch {
        server.demoService.sensor.notifyAllLiveChanged(value)
    }

    fun pushStatus(value: Short) = viewModelScope.launch {
        server.demoService.config.notifyAllStatusChanged(value)
    }
}

@Composable
fun ServerView(viewModel: ServerViewModel, modifier: Modifier = Modifier) {
    val reading by viewModel.reading.collectAsState()
    val name by viewModel.name.collectAsState()
    val lastThreshold by viewModel.lastThresholdWritten.collectAsState()
    val liveSubscribers by viewModel.liveSubscribers.collectAsState()
    val statusSubscribers by viewModel.statusSubscribers.collectAsState()
    var liveInput by remember { mutableStateOf("0") }
    var statusInput by remember { mutableStateOf("0") }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Server", style = MaterialTheme.typography.titleLarge)
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
        Text("live subscribers: ${liveSubscribers.size}")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = statusInput, onValueChange = { statusInput = it }, label = { Text("status value") }, modifier = Modifier.weight(1f))
            Button(onClick = { statusInput.toShortOrNull()?.let(viewModel::pushStatus) }) { Text("Indicate") }
        }
        Text("status subscribers: ${statusSubscribers.size}")
        Text("last threshold written = ${lastThreshold ?: "-"}")
    }
}
