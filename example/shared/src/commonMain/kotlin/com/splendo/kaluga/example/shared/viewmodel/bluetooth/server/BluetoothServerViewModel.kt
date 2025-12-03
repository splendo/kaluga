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

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildActionSheet
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.utils.getCompletedOrNull
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.server.ServerSettings
import com.splendo.kaluga.bluetooth.server.ServerStatus
import com.splendo.kaluga.bluetooth.server.triggerNotification
import com.splendo.kaluga.bluetooth.server.writable
import com.splendo.kaluga.bluetooth.server.readableAlwaysSuccess
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
import com.splendo.kaluga.scientific.formatter.CommonScientificValueFormatter
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.Duration.Companion.seconds

class BluetoothServerViewModel(private val alertPresenter: BaseAlertPresenter.Builder, navigator: Navigator<CloseNavigationAction>) :
    NavigatingViewModel<BluetoothServerViewModel.CloseNavigationAction>(navigator, alertPresenter),
    KoinComponent {

    companion object {
        val formatter = CommonScientificValueFormatter.with(builder = {
            defaultValueFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U)).apply {
                notANumberSymbol = "--"
            }
        })
    }

    object CloseNavigationAction : SingleValueNavigationAction<Unit>(Unit, NavigationBundleSpecType.UnitType)

    val bluetoothServer = coroutineScope.async {
        get<BluetoothBuilder>().createServer(
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
                    combine(
                        heartRate,
                        energyExpended,
                        position,
                    ) { heartRate, energyExpended, position ->
                        BluetoothSpec.HeartRate(
                            heartRate.value.toInt(),
                            true,
                            position != null,
                            energyExpended.value.toInt(),
                            listOf(BluetoothSpec.RRInterval(1.seconds)),
                        )
                    }.sample(1.seconds).collectTo(coroutineScope, SharingStarted.Lazily, 1) {
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

    private val heartRate = MutableStateFlow(60(BeatsPerMinute))
    private val energyExpended = MutableStateFlow(0(Kilojoule))
    private val position = MutableStateFlow<BluetoothSpec.SensorLocation?>(null)

    val status = flow {
        val server = bluetoothServer.await()
        emitAll(
            server.status.map { status ->
                KalugaLabel.Plain(status.toString(), TextStyles.defaultTitle)
            },
        )
    }.toInitializedObservable(KalugaLabel.Plain(ServerStatus.NOT_SUPPORTED.name, TextStyles.defaultTitle), coroutineScope)

    val increaseBPM = KalugaButton.Plain("+", ButtonStyles.default) {
        heartRate.update {
            minOf(
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
        KalugaLabel.Plain(formatter.format(value), TextStyles.redText)
    }.toInitializedObservable(KalugaLabel.Plain("", TextStyles.redText), coroutineScope)

    val energyExpendedLabel = energyExpended.map { value ->
        KalugaLabel.Plain(formatter.format(value), TextStyles.defaultText)
    }.toInitializedObservable(KalugaLabel.Plain("", TextStyles.defaultText), coroutineScope)

    val positionPicker = position.map { position ->
        KalugaButton.Plain(position?.let { "$it" } ?: "Select Position", ButtonStyles.default) {
            coroutineScope.launch {
                val sensorActions = BluetoothSpec.SensorLocation.entries.associateBy { Alert.Action(it.name) }
                val detachAction = Alert.Action("Detach", Alert.Action.Style.DESTRUCTIVE).takeIf { position != null }
                val action = alertPresenter.buildActionSheet(this) {
                    setTitle("Select Position")
                    addActions(listOfNotNull(*sensorActions.keys.toTypedArray(), detachAction))
                }.show()
                this@BluetoothServerViewModel.position.update { sensorActions[action] }
            }
        }
    }.toInitializedObservable(KalugaButton.Plain("Select Position", ButtonStyles.default) {}, coroutineScope)

    init {
        coroutineScope.launch {
            while (true) {
                energyExpended.update { it + 5(Kilojoule) }
                delay(5.seconds)
            }
        }
    }

    fun close() {
        bluetoothServer.getCompletedOrNull()?.close()
        navigator.navigate(CloseNavigationAction)
    }
}
