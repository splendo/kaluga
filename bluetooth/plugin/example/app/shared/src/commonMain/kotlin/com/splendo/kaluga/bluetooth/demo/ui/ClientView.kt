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
import com.splendo.kaluga.bluetooth.demo.DemoDeviceClient
import com.splendo.kaluga.bluetooth.demo.RemoteConfigCharacteristic
import com.splendo.kaluga.bluetooth.demo.SensorCharacteristicReadResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientViewModel(private val client: DemoDeviceClient) : ViewModel() {

    // Eagerly (not WhileSubscribed) so the notify/indicate subscription persists while this view is off-screen
    // — e.g. when the simulator's Server tab is shown — instead of being torn down on every tab switch.
    val live = client.demoService.sensor.live.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val status = client.demoService.config.status.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _reading = MutableStateFlow<Int?>(null)
    val reading = _reading.asStateFlow()
    private val _name = MutableStateFlow<String?>(null)
    val name = _name.asStateFlow()

    fun readReading() = viewModelScope.launch {
        _reading.value = (client.demoService.sensor.readReading() as? SensorCharacteristicReadResponse.Success)?.response
    }

    fun readName() = viewModelScope.launch {
        _name.value = (client.demoService.config.info.readName() as? RemoteConfigCharacteristic.InfoReadResponse.Success)?.response
    }

    fun writeThreshold(threshold: Int) = viewModelScope.launch {
        client.demoService.config.writeThreshold(threshold)
    }
}

@Composable
fun ClientView(viewModel: ClientViewModel, modifier: Modifier = Modifier) {
    val live by viewModel.live.collectAsState()
    val status by viewModel.status.collectAsState()
    val reading by viewModel.reading.collectAsState()
    val name by viewModel.name.collectAsState()
    var threshold by remember { mutableStateOf("0") }

    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Client", style = MaterialTheme.typography.titleLarge)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.readReading() }) { Text("Read reading") }
            Text("reading = ${reading ?: "-"}")
        }
        Text("live (notify) = ${live ?: "-"}")
        Text("status (indicate) = ${status ?: "-"}")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = threshold, onValueChange = { threshold = it }, label = { Text("threshold") })
            Button(onClick = { threshold.toIntOrNull()?.let { viewModel.writeThreshold(it) } }) { Text("Write") }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { viewModel.readName() }) { Text("Read name") }
            Text("name = ${name ?: "-"}")
        }
    }
}
