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

package com.splendo.kaluga.example.feature.beacons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.bluetooth.beacons.BeaconInfo
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BeaconsScreen(modifier: Modifier = Modifier, viewModel: BeaconsViewModel = koinViewModel()) {
    val isScanning by viewModel.isScanning.collectAsState()
    val beacons by viewModel.beacons.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = viewModel::toggleMonitoring,
        ) {
            Text(if (isScanning) "Stop Monitoring" else "Start Monitoring")
        }
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(beacons, key = { it.identifier }) { beacon ->
                BeaconRow(beacon)
            }
        }
    }
}

@Composable
private fun BeaconRow(beacon: BeaconInfo) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Namespace: ${beacon.beaconID.namespace}", fontWeight = FontWeight.SemiBold)
            Text("Instance: ${beacon.beaconID.instance}")
            Text("Tx Power: ${beacon.txPower} dBm")
        }
    }
}
