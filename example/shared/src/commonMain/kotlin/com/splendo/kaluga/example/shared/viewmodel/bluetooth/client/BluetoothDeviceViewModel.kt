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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth.client

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.base.text.NumberFormatStyle
import com.splendo.kaluga.base.text.NumberFormatter
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.ByteOrder
import com.splendo.kaluga.base.utils.ByteUtils
import com.splendo.kaluga.base.utils.decodeUShort
import com.splendo.kaluga.base.utils.isBitSet
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.SerializableIdentifier
import com.splendo.kaluga.bluetooth.device.bind
import com.splendo.kaluga.bluetooth.device.serializable
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.distance
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.info
import com.splendo.kaluga.bluetooth.rssi
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.bluetooth.updateRssi
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.example.shared.stylable.TextStyles
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothSpec
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import com.splendo.kaluga.resources.view.KalugaLabel
import com.splendo.kaluga.scientific.formatter.CommonScientificValueFormatter
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.unit.BeatsPerMinute
import com.splendo.kaluga.scientific.unit.Kilojoule
import com.splendo.kaluga.scientific.unit.Units
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeviceDetails(value: Identifier) :
    SingleValueNavigationAction<SerializableIdentifier>(
        value.serializable,
        NavigationBundleSpecType.SerializedType(SerializableIdentifier.serializer()),
    )

class BluetoothDeviceViewModel(private val identifier: Identifier) :
    BaseLifecycleViewModel(),
    KoinComponent {

    companion object {
        private const val RSSI_FREQUENCY = 1000L

        val formatter = CommonScientificValueFormatter.with(builder = {
            defaultValueFormatter = NumberFormatter(style = NumberFormatStyle.Integer(minDigits = 1U)).apply {
                notANumberSymbol = "--"
            }
        })
    }

    class HeartRateViewModel(coroutineScope: CoroutineScope, device: Flow<ConnectableDevice?>) {

        private val requestReset = MutableSharedFlow<Unit>()
        private val requestPositionUpdate = MutableSharedFlow<Unit>()
        private val heartRateState = MutableStateFlow(Double.NaN(BeatsPerMinute))
        val heartRate = heartRateState.map { KalugaLabel.Plain(formatter.format(it), TextStyles.redText) }
            .toInitializedObservable(KalugaLabel.Plain(formatter.format(heartRateState.value), TextStyles.redText), coroutineScope)


        private val energyExpendedState = MutableStateFlow(Double.NaN(Kilojoule))
        val isEnergyExpendedVisible = energyExpendedState.map { value -> value.value.isFinite() }.toInitializedObservable(false, coroutineScope)

        private val isPositionVisibleState = MutableStateFlow(false)
        val isPositionVisible = isPositionVisibleState.toInitializedObservable(false, coroutineScope)
        private val positionState = MutableStateFlow<BluetoothSpec.SensorLocation?>(null)
        val position = positionState.map { position -> KalugaLabel.Plain(position?.name ?: "Detached", TextStyles.defaultText) }
            .toInitializedObservable(KalugaLabel.Plain("", TextStyles.defaultText), coroutineScope)

        val resetEnergyExpandedButton =
            KalugaButton.Plain("Reset Energy Expended", ButtonStyles.default) {
            requestReset.tryEmit(Unit)
        }

        val refreshPositionButton = KalugaButton.Plain("Refresh", ButtonStyles.default) {
            requestPositionUpdate.tryEmit(Unit)
        }

        init {
            bind(device, coroutineScope) {
                service(BluetoothSpec.HeartRateService.UUID) {
                    characteristic(BluetoothSpec.HeartRateService.HEART_RATE_MEASUREMENT_CHARACTERISTIC) {
                        observe { measurementValue ->
                            val valueEncodedAsShort = measurementValue.isBitSet(0)
                            heartRateState.value = if (valueEncodedAsShort) {
                                measurementValue.decodeUShort(2, ByteOrder.LEAST_SIGNIFICANT_FIRST).toInt()
                            } else {
                                measurementValue[2].toInt()
                            }(BeatsPerMinute)

                            isPositionVisibleState.value = measurementValue.isBitSet(2)
                            energyExpendedState.value = if (measurementValue.isBitSet(1)) {
                                measurementValue.decodeUShort(if (valueEncodedAsShort) 4 else 3, ByteOrder.LEAST_SIGNIFICANT_FIRST).toDouble()
                            } else {
                                Double.NaN
                            }(Kilojoule)
                        }
                    }
                    characteristic(BluetoothSpec.HeartRateService.SENSOR_LOCATION_CHARACTERISTIC) {
                        requestPositionUpdate.collectAsRead(onRead = { value ->
                            BluetoothSpec.SensorLocation.from(value[0])
                        })
                    }
                    characteristic(BluetoothSpec.HeartRateService.HEART_RATE_CONTROL_POINT_CHARACTERISTIC) {
                        requestReset.collectAsWrite(asByte = { byteArrayOf(0x01) })
                    }
                }
            }
        }
    }

    private val bluetooth: Bluetooth by inject()
    private val device = bluetooth.allDevices()[identifier]

    val name = device.info().map { it.name ?: "bluetooth_no_name".localized() }.toInitializedObservable("", coroutineScope)
    val identifierString = identifier.stringValue
    val rssi = device.rssi().map { "rssi".localized().format(it) }.toInitializedObservable("", coroutineScope)
    val distance = device.distance().map { "distance".localized().format(it) }.toInitializedObservable("", coroutineScope)
    val state = device.state().map { deviceState ->
        when (deviceState) {
            is NotConnectableDeviceState -> ""
            is ConnectableDeviceState.Disconnecting -> "bluetooth_disconneting"
            is ConnectableDeviceState.Disconnected -> "bluetooth_disconnected"
            is ConnectableDeviceState.Connected.Discovering -> "bluetooth_discovering"
            is ConnectableDeviceState.Connected -> "bluetooth_connected"
            is ConnectableDeviceState.Connecting -> "bluetooth_connecting"
        }.localized()
    }.toInitializedObservable("", coroutineScope)

    val heartRateViewModel = HeartRateViewModel(coroutineScope, device)

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        scope.launch {
            while (true) {
                device.updateRssi()
                delay(RSSI_FREQUENCY)
            }
        }
    }
}