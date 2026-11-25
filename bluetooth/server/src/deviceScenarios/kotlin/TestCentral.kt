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

package com.splendo.kaluga.bluetooth.server

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import com.splendo.kaluga.logging.defaultLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import org.junit.Assume.assumeTrue
import org.junit.Before
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * The service a [CentralSession] serves to the GATT central run by `src/deviceScenarios/python/central.py`
 * on the emulator's netsim radio. The UUIDs and the instructions mirror that script.
 */
object TestCentral {
    val service: UUID = uuid(0x00)
    val control: UUID = uuid(0x01)
    val result: UUID = uuid(0x02)
    const val TYPED = 0x03
    const val RAW = 0x04
    const val NOTIFY = 0x05
    val typed: UUID = uuid(TYPED)
    val raw: UUID = uuid(RAW)
    val notify: UUID = uuid(NOTIFY)

    /** The instrumentation argument that tells the tests the central is running. */
    const val ARGUMENT = "netsimCentral"

    const val ECHO_MISMATCH = 0xE0

    /** The most an instruction can stage at once: a notification at the default MTU, less the instruction header. */
    const val STAGE_SIZE = 23 - 3 - 2

    private fun uuid(short: Int): UUID = java.util.UUID.fromString("6b7b00%02x-5d1c-4f0e-9a51-2f6f3c1d8e01".format(short))

    sealed class Instruction(val code: Int, val arguments: ByteArray = byteArrayOf()) {
        class Stage(bytes: ByteArray) : Instruction(1, bytes)
        data object Clear : Instruction(2)
        class Write(target: Int) : Instruction(3, byteArrayOf(target.toByte()))
        class Prepare(target: Int, offset: Int, start: Int, length: Int) : Instruction(4, byteArrayOf(target.toByte()) + u16(offset) + u16(start) + u16(length))
        class Execute(write: Boolean) : Instruction(5, byteArrayOf(if (write) 1 else 0))
        data object Disconnect : Instruction(6)
        class Subscribe(target: Int) : Instruction(7, byteArrayOf(target.toByte()))
        class Unsubscribe(target: Int) : Instruction(8, byteArrayOf(target.toByte()))

        override fun toString() = "${this::class.simpleName}(${arguments.toHexString()})"
    }

    private fun u16(value: Int) = byteArrayOf((value and 0xFF).toByte(), (value shr 8 and 0xFF).toByte())
}

/** A value written to the typed characteristic: longer than one packet, with a tail that may be left out. */
@Serializable
data class Record(val id: UShort, @LengthPrefix val label: String, @Trailing val serial: ULong?) {
    val bytes: ByteArray get() = BytesFormat.encodeToByteArray(serializer(), this)
}

private const val TAG = "CentralTest"

/**
 * A [BluetoothServer] serving the [TestCentral] service, and the central connected to it.
 * The central reports each instruction's result, and every notification it receives, by writing to the result characteristic.
 */
class CentralSession(private val context: Context) {

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val start = TimeSource.Monotonic.markNow()
    private val job = SupervisorJob()
    val scope = CoroutineScope(defaultBluetoothServerDispatcher + job)

    init {
        if (!ApplicationHolder.isInitialized) ApplicationHolder.applicationContext = context
    }

    private val results = Channel<ByteArray>(Channel.UNLIMITED)

    /** Notifications the central received, as (target, value). */
    val forwarded = Channel<Pair<Int, ByteArray>>(Channel.UNLIMITED)
    val typedWrites = Channel<Record>(Channel.UNLIMITED)
    val typedFailures = Channel<Exception>(Channel.UNLIMITED)
    val rawWrites = Channel<Pair<ByteArray, Int>>(Channel.UNLIMITED)

    lateinit var server: BluetoothServer
        private set
    lateinit var control: LocalCharacteristic.Notifiable
        private set
    lateinit var notify: LocalCharacteristic.Notifiable
        private set
    lateinit var central: ConnectedDevice
        private set
    private var nextId = 1

    fun log(message: String) {
        Log.i(TAG, "${start.elapsedNow().inWholeMilliseconds} ms: $message")
    }

    suspend fun <T> within(duration: Duration, what: String, block: suspend () -> T): T {
        val result = withTimeoutOrNull(duration) { block() } ?: fail("$what did not finish within $duration")
        log("$what -> $result")
        return result
    }

    suspend fun open() {
        server = BluetoothServerBuilder(context).createServer(
            { ServerSettings(it, autoRequestPermission = false, autoEnableBluetooth = false, logger = defaultLogger) },
            defaultBluetoothServerDispatcher + job,
        ) {}
        val service = assertNotNull(
            within(10.seconds, "add the service") {
                server.add(TestCentral.service) {
                    characteristic(TestCentral.control) { notifiable(onSubscribe = {}, onUnsubscribe = {}) }
                    characteristic(TestCentral.result) {
                        writable { _, value, _ ->
                            if (value.firstOrNull() == 0.toByte()) {
                                forwarded.trySend(value[1].toInt() to value.copyOfRange(2, value.size))
                            } else {
                                results.trySend(value)
                            }
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                    characteristic(TestCentral.typed) {
                        writable(
                            decode = { BytesFormat.decodeFromByteArray(Record.serializer(), it) },
                            onFailedToWrite = { _, exception ->
                                typedFailures.trySend(exception)
                                GattResponse.ApplicationError(0x80)
                            },
                        ) { _, record ->
                            typedWrites.trySend(record)
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                    characteristic(TestCentral.raw) {
                        writable { _, value, offset ->
                            rawWrites.trySend(value to offset)
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                    characteristic(TestCentral.notify) { notifiable(onSubscribe = {}, onUnsubscribe = {}) }
                }
            },
        )
        control = service.characteristics.first { it.uuid == TestCentral.control } as LocalCharacteristic.Notifiable
        notify = service.characteristics.first { it.uuid == TestCentral.notify } as LocalCharacteristic.Notifiable
        awaitCentral()
    }

    /** Advertises until the central has connected and subscribed to the control characteristic. */
    suspend fun awaitCentral() {
        assertEquals(true, within(10.seconds, "advertise") { server.advertise { serviceUUIDs(TestCentral.service) } })
        central = within(30.seconds, "central subscribed") { control.subscribedDevices.first { it.isNotEmpty() }.single() }
        server.stopAdvertising()
    }

    /** Sends [instruction] to the central and returns its status and data. */
    suspend fun instruct(instruction: TestCentral.Instruction): Pair<Int, ByteArray> {
        val id = nextId.also { nextId = nextId % 255 + 1 }
        val message = byteArrayOf(id.toByte(), instruction.code.toByte()) + instruction.arguments
        assertEquals(true, within(10.seconds, "send $instruction") { control.notifyAll(message) })
        val result = within(20.seconds, "result of $instruction") { results.receive() }
        assertEquals(id, result[0].toInt() and 0xFF, "result for another instruction")
        return (result[1].toInt() and 0xFF) to result.copyOfRange(2, result.size)
    }

    suspend fun status(instruction: TestCentral.Instruction): Int = instruct(instruction).first

    /** Stages [bytes] at the central, in as many instructions as it takes. */
    suspend fun stage(bytes: ByteArray) {
        assertEquals(0, status(TestCentral.Instruction.Clear))
        bytes.toList().chunked(TestCentral.STAGE_SIZE).forEach { assertEquals(0, status(TestCentral.Instruction.Stage(it.toByteArray()))) }
    }

    @SuppressLint("MissingPermission")
    fun isCentralConnected() = ::central.isInitialized && bluetoothManager.getConnectedDevices(BluetoothProfile.GATT_SERVER).any { it.address == central.identifier }

    suspend fun awaitCentralDisconnected() = within(20.seconds, "central disconnected") {
        while (isCentralConnected()) delay(100)
    }

    suspend fun close() {
        if (::server.isInitialized) {
            server.stopAdvertising()
            if (isCentralConnected()) {
                withTimeoutOrNull(10.seconds) { instruct(TestCentral.Instruction.Disconnect) } ?: log("disconnect instruction timed out")
                awaitCentralDisconnected()
            }
            server.close()
        }
        scope.cancel()
        job.cancel()
    }
}

/**
 * Runs a [BluetoothServer] against the netsim central on an emulator. Skipped unless the instrumentation argument
 * [TestCentral.ARGUMENT] is `true`, since the central has to be started next to the emulator.
 */
abstract class CentralTest {

    @Before
    fun requireCentral() {
        assumeTrue(InstrumentationRegistry.getArguments().getString(TestCentral.ARGUMENT) == "true")
        // Granted here rather than by a rule, which would run before the assumption on API levels that lack these permissions.
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        listOf(Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT).forEach {
            instrumentation.uiAutomation.grantRuntimePermission(instrumentation.targetContext.packageName, it)
        }
    }

    protected fun centralTest(timeout: Duration = 120.seconds, block: suspend CentralSession.() -> Unit) = runBlocking {
        val session = CentralSession(InstrumentationRegistry.getInstrumentation().targetContext)
        try {
            session.within(timeout, "test") {
                session.open()
                session.block()
            }
        } finally {
            session.close()
        }
    }
}
