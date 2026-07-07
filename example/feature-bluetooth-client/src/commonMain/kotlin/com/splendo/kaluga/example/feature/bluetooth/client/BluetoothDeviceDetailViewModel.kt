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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.bind
import com.splendo.kaluga.bluetooth.device.observe
import com.splendo.kaluga.bluetooth.device.triggerRead
import com.splendo.kaluga.bluetooth.device.triggerWrite
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.distance
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.info
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.bluetooth.updateRssi
import com.splendo.kaluga.example.feature.bluetooth.base.BluetoothSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class BluetoothDeviceDetailViewModel(client: BluetoothClient, val identifier: Identifier) : ViewModel() {

    private val deviceFlow = client.allDevices()[identifier]

    val info: StateFlow<DeviceInfo?> = deviceFlow.info().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val deviceState: StateFlow<DeviceState?> = deviceFlow.state().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val distance: StateFlow<Double> = deviceFlow.distance().stateIn(viewModelScope, SharingStarted.Eagerly, Double.NaN)

    private val _heartRate = MutableStateFlow<BluetoothSpec.HeartRate?>(null)
    val heartRate: StateFlow<BluetoothSpec.HeartRate?> = _heartRate.asStateFlow()
    private val _position = MutableStateFlow<BluetoothSpec.SensorLocation?>(null)
    val position: StateFlow<BluetoothSpec.SensorLocation?> = _position.asStateFlow()

    private val requestPositionUpdate = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val requestReset = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            while (true) {
                deviceFlow.updateRssi()
                delay(1.seconds)
            }
        }
        viewModelScope.launch {
            Unit.bind(deviceFlow, viewModelScope) {
                service(BluetoothSpec.HeartRateService.UUID) {
                    characteristic(BluetoothSpec.HeartRateService.HEART_RATE_MEASUREMENT_CHARACTERISTIC) {
                        observe<BluetoothSpec.HeartRate, Unit> {
                            onNotification { hr -> _heartRate.value = hr }
                        }
                    }
                    characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                        requestPositionUpdate.collectTo {
                            triggerRead<BluetoothSpec.SensorLocation, Unit> {
                                onRead { loc -> _position.value = loc }
                                onFailedToRead { _ -> _position.value = null }
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
    }

    fun refreshPosition() = viewModelScope.launch { requestPositionUpdate.emit(Unit) }

    fun resetEnergy() = viewModelScope.launch { requestReset.emit(Unit) }

    fun disconnect(onDisconnected: () -> Unit) = viewModelScope.launch {
        deviceFlow.disconnect()
        onDisconnected()
    }
}
