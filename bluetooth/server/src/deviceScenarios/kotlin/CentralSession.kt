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

import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.logging.defaultLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * A [BluetoothServer] serving the [TestCentral] service, and the central connected to it.
 * The central reports each instruction's result, and every notification it receives, by writing to the result characteristic.
 */
class CentralSession(val platform: CentralScenarioPlatform) {

    private val start = TimeSource.Monotonic.markNow()
    private val job = SupervisorJob()
    val scope = CoroutineScope(defaultBluetoothServerDispatcher + job)

    private val results = Channel<TestCentral.Report.Result>(Channel.UNLIMITED)

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
        platform.log("${start.elapsedNow().inWholeMilliseconds} ms: $message")
    }

    suspend fun <T> within(duration: Duration, what: String, block: suspend () -> T): T {
        val result = withTimeoutOrNull(duration) { block() } ?: fail("$what did not finish within $duration")
        log("$what -> $result")
        return result
    }

    suspend fun open() {
        server = platform.serverBuilder().createServer(
            { ServerSettings(it, autoRequestPermission = false, autoEnableBluetooth = false, logger = defaultLogger) },
            defaultBluetoothServerDispatcher + job,
        ) {}
        val service = assertNotNull(
            within(10.seconds, "add the service") {
                server.add(TestCentral.service) {
                    characteristic(TestCentral.control) { notifiable(onSubscribe = {}, onUnsubscribe = {}) }
                    characteristic(TestCentral.result) {
                        writable { _, value, _ ->
                            when (val report = TestCentral.Report.decode(value)) {
                                is TestCentral.Report.Forwarded -> forwarded.trySend(report.target.toInt() to report.value)
                                is TestCentral.Report.Result -> results.trySend(report)
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
        val message = TestCentral.Message(id.toUByte(), instruction)
        assertEquals(true, within(10.seconds, "send $instruction") { control.notifyAll(message.bytes) })
        val result = within(20.seconds, "result of $instruction") { results.receive() }
        assertEquals(id, result.id.toInt(), "result for another instruction")
        return result.status.toInt() to result.data
    }

    suspend fun status(instruction: TestCentral.Instruction): Int = instruct(instruction).first

    /** Stages [bytes] at the central, in as many instructions as it takes. */
    suspend fun stage(bytes: ByteArray) {
        assertEquals(0, status(TestCentral.Instruction.Clear))
        bytes.toList().chunked(TestCentral.STAGE_SIZE).forEach { assertEquals(0, status(TestCentral.Instruction.Stage(it.toByteArray()))) }
    }

    /** Whether the central is connected, told by its subscription to the control characteristic where the platform does not tell. */
    fun isCentralConnected() = ::central.isInitialized && (platform.isConnected(central) ?: control.subscribedDevices.value.contains(central))

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
 * Runs a [BluetoothServer] against the [TestCentral]. Skipped unless the platform says the central is running, since it
 * has to be started next to the test run.
 */
abstract class CentralTest {

    protected fun centralTest(timeout: Duration = 120.seconds, block: suspend CentralSession.() -> Unit) {
        val platform = centralScenarioPlatform
        if (!platform.isEnabled) {
            platform.skip("the test central is not running")
            return
        }
        platform.setUp()
        runBlocking {
            val session = CentralSession(platform)
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
}
