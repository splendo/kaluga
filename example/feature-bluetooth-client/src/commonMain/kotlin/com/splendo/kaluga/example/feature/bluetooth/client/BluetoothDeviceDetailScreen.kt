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

package com.splendo.kaluga.example.feature.bluetooth.client

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.example.arch.DetailScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BluetoothDeviceDetailScreen(identifier: Identifier, onBack: () -> Unit) {
    val viewModel: BluetoothDeviceDetailViewModel = koinViewModel(key = identifier.stringValue) { parametersOf(identifier) }
    val info by viewModel.info.collectAsState()
    val deviceState by viewModel.deviceState.collectAsState()
    val distance by viewModel.distance.collectAsState()
    val currentHeartRate by viewModel.heartRate.collectAsState()
    val currentPosition by viewModel.position.collectAsState()

    val title = "${info?.name ?: identifier.stringValue} — ${identifier.stringValue}"

    DetailScaffold(
        title = title,
        onBack = { viewModel.disconnect(onBack) },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val stateLabel = when (deviceState) {
                is ConnectableDeviceState.Connected.Discovering -> "Discovering services…"
                is ConnectableDeviceState.Connected -> "Connected"
                is ConnectableDeviceState.Connecting -> "Connecting…"
                is ConnectableDeviceState.Disconnecting -> "Disconnecting…"
                is ConnectableDeviceState.Disconnected -> "Disconnected"
                is NotConnectableDeviceState -> "Not connectable"
                null -> "—"
            }
            Text("State: $stateLabel", fontWeight = FontWeight.SemiBold)

            val currentInfo = info
            if (currentInfo != null) {
                Text("RSSI: ${currentInfo.rssi} dBm")
                if (!distance.isNaN()) {
                    Text("Distance: ${"%.1f".format(distance)} m")
                }
            }

            HorizontalDivider()

            Text("Heart Rate Monitor", style = MaterialTheme.typography.titleMedium)
            val hr = currentHeartRate
            if (hr == null) {
                Text("Waiting for heart rate data…", style = MaterialTheme.typography.bodySmall)
            } else {
                Text("${hr.heartRate} bpm", style = MaterialTheme.typography.headlineMedium)
                if (hr.contactDetected) {
                    Text("Sensor contact detected")
                }
                val energy = hr.energyExpended
                if (energy != null) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Energy expended: $energy kJ")
                        OutlinedButton(onClick = viewModel::resetEnergy) {
                            Text("Reset")
                        }
                    }
                }
            }

            HorizontalDivider()

            Text("Sensor Location", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(currentPosition?.name ?: "Unknown")
                Button(onClick = viewModel::refreshPosition) {
                    Text("Refresh")
                }
            }
        }
    }
}
