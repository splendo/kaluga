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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.bluetooth.server.ServerStatus
import com.splendo.kaluga.bluetooth.server.readableAlwaysSuccess
import com.splendo.kaluga.bluetooth.server.triggerNotification
import com.splendo.kaluga.bluetooth.server.writable
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.minus
import com.splendo.kaluga.scientific.plus
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Kilojoule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class BluetoothServerViewModel(serverBuilder: BluetoothServerBuilder) : ViewModel() {

    private val heartRate = MutableStateFlow(60(BeatsPerMinute))
    private val energyExpended = MutableStateFlow(0(Kilojoule))
    private val _position = MutableStateFlow<BluetoothSpec.SensorLocation?>(null)

    val bpm = heartRate.asStateFlow()
    val energy = energyExpended.asStateFlow()
    val position: StateFlow<BluetoothSpec.SensorLocation?> = _position.asStateFlow()

    private val _status = MutableStateFlow(ServerStatus.NOT_SUPPORTED)
    val status: StateFlow<ServerStatus> = _status.asStateFlow()

    private var server: BluetoothServer? = null

    init {
        viewModelScope.launch {
            val created = serverBuilder.createServer(
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
                        combine(heartRate, energyExpended, _position) { bpm, energy, sensor ->
                            BluetoothSpec.HeartRate(
                                bpm.value.toInt(),
                                true,
                                sensor != null,
                                energy.value.toInt(),
                                listOf(BluetoothSpec.RRInterval(1.seconds)),
                            )
                        }.sample(1.seconds).collectTo(viewModelScope, SharingStarted.Lazily, 1) {
                            triggerNotification()
                        }
                    }
                    characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                        readableAlwaysSuccess { _ ->
                            _position.value ?: BluetoothSpec.SensorLocation.OTHER
                        }
                    }
                    characteristic(BluetoothSpec.HeartRateService.HEART_RATE_CONTROL_POINT_CHARACTERISTIC) {
                        writable<BluetoothSpec.ResetEnergyCommand> { _, _ ->
                            energyExpended.update { 0(Kilojoule) }
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                }
            }
            server = created
            created.status.collect { _status.value = it }
        }
        viewModelScope.launch {
            // Drive the energy counter.
            while (true) {
                energyExpended.update { it + 5(Kilojoule) }
                delay(5.seconds)
            }
        }
    }

    fun incrementBpm() = heartRate.update { minOf(400(BeatsPerMinute), it + 10(BeatsPerMinute)) }

    fun decrementBpm() = heartRate.update { maxOf(0(BeatsPerMinute), it - 10(BeatsPerMinute)) }

    fun selectPosition(position: BluetoothSpec.SensorLocation?) {
        _position.value = position
    }

    override fun onCleared() {
        server?.close()
        super.onCleared()
    }
}
