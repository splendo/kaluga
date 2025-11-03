/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth.server

import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.server.GattResponse
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.bluetooth.server.ServerStatus
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothSpec
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.minus
import com.splendo.kaluga.scientific.plus
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Kilojoule
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.example.shared.stylable.TextStyles
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.Duration.Companion.seconds

class BluetoothServerViewModel : BaseLifecycleViewModel(), KoinComponent {

    companion object {
        fun generateHeartRateMeasurement(
            heartRate: Int,
            energyExpended: Int,
            sensorContactDetected: Boolean
        ): ByteArray {
            require(heartRate in 0..65535) { "Heart rate must be 0..65535" }

            val flags = buildFlags(heartRate, sensorContactDetected)
            val buffer = mutableListOf<Byte>()
            buffer.add(flags)
            if (heartRate <= 0xFF) {
                buffer.add(heartRate.toByte())
            } else {
                buffer.add((heartRate and 0xFF).toByte())
                buffer.add(((heartRate shr 8) and 0xFF).toByte())
            }

            buffer.add((energyExpended and 0xFF).toByte())
            buffer.add(((energyExpended shr 8) and 0xFF).toByte())

            return buffer.toByteArray()
        }

        private fun buildFlags(
            heartRate: Int,
            sensorContactDetected: Boolean
        ): Byte {
            var flags = 0

            if (heartRate > 0xFF) {
                flags = flags or 0x01
            }

            flags = flags or (0x02)
            if (sensorContactDetected) {
                flags = flags or 0x04
            }

            flags = flags or 0x08

            return flags.toByte()
        }
    }


    val bluetoothServer = coroutineScope.async {
        get<BluetoothBuilder>().createServer(
            settingsBuilder =  { permissions ->
                ServerSettings(permissions, autoRequestPermission = true, autoEnableBluetooth = true)
            }
        ) {
            advertise {
                localName = "Kaluga Bluetooth Server"
                serviceUUIDs(BluetoothSpec.HeartRateService.UUID, BluetoothSpec.KalugaSensorService.UUID, BluetoothSpec.KalugaSensorInfo.UUID)
            }
            service(BluetoothSpec.HeartRateService.UUID) {
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_MEASUREMENT_CHARACTERISTIC) {
                    combine(
                        heartRate,
                        energyExpended,
                        isConnected,
                    ) { heartRate, energyExpended, isConnected ->
                        generateHeartRateMeasurement(heartRate.value.toInt(), energyExpended.value.toInt(), isConnected)
                    }.sample(1.seconds).attachIn(this@async, SharingStarted.Lazily, 1)
                }
                characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                    readableAlwaysSuccess { _, _ ->
                        byteArrayOf(0x00)
                    }
                }
                characteristic(BluetoothSpec.HeartRateService.HEART_RATE_CONTROL_POINT_CHARACTERISTIC) {
                    writable { _, value, _ ->
                        if (value.contentEquals(byteArrayOf(0x01))) {
                            energyExpended.update { 0(Kilojoule) }
                            GattResponse.WriteSuccess
                        } else {
                            GattResponse.Custom(0x80)
                        }
                    }
                }
            }
        }
    }

    private val heartRate = MutableStateFlow(60(BeatsPerMinute))
    private val energyExpended = MutableStateFlow(0(Kilojoule))
    private val isConnected = MutableStateFlow(false)

    val status = flow {
        bluetoothServer.await().status.collect { status ->
            emit(KalugaLabel.Plain(status.toString(), TextStyles.defaultTitle))
        }
    }.toInitializedObservable(KalugaLabel.Plain(ServerStatus.NOT_SUPPORTED.name, TextStyles.defaultTitle), coroutineScope)

    val increaseBPM = KalugaButton.Plain("+", ButtonStyles.default) {
        heartRate.update {
            maxOf(
                400(BeatsPerMinute),
            it + 10(BeatsPerMinute),
            )
        }
    }

    val decreaseBPM = KalugaButton.Plain("-", ButtonStyles.default) {
        heartRate.update {
            maxOf(
                0(BeatsPerMinute),
                it - 10(BeatsPerMinute),
            )
        }
    }

    val heartRateLabel = heartRate.map { value ->
        KalugaLabel.Plain("${value.value.toInt()} BPM", TextStyles.defaultText)
    }.toInitializedObservable(KalugaLabel.Plain("", TextStyles.defaultText), coroutineScope)


    override fun onCleared() {
        bluetoothServer.getCompletedOrNull()?.close()
        super.onCleared()
    }


}