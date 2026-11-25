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

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.device.ConnectableDevice
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.logging.defaultLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * The GATT peripheral run by `src/androidDeviceTest/python/netsim_peripheral.py` on the emulator's netsim radio.
 * The UUIDs and the control commands mirror that script.
 */
object NetsimPeripheral {
    val service: UUID = java.util.UUID.fromString("6b7a0000-5d1c-4f0e-9a51-2f6f3c1d8e01")
    val data: UUID = java.util.UUID.fromString("6b7a0001-5d1c-4f0e-9a51-2f6f3c1d8e01")
    val control: UUID = java.util.UUID.fromString("6b7a0002-5d1c-4f0e-9a51-2f6f3c1d8e01")
    val initialValue = byteArrayOf(1, 2, 3)

    /** The instrumentation argument that tells the tests the peripheral is running. */
    const val ARGUMENT = "netsimPeripheral"

    sealed class Command(private val code: Int, private val millis: Int = 0, private val payload: ByteArray = byteArrayOf()) {
        data object Reset : Command(0)
        class HoldRead(releaseAfter: Duration = Duration.ZERO) : Command(1, releaseAfter.inWholeMilliseconds.toInt())
        class HoldCccdWrite(releaseAfter: Duration = Duration.ZERO) : Command(2, releaseAfter.inWholeMilliseconds.toInt())
        class DropWhenHeld(after: Duration) : Command(3, after.inWholeMilliseconds.toInt())
        class Drop(after: Duration) : Command(4, after.inWholeMilliseconds.toInt())
        class Notify(value: ByteArray, after: Duration = Duration.ZERO) : Command(5, after.inWholeMilliseconds.toInt(), value)

        val bytes: ByteArray get() = byteArrayOf(code.toByte(), (millis and 0xFF).toByte(), (millis shr 8 and 0xFF).toByte()) + payload

        override fun toString() = "${this::class.simpleName}(${bytes.toHexString()})"
    }
}

private const val TAG = "NetsimTest"

/**
 * A [BluetoothClient] for the [NetsimPeripheral], with a log of what the test saw and when.
 * One session serves every test of a run: Android refuses an app more than five scan starts in 30 seconds.
 */
class NetsimSession(context: Context) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)

    private val start = TimeSource.Monotonic.markNow()
    private val job = SupervisorJob()
    val scope = CoroutineScope(defaultBluetoothClientDispatcher + job)

    init {
        if (!ApplicationHolder.isInitialized) ApplicationHolder.applicationContext = context
    }

    private val client = BluetoothClientBuilder(context).createClient(coroutineContext = defaultBluetoothClientDispatcher + job)
    private val connectionSettings = ConnectionSettings(
        reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never,
        logger = defaultLogger,
        dataLogger = defaultLogger,
    )

    lateinit var device: ConnectableDevice
        private set

    class Attributes(val service: RemoteService, val data: RemoteCharacteristic, val control: RemoteCharacteristic)

    fun log(message: String) {
        Log.i(TAG, "${start.elapsedNow().inWholeMilliseconds} ms: $message")
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
        client.startScanning(setOf(NetsimPeripheral.service), connectionSettings = connectionSettings)
        device = within(30.seconds, "scan") { client.devices().first { it.isNotEmpty() }.first() }
        client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)
    }

    suspend fun connectAndDiscover(): Attributes {
        assertTrue(within(30.seconds, "connect") { device.connect() })
        val services = within(30.seconds, "discover") { device.discoveredServices().first() }
        val service = services.first { it.uuid == NetsimPeripheral.service }
        return Attributes(
            service,
            service.characteristics.first { it.uuid == NetsimPeripheral.data },
            service.characteristics.first { it.uuid == NetsimPeripheral.control },
        )
    }

    suspend fun Attributes.send(command: NetsimPeripheral.Command) {
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "command $command") { control.write(command.bytes) })
    }

    suspend fun awaitInFlight(action: DeviceAction<*>, queued: Int = 0) = within(10.seconds, "in flight with $queued queued") {
        device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first { it.action === action && it.nextActions.size == queued }
    }

    suspend fun awaitDisconnected() = within(20.seconds, "disconnected") { device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first() }

    /**
     * Disconnects and waits for the stack to close the link, which it keeps for a moment after the last client
     * disconnected. A connection made before that reuses the link, along with any request still held on it, and
     * service discovery then waits for the 30 second ATT timeout of that request.
     */
    @SuppressLint("MissingPermission")
    suspend fun disconnect() {
        if (!::device.isInitialized) return
        withTimeoutOrNull(20.seconds) { device.disconnect() } ?: log("disconnect after the test timed out")
        within(20.seconds, "link closed") {
            while (bluetoothManager.getConnectedDevices(BluetoothProfile.GATT).any { it.address == device.identifier }) delay(100)
        }
    }

    fun close() {
        job.cancel()
    }
}
