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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.bind
import com.splendo.kaluga.bluetooth.device.observe
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.device.triggerRead
import com.splendo.kaluga.bluetooth.device.triggerWrite
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.distance
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.info
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.bluetooth.updateRssi
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds

@Composable
fun BluetoothDeviceDetailScreen(identifier: Identifier, onBack: () -> Unit) {
    val bluetoothClient: BluetoothClient = koinInject()
    val scope = rememberCoroutineScope()

    val deviceFlow = remember(identifier) { bluetoothClient.allDevices()[identifier] }

    val info: DeviceInfo? by remember(deviceFlow) { deviceFlow.info() }.collectAsState(initial = null)
    val deviceState: DeviceState? by deviceFlow.state().collectAsState(initial = null)
    val distance by remember(deviceFlow) { deviceFlow.distance() }.collectAsState(initial = Double.NaN)

    // Heart rate state
    val heartRate = remember { MutableStateFlow<BluetoothSpec.HeartRate?>(null) }
    val position = remember { MutableStateFlow<BluetoothSpec.SensorLocation?>(null) }
    val requestPositionUpdate = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }
    val requestReset = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    val currentHeartRate by heartRate.collectAsState()
    val currentPosition by position.collectAsState()

    LaunchedEffect(deviceFlow) {
        while (true) {
            deviceFlow.updateRssi()
            delay(1.seconds)
        }
    }

    LaunchedEffect(Unit) {
        Unit.bind(deviceFlow, scope) {
            service(BluetoothSpec.HeartRateService.UUID) {
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_MEASUREMENT_CHARACTERISTIC) {
                    observe<BluetoothSpec.HeartRate, Unit> {
                        onNotification { hr -> heartRate.value = hr }
                    }
                }
                characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                    requestPositionUpdate.collectTo {
                        triggerRead<BluetoothSpec.SensorLocation, Unit> {
                            onRead { loc -> position.value = loc }
                            onFailedToRead { _ -> position.value = null }
                        }
                    }
                }
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_CONTROL_POINT_CHARACTERISTIC) {
                    requestReset.collectTo {
                        triggerWrite(mapper = { BluetoothSpec.ResetEnergyCommand }) { }
                    }
                }
            }
        }
    }

    val title = "${info?.name ?: identifier.stringValue} — ${identifier.stringValue}"

    DetailScaffold(
        title = title,
        onBack = {
            scope.launch {
                deviceFlow.disconnect()
                onBack()
            }
        },
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
                        OutlinedButton(onClick = { scope.launch { requestReset.emit(Unit) } }) {
                            Text("Reset")
                        }
                    }
                }
            }

            HorizontalDivider()

            Text("Sensor Location", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(currentPosition?.name ?: "Unknown")
                Button(onClick = { scope.launch { requestPositionUpdate.emit(Unit) } }) {
                    Text("Refresh")
                }
            }
        }
    }
}
