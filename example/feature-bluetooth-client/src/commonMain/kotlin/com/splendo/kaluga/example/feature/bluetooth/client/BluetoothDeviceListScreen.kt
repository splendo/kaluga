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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.stringValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BluetoothDeviceListScreen(onDeviceClick: (Identifier) -> Unit, modifier: Modifier = Modifier, viewModel: BluetoothDeviceListViewModel = koinViewModel()) {
    val pairedDevices by viewModel.pairedDevices.collectAsState()
    val scannedDevices by viewModel.scannedDevices.collectAsState()
    val scanning by viewModel.scanning.collectAsState()
    val enabled by viewModel.enabled.collectAsState()

    var showScanDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Bluetooth: ${if (enabled) "Enabled" else "Disabled"}",
                fontWeight = FontWeight.SemiBold,
            )
            Button(onClick = { showScanDialog = true }) {
                Text(if (scanning) "Stop Scanning" else "Start Scanning")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item { Text("Paired", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall) }
            if (pairedDevices.isEmpty()) {
                item { Text("None", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp)) }
            } else {
                items(pairedDevices, key = { it.identifier.stringValue + "_paired" }) { device ->
                    DeviceRow(device = device, viewModel = viewModel, onNavigate = { onDeviceClick(device.identifier) })
                }
            }

            item { Text("Scanned", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall) }
            if (scannedDevices.isEmpty()) {
                item { Text("None", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp)) }
            } else {
                items(scannedDevices, key = { it.identifier.stringValue }) { device ->
                    DeviceRow(device = device, viewModel = viewModel, onNavigate = { onDeviceClick(device.identifier) })
                }
            }
        }
    }

    if (showScanDialog) {
        ScanModeDialog(
            isScanning = scanning,
            onRetainAll = {
                viewModel.toggleScanning(BluetoothClient.CleanMode.RETAIN_ALL)
                showScanDialog = false
            },
            onRemoveAll = {
                viewModel.toggleScanning(BluetoothClient.CleanMode.REMOVE_ALL)
                showScanDialog = false
            },
            onDismiss = { showScanDialog = false },
        )
    }
}

@Composable
private fun ScanModeDialog(isScanning: Boolean, onRetainAll: () -> Unit, onRemoveAll: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isScanning) "Stop Scanning" else "Start Scanning") },
        text = { Text("Select clean mode") },
        confirmButton = {
            TextButton(onClick = onRetainAll) { Text("Retain All") }
        },
        dismissButton = {
            TextButton(onClick = onRemoveAll) { Text("Remove All") }
        },
    )
}

@Composable
private fun DeviceRow(device: ConnectableDevice, viewModel: BluetoothDeviceListViewModel, onNavigate: () -> Unit) {
    val info by device.info.collectAsState()
    val deviceState by device.state.collectAsState(initial = null as DeviceState?)

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(info.name ?: "Unknown", fontWeight = FontWeight.SemiBold)
                Text(info.identifier.stringValue, style = MaterialTheme.typography.bodySmall)
                info.rssi?.let { Text("RSSI: $it dBm") }
                info.advertisementData.txPowerLevel?.let {
                    Text("TX Power: $it dBm", style = MaterialTheme.typography.bodySmall)
                }
            }
            DeviceActions(
                deviceState = deviceState,
                onConnect = { viewModel.connect(device, onNavigate) },
                onDisconnect = { viewModel.disconnect(device) },
                onNavigate = onNavigate,
            )
        }
    }
}

@Composable
private fun DeviceActions(deviceState: DeviceState?, onConnect: () -> Unit, onDisconnect: () -> Unit, onNavigate: () -> Unit) {
    Column(horizontalAlignment = Alignment.End) {
        when (deviceState) {
            is ConnectableDeviceState.Connected -> {
                Button(onClick = onNavigate) { Text("Details") }
                OutlinedButton(onClick = onDisconnect) { Text("Disconnect") }
            }

            is ConnectableDeviceState.Disconnected -> {
                Button(onClick = onConnect) { Text("Connect") }
            }

            is ConnectableDeviceState.Connecting -> {
                Text("Connecting…", style = MaterialTheme.typography.bodySmall)
            }

            is ConnectableDeviceState.Disconnecting -> {
                Text("Disconnecting…", style = MaterialTheme.typography.bodySmall)
            }

            is NotConnectableDeviceState, null -> {}
        }
    }
}
