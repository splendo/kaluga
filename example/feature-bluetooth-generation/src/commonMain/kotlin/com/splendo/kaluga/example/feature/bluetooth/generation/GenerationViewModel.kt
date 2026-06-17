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

package com.splendo.kaluga.example.feature.bluetooth.generation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.randomIdentifier
import com.splendo.kaluga.bluetooth.device.stringValue
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/** A connected client surfaced in the Client tab — either the in-process simulated loopback or a real device. */
data class SelectedClient(val key: String, val client: DemoDeviceClient)

/**
 * Owns the feature session: the device acts as a server (a real Bluetooth server + an in-process simulated
 * one, both driven by the same [DemoDeviceServer.Delegate]) and as a client (scanning for real devices, plus
 * the simulated loopback). Shared by both tabs of [GenerationScreen].
 */
class GenerationViewModel(
    private val client: BluetoothClient,
    private val serverBuilder: BluetoothServerBuilder,
    private val delegate: DemoDeviceServer.Delegate,
    private val state: DemoServerState,
) : ViewModel() {

    // ----- Server: real + simulated, sharing one delegate -----
    // The simulated server is in-process and always available; the real server only advertises/serves while the
    // Server tab is active (startServer/stopServer), so the device isn't a peripheral while the Client tab connects.
    private val simulatedServer = DemoDeviceServer.simulated(delegate)
    private val realServer = MutableStateFlow<DemoDeviceServer?>(null)
    private var serverJob: Job? = null

    val reading = state.reading
    val name = state.name
    val lastThresholdWritten = state.lastThresholdWritten

    // Subscriber counts are the total across the real and simulated servers.
    val liveSubscriberCount = combine(
        simulatedServer.demoService.sensor.liveSubscribers,
        realServer.flatMapLatest { it?.demoService?.sensor?.liveSubscribers ?: flowOf(emptyList<Identifier>()) },
    ) { simulated, real -> simulated.size + real.size }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), 0)

    val statusSubscriberCount = combine(
        simulatedServer.demoService.config.statusSubscribers,
        realServer.flatMapLatest { it?.demoService?.config?.statusSubscribers ?: flowOf(emptyList<Identifier>()) },
    ) { simulated, real -> simulated.size + real.size }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), 0)

    fun setReading(value: Int) {
        state.reading.value = value
    }

    fun setName(value: String) {
        state.name.value = value
    }

    fun pushLive(value: Short) = viewModelScope.launch {
        simulatedServer.demoService.sensor.notifyAllLiveChanged(value)
        realServer.value?.demoService?.sensor?.notifyAllLiveChanged(value)
    }

    fun pushStatus(value: Short) = viewModelScope.launch {
        simulatedServer.demoService.config.notifyAllStatusChanged(value)
        realServer.value?.demoService?.config?.notifyAllStatusChanged(value)
    }

    // Built once on first Server-tab visit and kept alive for the feature's lifetime (closed in onCleared).
    fun startServer() {
        if (serverJob != null) {
            return
        }
        serverJob = viewModelScope.launch { realServer.value = DemoDeviceServer.bluetooth(serverBuilder, delegate) }
    }

    override fun onCleared() {
        super.onCleared()
        serverJob?.cancel()
        realServer.value?.close()
    }

    // ----- Client: the simulated loopback + scanned real devices -----
    val scannedDevices = client.devices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val simulatedClient: DemoDeviceClient = DemoDeviceClient.simulated(randomIdentifier(), simulatedServer)

    private val _selected = MutableStateFlow<SelectedClient?>(null)
    val selected = _selected.asStateFlow()

    private val _connecting = MutableStateFlow(false)
    val connecting = _connecting.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var connectedDevice: ConnectableDevice? = null

    // Bumped per connection so each selection gets a unique key, forcing a fresh ClientViewModel (a reconnect must
    // not inherit the previous session's client state).
    private var clientSessionId = 0

    fun startScanning() = client.startScanning(
        // Retain devices across scan restarts so an already-connected device is reused (its connect() short-circuits).
        filter = setOf(RemoteDemoService.UUID),
        cleanMode = BluetoothClient.CleanMode.RETAIN_ALL,
        // Never auto-reconnect: an explicit disconnect (on deselect) must stick.
        connectionSettings = ConnectionSettings(reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never),
    )

    fun stopScanning() = client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)

    fun selectSimulated() {
        _selected.value = SelectedClient("simulated#${clientSessionId++}", simulatedClient)
    }

    // Connect and disconnect operate on the same (reused) device, so serialize them: a new operation waits for the
    // previous one to finish, otherwise a quick back-then-reconnect races a lingering disconnect against the connect.
    private var connectionJob: Job? = null

    fun connect(device: ConnectableDevice) {
        _error.value = null
        _connecting.value = true
        val previous = connectionJob
        connectionJob = viewModelScope.launch {
            try {
                // Bounds the wait on any pending disconnect plus the connect and the service discovery that
                // DemoDeviceClient.bluetooth(...) awaits, so a stalled prior operation surfaces as an error.
                val connectedClient = withTimeoutOrNull(1.minutes) {
                    previous?.join()
                    if (device.connect(reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never)) {
                        DemoDeviceClient.bluetooth(client, device.identifier)
                    } else {
                        null
                    }
                }
                if (connectedClient != null) {
                    connectedDevice = device
                    _selected.value = SelectedClient("${device.identifier.stringValue}#${clientSessionId++}", connectedClient)
                } else {
                    _error.value = "Could not connect to ${device.identifier.stringValue}"
                }
            } finally {
                _connecting.value = false
            }
        }
    }

    fun deselect() {
        val device = connectedDevice
        connectedDevice = null
        _selected.value = null
        val previous = connectionJob
        connectionJob = viewModelScope.launch {
            previous?.join()
            device?.disconnect()
        }
    }
}
