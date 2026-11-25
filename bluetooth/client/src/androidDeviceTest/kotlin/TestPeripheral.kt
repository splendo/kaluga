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

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assume.assumeTrue
import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * The GATT peripheral run by `src/androidDeviceTest/python/peripheral.py` on the emulator's netsim radio.
 * The UUIDs and the control commands mirror that script.
 */
object TestPeripheral {
    val service: UUID = uuid("0000")
    val data: UUID = uuid("0001")
    val control: UUID = uuid("0002")
    val error: UUID = uuid("0003")
    val indicate: UUID = uuid("0004")
    val long: UUID = uuid("0005")
    val sink: UUID = uuid("0006")
    val twinService: UUID = uuid("0100")
    val twin: UUID = uuid("0101")
    val extraService: UUID = uuid("0200")
    val extra: UUID = uuid("0201")
    val beaconService: UUID = uuid("0300")

    val initialValue = byteArrayOf(1, 2, 3)
    val twinValues = listOf("A".encodeToByteArray(), "B".encodeToByteArray())
    val extraValue = "extra".encodeToByteArray()

    const val NAME = "kaluga-netsim"
    const val BEACON_NAME = "kaluga-beacon"
    val beaconServiceDataUuid: UUID = java.util.UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb")
    val beaconServiceData = byteArrayOf(0x5A, 0x01, 0x02, 0x03)
    const val BEACON_COMPANY = 0xFFFF
    val beaconManufacturerData = byteArrayOf(0xCA.toByte(), 0xFE.toByte(), 0x00, 0x01)

    /** The instrumentation argument that tells the tests the peripheral is running. */
    const val ARGUMENT = "netsimPeripheral"

    private fun uuid(short: String): UUID = java.util.UUID.fromString("6b7a$short-5d1c-4f0e-9a51-2f6f3c1d8e01")

    /** [length] bytes counting up from 0, as the peripheral fills its long values. */
    fun counting(length: Int) = ByteArray(length) { it.toByte() }

    sealed class Command(private val code: Int, private val argument: Int = 0, private val payload: ByteArray = byteArrayOf()) {
        data object Reset : Command(0)
        class HoldRead(releaseAfter: Duration = Duration.ZERO) : Command(1, releaseAfter.millis)
        class HoldCccdWrite(releaseAfter: Duration = Duration.ZERO) : Command(2, releaseAfter.millis)
        class DropWhenHeld(after: Duration) : Command(3, after.millis)
        class Drop(after: Duration) : Command(4, after.millis)
        class Notify(value: ByteArray, after: Duration = Duration.ZERO) : Command(5, after.millis, value)
        class AnswerWithError(code: Int) : Command(6, code)
        class Indicate(value: ByteArray, after: Duration = Duration.ZERO) : Command(7, after.millis, value)
        class NotifyBurst(count: Int) : Command(8, count)
        class SetLongValue(length: Int) : Command(9, length)
        class NotifyTwin(index: Int, value: ByteArray, after: Duration = Duration.ZERO) : Command(10, after.millis, byteArrayOf(index.toByte()) + value)
        class ChangeServices(after: Duration = Duration.ZERO) : Command(11, after.millis)
        data object StartBeacon : Command(12)
        data object StopBeacon : Command(13)
        class NotifyLength(length: Int) : Command(14, length)
        class SetLongValueTo(value: ByteArray) : Command(15, payload = value)

        val bytes: ByteArray get() = byteArrayOf(code.toByte(), (argument and 0xFF).toByte(), (argument shr 8 and 0xFF).toByte()) + payload

        override fun toString() = "${this::class.simpleName}(${bytes.toHexString()})"
    }

    private val Duration.millis get() = inWholeMilliseconds.toInt()
}

private const val TAG = "PeripheralTest"

/**
 * Android allows an app five scan starts in 30 seconds and silently returns no results after that. Every test that
 * starts a scan takes a start from here first, so a whole run stays under that limit.
 */
object ScanBudget {
    private const val MAX_STARTS = 4
    private val window = 32.seconds
    private val mutex = Mutex()
    private val starts = ArrayDeque<TimeMark>()

    suspend fun take() = mutex.withLock {
        while (true) {
            while (starts.isNotEmpty() && starts.first().elapsedNow() > window) starts.removeFirst()
            if (starts.size < MAX_STARTS) break
            delay(window - starts.first().elapsedNow() + 1.seconds)
        }
        starts.addLast(TimeSource.Monotonic.markNow())
    }
}

/**
 * A [BluetoothClient] for the [TestPeripheral], with a log of what the test saw and when.
 * One session serves every test class of a run: a scan per class would soon hit the [ScanBudget].
 */
class PeripheralSession(context: Context) {

    companion object {
        val shared by lazy { PeripheralSession(InstrumentationRegistry.getInstrumentation().targetContext) }
    }

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)

    private val start = TimeSource.Monotonic.markNow()
    private val job = SupervisorJob()
    val scope = CoroutineScope(defaultBluetoothClientDispatcher + job)

    init {
        if (!ApplicationHolder.isInitialized) ApplicationHolder.applicationContext = context
    }

    val client = BluetoothClientBuilder(context).createClient(coroutineContext = defaultBluetoothClientDispatcher + job)
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
        ScanBudget.take()
        client.startScanning(setOf(TestPeripheral.service), connectionSettings = connectionSettings)
        device = within(30.seconds, "scan") { client.devices().first { it.isNotEmpty() }.first() }
        client.stopScanning(BluetoothClient.CleanMode.RETAIN_ALL)
    }

    suspend fun connectAndDiscover(): Attributes {
        assertTrue(within(30.seconds, "connect") { device.connect() })
        return Attributes(within(30.seconds, "discover") { device.discoveredServices().first() })
    }

    suspend fun Attributes.send(command: TestPeripheral.Command) {
        assertEquals<GattResponse>(GattResponse.WriteSuccess.Acknowledged, within(10.seconds, "command $command") { control.write(command.bytes) })
    }

    suspend fun awaitInFlight(action: DeviceAction<*>, queued: Int = 0) = within(10.seconds, "in flight with $queued queued") {
        device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first { it.action === action && it.nextActions.size == queued }
    }

    suspend fun awaitDisconnected(timeout: Duration = 20.seconds) = within(timeout, "disconnected") { device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first() }

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
}

/**
 * Runs the client against the netsim peripheral on an emulator. Skipped unless the instrumentation argument
 * [TestPeripheral.ARGUMENT] is `true`, since the peripheral has to be started next to the emulator.
 */
abstract class PeripheralTest {

    @Before
    fun requirePeripheral() {
        assumeTrue(InstrumentationRegistry.getArguments().getString(TestPeripheral.ARGUMENT) == "true")
        // Granted here rather than by a rule, which would run before the assumption on API levels that lack these permissions.
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        listOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT).forEach {
            instrumentation.uiAutomation.grantRuntimePermission(instrumentation.targetContext.packageName, it)
        }
    }

    protected fun peripheralTest(timeout: Duration = 120.seconds, block: suspend PeripheralSession.() -> Unit) = runBlocking {
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
