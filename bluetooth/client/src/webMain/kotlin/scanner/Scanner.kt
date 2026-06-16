/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.bluetooth.BluetoothMonitor
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.AdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.WebDeviceWrapper
import com.splendo.kaluga.bluetooth.device.webBluetoothSupported
import com.splendo.kaluga.bluetooth.device.webHideDevicePicker
import com.splendo.kaluga.bluetooth.device.webShowDevicePicker
import com.splendo.kaluga.bluetooth.uuidString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Configures the on-screen "Add Device" overlay rendered by the Web Bluetooth [DefaultScanner].
 *
 * Every element gets a class derived from [cssClassPrefix] (e.g. `<prefix>-overlay`, `<prefix>-button`,
 * `<prefix>-list`, `<prefix>-list-item`), so the host page can fully restyle the overlay through CSS.
 * @property title the heading shown at the top of the overlay
 * @property addButtonLabel the label of the button that opens the system device picker
 * @property emptyLabel the text shown in the device list while no device has been added yet
 * @property cssClassPrefix the prefix applied to every overlay element's CSS class
 * @property containerId the id of the element to mount the overlay in, or `null` for `document.body`
 */
data class WebDevicePickerSettings(
    val title: String = "Bluetooth Devices",
    val addButtonLabel: String = "Add Device",
    val emptyLabel: String = "No devices added yet",
    val cssClassPrefix: String = "kaluga-bluetooth",
    val containerId: String? = null,
)

/**
 * A default implementation of [BaseScanner] backed by the Web Bluetooth API.
 *
 * Web Bluetooth has no free-running scan, and `navigator.bluetooth.requestDevice` requires transient
 * user activation. This scanner therefore renders an "Add Device" overlay for the duration of a scan
 * ([didStartScanning] shows it, [didStopScanning] removes it); each press of its button opens the
 * system picker and adds the chosen device, so a single scan can collect any number of devices.
 * @param settings the [BaseScanner.Settings] to configure this scanner
 * @param optionalServices the advertisement-independent access allowlist applied to every picker invocation
 * @param pickerSettings the [WebDevicePickerSettings] configuring the "Add Device" overlay
 * @param coroutineScope the [CoroutineScope] this scanner runs on
 * @param scanningDispatcher the [CoroutineDispatcher] to which scanning should be dispatched
 */
actual class DefaultScanner internal constructor(
    settings: Settings,
    private val optionalServices: List<UUID>,
    private val pickerSettings: WebDevicePickerSettings,
    coroutineScope: CoroutineScope,
    scanningDispatcher: CoroutineDispatcher = com.splendo.kaluga.bluetooth.scanner.scanningDispatcher,
) : BaseScanner(settings, coroutineScope, scanningDispatcher) {

    /**
     * Builder for creating a [DefaultScanner]
     * @param optionalServices the advertisement-independent access allowlist applied to every picker invocation.
     * Services that are not advertised (or only available after connecting) must be listed here to be reachable.
     * @param pickerSettings the [WebDevicePickerSettings] configuring the "Add Device" overlay
     */
    class Builder(private val optionalServices: List<UUID> = emptyList(), private val pickerSettings: WebDevicePickerSettings = WebDevicePickerSettings()) : BaseScanner.Builder {

        override fun create(settings: Settings, coroutineScope: CoroutineScope, scanningDispatcher: CoroutineDispatcher): BaseScanner =
            DefaultScanner(settings, optionalServices, pickerSettings, coroutineScope, scanningDispatcher)
    }

    actual override val isSupported: Boolean = webBluetoothSupported()
    actual override val bluetoothEnabledMonitor: BluetoothMonitor? = BluetoothMonitor.Builder().create()

    actual override suspend fun didStartScanning(filter: Filter) {
        webShowDevicePicker(
            filter.map { it.uuidString },
            optionalServices.map { it.uuidString },
            pickerSettings.title,
            pickerSettings.addButtonLabel,
            pickerSettings.emptyLabel,
            pickerSettings.cssClassPrefix,
            pickerSettings.containerId,
        ) { identifier, name ->
            val deviceName = name.ifEmpty { null }
            handleDeviceDiscovered(
                WebDeviceWrapper(identifier, deviceName),
                null,
                AdvertisementData(deviceName, filter.toList()),
                DefaultDeviceConnectionManager.Builder(),
            )
        }
    }

    actual override suspend fun didStopScanning() {
        webHideDevicePicker()
    }

    actual override fun generateEnableSensorsActions(): List<EnableSensorAction> = emptyList()

    actual override suspend fun retrievePairedDeviceDiscoveredEvents(withServices: Filter, connectionSettings: ConnectionSettings?): List<Scanner.DeviceDiscovered> = emptyList()
}
