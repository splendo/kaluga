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

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.BaseInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.base.text.format
import com.splendo.kaluga.base.utils.KalugaTimeZone.Companion.get
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.advertisement
import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.NotConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.rssi
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.example.shared.stylable.ButtonStyles
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothSpec
import com.splendo.kaluga.logging.defaultLogger
import com.splendo.kaluga.resources.localized
import com.splendo.kaluga.resources.view.KalugaButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.collections.emptyList
import kotlin.time.Duration.Companion.minutes

class BluetoothDeviceListViewModel(private val alertPresenterBuilder: AlertPresenter.Builder, navigator: Navigator<DeviceDetails>) :
    NavigatingViewModel<DeviceDetails>(navigator, alertPresenterBuilder),
    KoinComponent {

    class DeviceViewModel(
        private val deviceIdentifier: Identifier,
        private val bluetooth: Bluetooth,
        private val coroutineScope: CoroutineScope,
        private val navigator: Navigator<DeviceDetails>,
    ) {
        private val device = bluetooth.allDevices()[deviceIdentifier]

        val name = advertisementObservable("bluetooth_no_name".localized()) { it.name ?: "bluetooth_no_name".localized() }
        val identifierString = deviceIdentifier.stringValue
        val rssi = device.rssi().map { "rssi".localized().format(it) }.toInitializedObservable("", coroutineScope)
        val isTxPowerVisible = advertisementObservable(false) { it.txPowerLevel != Int.MIN_VALUE }
        val txPower = advertisementObservable("") { if (it.txPowerLevel != Int.MIN_VALUE) "txPower".localized().format(it.txPowerLevel) else "" }

        val isConnectButtonVisible = deviceStateObservable(false) { it !is NotConnectableDeviceState }
        val connectButton: BaseInitializedObservable<KalugaButton> = deviceStateObservable(KalugaButton.Plain("", ButtonStyles.default) {}) {
            when (it) {
                is ConnectableDeviceState.Disconnected, is ConnectableDeviceState.Disconnecting -> KalugaButton.Plain("Connect", ButtonStyles.default) { onConnectPressed() }
                else -> KalugaButton.Plain("Disconnect", ButtonStyles.default) { onDisconnectPressed() }
            }
        }

        private fun onConnectPressed() = coroutineScope.launch {
            try {
                withTimeout(5.minutes) {
                    device.connect()
                    navigator.navigate(DeviceDetails(deviceIdentifier))
                }
            } catch (_: TimeoutCancellationException) {}
        }

        private fun onDisconnectPressed() = coroutineScope.launch {
            device.disconnect()
        }

        private fun <T> deviceStateObservable(initialValue: T, mapper: (DeviceState) -> T): BaseInitializedObservable<T> = device.state()
            .map { mapper(it) }
            .toInitializedObservable(initialValue, coroutineScope)
        private fun <T> advertisementObservable(initialValue: T, mapper: (BaseAdvertisementData) -> T): BaseInitializedObservable<T> = device.advertisement().map {
            mapper(it)
        }.toInitializedObservable(initialValue, coroutineScope)
    }

    private val bluetooth: Bluetooth by inject()
    private val isResumed = MutableStateFlow(false)
    val isScanning = observeWhenResumed(false) {
        bluetooth.isScanning()
    }

    val title = bluetooth.isEnabled
        .mapLatest { if (it) "Enabled" else "Disabled" }
        .toInitializedObservable("Initializing...", coroutineScope)

    private val pairedDevicesJob = Job(coroutineScope.coroutineContext[Job])
    val pairedDevices = observeWhenResumed(emptyList()) {
        pairedDevicesJob.cancelChildren()
        bluetooth.pairedDevices(setOf(BluetoothSpec.HeartRateService.UUID)).map { devices ->
            devices.map { device ->
                DeviceViewModel(device.identifier, bluetooth, CoroutineScope(coroutineScope.coroutineContext + pairedDevicesJob), navigator)
            }
        }
    }

    private val scannedDevicesJob = Job(coroutineScope.coroutineContext[Job])
    val scannedDevices = observeWhenResumed(emptyList()) {
        scannedDevicesJob.cancelChildren()
        bluetooth.devices().map { devices ->
            devices.map { device ->
                DeviceViewModel(device.identifier, bluetooth, CoroutineScope(coroutineScope.coroutineContext + scannedDevicesJob), navigator)
            }
        }
    }

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        isResumed.value = true
    }

    fun onScanPressed() {
        coroutineScope.launch {
            val isScanning = bluetooth.isScanning().first()
            val builder = alertPresenterBuilder::buildAlert
            val action = builder.invoke(coroutineScope, defaultLogger) {
                if (isScanning) {
                    setTitle("Stop Scanning")
                } else {
                    setTitle("Start Scanning")
                }
                setMessage("Select Clean Mode")
                setPositiveButton("Retain All")
                setNeutralButton("Clean Only Provided Filter")
                setNegativeButton("Remove All")
            }.show()
            val cleanMode = when (action?.style) {
                null -> return@launch
                Alert.Action.Style.DEFAULT,
                Alert.Action.Style.POSITIVE,
                -> BluetoothService.CleanMode.RETAIN_ALL
                Alert.Action.Style.DESTRUCTIVE,
                Alert.Action.Style.NEUTRAL,
                -> BluetoothService.CleanMode.ONLY_PROVIDED_FILTER
                Alert.Action.Style.CANCEL,
                Alert.Action.Style.NEGATIVE,
                -> BluetoothService.CleanMode.REMOVE_ALL
            }

            if (isScanning) {
                bluetooth.stopScanning(cleanMode = cleanMode)
            } else {
                bluetooth.startScanning(
                    filter = setOf(BluetoothSpec.HeartRateService.UUID),
                    cleanMode = cleanMode,
                    connectionSettings = ConnectionSettings(logger = get()),
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()

        isResumed.value = false
    }
    private fun <T> observeWhenResumed(default: T, flow: suspend () -> Flow<T>) = isResumed.flatMapLatest {
        if (it) {
            flow()
        } else {
            flowOf(default)
        }
    }.toInitializedObservable(default, coroutineScope)
}
