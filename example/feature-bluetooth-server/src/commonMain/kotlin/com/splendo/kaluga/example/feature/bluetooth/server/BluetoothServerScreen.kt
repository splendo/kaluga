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

package com.splendo.kaluga.example.feature.bluetooth.server

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.splendo.kaluga.base.formatting.NumberFormatStyle
import com.splendo.kaluga.base.formatting.NumberFormatter
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import com.splendo.kaluga.scientific.formatter.CommonScientificValueFormatter
import org.koin.compose.viewmodel.koinViewModel

private val valueFormatter = CommonScientificValueFormatter.with(builder = {
    defaultValueFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U)).apply {
        notANumberSymbol = "--"
    }
})

@Composable
fun BluetoothServerScreen(modifier: Modifier = Modifier, viewModel: BluetoothServerViewModel = koinViewModel()) {
    val status by viewModel.status.collectAsState()
    val bpm by viewModel.bpm.collectAsState()
    val energy by viewModel.energy.collectAsState()
    val sensor by viewModel.position.collectAsState()

    var pickerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Status: $status", fontWeight = FontWeight.SemiBold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = viewModel::decrementBpm) { Text("-") }
            Text("BPM: ${valueFormatter.format(bpm)}", modifier = Modifier.weight(1f))
            Button(onClick = viewModel::incrementBpm) { Text("+") }
        }
        Text("Energy: ${valueFormatter.format(energy)}")
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { pickerVisible = true },
        ) {
            Text(sensor?.name ?: "Select Position")
        }
    }

    if (pickerVisible) {
        SensorPositionDialog(
            current = sensor,
            onPick = { picked ->
                viewModel.selectPosition(picked)
                pickerVisible = false
            },
            onDismiss = { pickerVisible = false },
        )
    }
}

@Composable
private fun SensorPositionDialog(current: BluetoothSpec.SensorLocation?, onPick: (BluetoothSpec.SensorLocation?) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Position") },
        text = {
            Column {
                BluetoothSpec.SensorLocation.entries.forEach { location ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPick(location) },
                    ) { Text(location.name) }
                }
                if (current != null) {
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPick(null) },
                    ) { Text("Detach") }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
