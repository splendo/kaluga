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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.logging.defaultLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * A [BluetoothClient] for the [TestPeripheral], with a log of what the test saw and when.
 * One session serves every test class of a run, so the client scans for the peripheral only once.
 */
class PeripheralSession(val platform: PeripheralScenarioPlatform) {

    companion object {
        val shared by lazy { PeripheralSession(peripheralScenarioPlatform) }
    }

    private val start = TimeSource.Monotonic.markNow()
    private val job = SupervisorJob()
    val scope = CoroutineScope(defaultBluetoothClientDispatcher + job)

    val client = platform.clientBuilder().createClient(coroutineContext = defaultBluetoothClientDispatcher + job)
    val connectionSettings = ConnectionSettings(
        reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never,
        logger = defaultLogger,
        dataLogger = defaultLogger,
    )

    lateinit var device: ConnectableDevice
        private set

    class Attributes(val services: List<RemoteService>) {
        val service = services.first { it.uuid == TestPeripheral.service }
        val data = service.characteristic(TestPeripheral.data)
        val control = service.characteristic(TestPeripheral.control)
        val error get() = service.characteristic(TestPeripheral.error)
        val indicate get() = service.characteristic(TestPeripheral.indicate)
        val long get() = service.characteristic(TestPeripheral.long)
        val sink get() = service.characteristic(TestPeripheral.sink)
        val twins get() = services.first { it.uuid == TestPeripheral.twinService }.characteristics

        private fun RemoteService.characteristic(uuid: UUID) = characteristics.first { it.uuid == uuid }
    }

    fun log(message: String) {
        platform.log("${start.elapsedNow().inWholeMilliseconds} ms: $message")
    }

    suspend fun <T> within(duration: Duration, what: String, block: suspend () -> T): T {
        val result = withTimeoutOrNull(duration) { block() } ?: fail("$what did not finish within $duration")
        log("$what -> $result")
        return result
    }

    suspend fun <T> Deferred<T>.answer(what: String, duration: Duration = 10.seconds): T = within(duration, what) { await() }

    suspend fun scanOnce() {
        if (::device.isInitialized) return
        scope.launch { client.devices().collect { } }
        platform.awaitScanStart()
        client.startScanning(setOf(TestPeripheral.service), connectionSettings = connectionSettings)
        device = within(30.seconds, "scan") { client.devices().first { it.isNotEmpty() }.first() }
        client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)
    }

    /** Connects and discovers, and resets the peripheral, which not every player can do on a link of its own. */
    suspend fun connectAndDiscover(): Attributes {
        assertTrue(within(30.seconds, "connect") { device.connect() })
        return Attributes(within(30.seconds, "discover") { device.discoveredServices().first() }).also { it.send(TestPeripheral.Command.Reset) }
    }

    suspend fun Attributes.send(command: TestPeripheral.Command) {
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "command $command") { control.write(command.bytes) })
    }

    suspend fun awaitInFlight(action: DeviceAction<*>, queued: Int = 0) = within(10.seconds, "in flight with $queued queued") {
        device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first { it.action === action && it.nextActions.size == queued }
    }

    suspend fun awaitDisconnected(timeout: Duration = 20.seconds) = within(timeout, "disconnected") { device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first() }

    /**
     * Disconnects and waits for the stack to close the link, which it may keep for a moment after the last client
     * disconnected. A connection made before that reuses the link, along with any request still held on it, and service
     * discovery then waits for the ATT timeout of that request.
     */
    suspend fun disconnect() {
        if (!::device.isInitialized) return
        withTimeoutOrNull(20.seconds) { device.disconnect() } ?: log("disconnect after the test timed out")
        within(20.seconds, "link closed") { platform.awaitLinkClosed(device) }
    }
}

/**
 * Runs the client against the [TestPeripheral]. Skipped unless the platform says the peripheral is running, since it
 * has to be started next to the test run, and when its player lacks a capability the scenario [requires].
 */
abstract class PeripheralTest {

    protected fun peripheralTest(timeout: Duration = 120.seconds, requires: Set<TestPeripheral.Capability> = emptySet(), block: suspend PeripheralSession.() -> Unit) {
        val platform = peripheralScenarioPlatform
        val player = platform.player
        if (player == null) {
            platform.skip("the test peripheral is not running")
            return
        }
        (requires - player.capabilities).firstOrNull()?.let {
            platform.skip("the test peripheral played by $player cannot ${it.description}")
            return
        }
        platform.setUp()
        runBlocking {
            val session = PeripheralSession.shared
            try {
                session.within(timeout, "test") {
                    session.scanOnce()
                    session.block()
                }
            } finally {
                session.disconnect()
            }
        }
    }
}
