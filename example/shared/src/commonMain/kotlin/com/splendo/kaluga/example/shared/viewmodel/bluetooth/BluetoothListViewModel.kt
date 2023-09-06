/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.bluetooth

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.alerts.buildAlertWithInput
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.UUIDException
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.uuidFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class BluetoothListViewModel(
    private val alertPresenterBuilder: AlertPresenter.Builder,
    navigator: Navigator<DeviceDetails>,
) : NavigatingViewModel<DeviceDetails>(navigator, alertPresenterBuilder), KoinComponent {

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
        bluetooth.pairedDevices(emptySet()).map { devices ->
            devices.map { device ->
                BluetoothListDeviceViewModel(device.identifier, bluetooth, CoroutineScope(coroutineScope.coroutineContext + pairedDevicesJob), navigator)
            }
        }
    }

    private val scannedDevicesJob = Job(coroutineScope.coroutineContext[Job])
    val scannedDevices = observeWhenResumed(emptyList()) {
        scannedDevicesJob.cancelChildren()
        bluetooth.devices().map { devices ->
            devices.map { device ->
                BluetoothListDeviceViewModel(device.identifier, bluetooth, CoroutineScope(coroutineScope.coroutineContext + scannedDevicesJob), navigator)
            }
        }
    }

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)

        isResumed.value = true
    }

    override fun onPause() {
        super.onPause()

        isResumed.value = false
    }

    fun onScanPressed() {
        coroutineScope.launch {
            val isScanning = bluetooth.isScanning().first()
            val builder = if (isScanning) alertPresenterBuilder::buildAlert else alertPresenterBuilder::buildAlertWithInput
            val filter = mutableSetOf<UUID>()
            val action = builder.invoke(coroutineScope) {
                if (isScanning) {
                    setTitle("Stop Scanning")
                } else {
                    setTitle("Start Scanning")
                    setTextInput("", "Filter for UUIDS") { text ->
                        filter.clear()
                        text.split(",").mapNotNull {
                            try {
                                filter.add(uuidFrom(it.trim()))
                            } catch (e: UUIDException.InvalidFormat) {
                                null
                            }
                        }
                    }
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
                    filter = filter,
                    cleanMode = cleanMode,
                    connectionSettings = ConnectionSettings(logger = get())
                )
            }
        }
    }

    private fun <T> observeWhenResumed(default: T, flow: suspend () -> Flow<T>) = isResumed.flatMapLatest {
        if (it) {
            flow()
        } else {
            flowOf(default)
        }
    }.toInitializedObservable(default, coroutineScope)
}
