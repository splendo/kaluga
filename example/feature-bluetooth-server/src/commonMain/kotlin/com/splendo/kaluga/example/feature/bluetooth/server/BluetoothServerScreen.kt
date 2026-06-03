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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.bluetooth.server.ServerStatus
import com.splendo.kaluga.bluetooth.server.readableAlwaysSuccess
import com.splendo.kaluga.bluetooth.server.triggerNotification
import com.splendo.kaluga.bluetooth.server.writable
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import com.splendo.kaluga.scientific.formatter.CommonScientificValueFormatter
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.minus
import com.splendo.kaluga.scientific.plus
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Kilojoule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds

private val valueFormatter = CommonScientificValueFormatter.with(builder = {
    defaultValueFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U)).apply {
        notANumberSymbol = "--"
    }
})

@Composable
fun BluetoothServerScreen(modifier: Modifier = Modifier) {
    val serverBuilder: BluetoothServerBuilder = koinInject()
    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    val heartRate = remember { MutableStateFlow(60(BeatsPerMinute)) }
    val energyExpended = remember { MutableStateFlow(0(Kilojoule)) }
    val position = remember { MutableStateFlow<BluetoothSpec.SensorLocation?>(null) }

    var server by remember { mutableStateOf<BluetoothServer?>(null) }
    LaunchedEffect(serverBuilder) {
        server = serverBuilder.createServer(
            settingsBuilder = { permissions ->
                ServerSettings(permissions, autoRequestPermission = true, autoEnableBluetooth = true)
            },
        ) {
            advertise {
                localName = "Kaluga"
                serviceUUIDs(BluetoothSpec.HeartRateService.UUID, BluetoothSpec.KalugaSensorService.UUID)
            }
            service(BluetoothSpec.HeartRateService.UUID) {
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_MEASUREMENT_CHARACTERISTIC) {
                    combine(heartRate, energyExpended, position) { bpm, energy, sensor ->
                        BluetoothSpec.HeartRate(
                            bpm.value.toInt(),
                            true,
                            sensor != null,
                            energy.value.toInt(),
                            listOf(BluetoothSpec.RRInterval(1.seconds)),
                        )
                    }.sample(1.seconds).collectTo(scope, SharingStarted.Lazily, 1) {
                        triggerNotification()
                    }
                }
                characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                    readableAlwaysSuccess { _ ->
                        position.value ?: BluetoothSpec.SensorLocation.OTHER
                    }
                }
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_CONTROL_POINT_CHARACTERISTIC) {
                    writable<BluetoothSpec.ResetEnergyCommand> { _, _ ->
                        energyExpended.update { 0(Kilojoule) }
                        GattResponse.WriteSuccess
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        // Drive the energy counter.
        while (true) {
            energyExpended.update { it + 5(Kilojoule) }
            delay(5.seconds)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            server?.close()
            scope.cancel()
        }
    }

    val status = server?.status?.collectAsState(initial = ServerStatus.NOT_SUPPORTED)?.value
        ?: ServerStatus.NOT_SUPPORTED
    val bpm by heartRate.collectAsState()
    val energy by energyExpended.collectAsState()
    val sensor by position.collectAsState()

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
            Button(onClick = {
                heartRate.update { maxOf(0(BeatsPerMinute), it - 10(BeatsPerMinute)) }
            }) { Text("-") }
            Text("BPM: ${valueFormatter.format(bpm)}", modifier = Modifier.weight(1f))
            Button(onClick = {
                heartRate.update { minOf(400(BeatsPerMinute), it + 10(BeatsPerMinute)) }
            }) { Text("+") }
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
                position.value = picked
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
