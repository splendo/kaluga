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

import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.TestPeripheral
import com.splendo.kaluga.bluetooth.TestPeripheral.Command
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.logging.defaultLogger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

/**
 * Plays the [TestPeripheral] with a kaluga [BluetoothServer], for the client scenarios on another device in place of
 * `peripheral.py`. Runs when the platform says this test run plays it, and serves until no central has made a request
 * for a minute.
 *
 * A server has no say over the link and answers no request the stack answers itself, so it obeys every command but those:
 * ending the link, holding a CCCD write, changing services, the beacon and capping the MTU are logged and left undone,
 * and the scenarios that send them are skipped against this player.
 */
class KalugaTestPeripheral {

    @Test
    fun playTheTestPeripheral() {
        val platform = centralScenarioPlatform
        if (!platform.playsTestPeripheral) {
            platform.skip("this test run does not play the test peripheral")
            return
        }
        platform.setUp()
        runBlocking { Player(platform).play() }
    }

    private class Player(private val platform: CentralScenarioPlatform) {

        private companion object {
            val FIRST_REQUEST = 10.minutes
            val IDLE = 1.minutes
            val LINK_CHECK = 200.milliseconds
            const val MAX_ATTRIBUTE_LENGTH = 512
        }

        private val start = TimeSource.Monotonic.markNow()
        private val job = SupervisorJob()
        private val scope = CoroutineScope(defaultBluetoothServerDispatcher + job)
        private val lastRequest = MutableStateFlow<TimeMark?>(null)
        private var central: ConnectedDevice? = null

        private var value = TestPeripheral.initialValue
        private var longValue = byteArrayOf()
        private var sink = byteArrayOf()
        private val twinValues = TestPeripheral.twinValues.toMutableList()
        private var errorCode = 0
        private var holdRead: Duration? = null
        private val pending = mutableListOf<Job>()

        private lateinit var data: LocalCharacteristic.Notifiable
        private lateinit var indicate: LocalCharacteristic.Notifiable
        private lateinit var twins: List<LocalCharacteristic.Notifiable>

        fun log(message: String) = platform.log("${start.elapsedNow().inWholeMilliseconds} ms: $message")

        suspend fun play() {
            val server = platform.serverBuilder().createServer(
                { ServerSettings(it, autoRequestPermission = false, autoEnableBluetooth = false, logger = defaultLogger) },
                defaultBluetoothServerDispatcher + job,
            ) {}
            try {
                addServices(server)
                assertTrue(server.advertise { serviceUUIDs(TestPeripheral.service) }, "advertising did not start")
                log(READY)
                withTimeoutOrNull(FIRST_REQUEST) { lastRequest.filterNotNull().first() } ?: fail("no central made a request within $FIRST_REQUEST")
                var linked = false
                while ((lastRequest.value?.elapsedNow() ?: Duration.ZERO) < IDLE) {
                    // A stack with a single advertising set ends advertising when a central connects and does not
                    // resume it, so the next connection needs it started again. Where the platform tells no links, its
                    // stack keeps advertising.
                    val connected = central?.let { platform.isConnected(it) }
                    if (connected == false && linked) {
                        server.stopAdvertising()
                        log("the link ended, advertising again -> ${server.advertise { serviceUUIDs(TestPeripheral.service) }}")
                    }
                    if (connected != null) linked = connected
                    delay(LINK_CHECK)
                }
                log("no request for $IDLE")
            } finally {
                server.stopAdvertising()
                server.close()
                job.cancel()
            }
        }

        private fun request(device: ConnectedDevice) {
            central = device
            lastRequest.value = TimeSource.Monotonic.markNow()
        }

        private suspend fun addServices(server: BluetoothServer) {
            val service = assertNotNull(
                server.add(TestPeripheral.service) {
                    characteristic(TestPeripheral.data) {
                        readable { device, offset -> readData(device, offset) }
                        writable { device, written, offset -> write(device, value, written, offset) { value = it } }
                        notifiable(onSubscribe = { request(it) }, onUnsubscribe = { request(it) })
                    }
                    characteristic(TestPeripheral.control) {
                        writable { device, bytes, _ ->
                            request(device)
                            obey(Command.decode(bytes))
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                    characteristic(TestPeripheral.error) {
                        readable { device, _ ->
                            request(device)
                            if (errorCode != 0) GattResponse.Error.from(errorCode) else GattResponse.ReadSuccess(TestPeripheral.errorCharacteristicValue)
                        }
                        writable { device, _, _ ->
                            request(device)
                            if (errorCode != 0) GattResponse.Error.from(errorCode) else GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                    characteristic(TestPeripheral.indicate) {
                        notifiable(properties = setOf(CharacteristicProperty.Indicate), onSubscribe = { request(it) }, onUnsubscribe = { request(it) })
                    }
                    characteristic(TestPeripheral.long) {
                        readable { device, offset -> read(device, longValue, offset) }
                        writable { device, written, offset -> write(device, longValue, written, offset) { longValue = it } }
                    }
                    characteristic(TestPeripheral.sink) {
                        readable { device, offset -> read(device, sink, offset) }
                        writable(properties = setOf(CharacteristicProperty.WriteWithoutResponse)) { device, written, _ ->
                            request(device)
                            sink += written
                            GattResponse.WriteSuccess.Acknowledged
                        }
                    }
                },
                "the test service was not added",
            )
            val twinService = assertNotNull(
                server.add(TestPeripheral.twinService) {
                    TestPeripheral.twinValues.indices.forEach { index ->
                        characteristic(TestPeripheral.twin) {
                            readable { device, offset -> read(device, twinValues[index], offset) }
                            writable { device, written, offset -> write(device, twinValues[index], written, offset) { twinValues[index] = it } }
                            notifiable(onSubscribe = { request(it) }, onUnsubscribe = { request(it) })
                        }
                    }
                },
                "the twin service was not added",
            )
            data = service.notifiable(TestPeripheral.data)
            indicate = service.notifiable(TestPeripheral.indicate)
            twins = twinService.characteristics.map { it as LocalCharacteristic.Notifiable }
        }

        private fun LocalService.notifiable(uuid: UUID) = characteristics.first { it.uuid == uuid } as LocalCharacteristic.Notifiable

        private fun read(device: ConnectedDevice, bytes: ByteArray, offset: Int): GattResponse.ReadResponse {
            request(device)
            return if (offset > bytes.size) GattResponse.InvalidOffset else GattResponse.ReadSuccess(bytes.copyOfRange(offset, bytes.size))
        }

        // A write at offset 0 starts the value over; the parts of a long write the stack hands over one by one follow it.
        private fun write(device: ConnectedDevice, current: ByteArray, part: ByteArray, offset: Int, store: (ByteArray) -> Unit): GattResponse.WriteResponse {
            request(device)
            if (offset + part.size > MAX_ATTRIBUTE_LENGTH) return GattResponse.InvalidAttributeValueLength
            store(if (offset == 0) part else current.copyOf(offset) + part)
            return GattResponse.WriteSuccess.Acknowledged
        }

        private suspend fun readData(device: ConnectedDevice, offset: Int): GattResponse.ReadResponse {
            request(device)
            holdRead?.let { release ->
                holdRead = null
                val released = CompletableDeferred<Unit>()
                log("holding the answer to a read" + if (release > Duration.ZERO) " for $release" else "")
                // One held until the link ends stays unanswered here, as the player cannot tell when that link ended.
                if (release > Duration.ZERO) pending += scope.launch { delay(release).also { released.complete(Unit) } }
                released.await()
                log("answering the held read")
            }
            return read(device, value, offset)
        }

        private fun obey(command: Command) {
            log("command $command")
            when (command) {
                Command.Reset -> {
                    pending.forEach { it.cancel() }
                    pending.clear()
                    holdRead = null
                    errorCode = 0
                    value = TestPeripheral.initialValue
                    longValue = byteArrayOf()
                    sink = byteArrayOf()
                    TestPeripheral.twinValues.forEachIndexed { index, twin -> twinValues[index] = twin }
                }

                is Command.HoldRead -> holdRead = command.releaseMillis.toInt().milliseconds

                is Command.Notify -> later(command.afterMillis) {
                    value = command.value
                    notify(data, command.value)
                }

                is Command.AnswerWithError -> errorCode = command.code.toInt()

                is Command.Indicate -> later(command.afterMillis) { notify(indicate, command.value) }

                is Command.NotifyBurst -> later(0u) {
                    repeat(command.count.toInt()) { notify(data, byteArrayOf(it.toByte(), (it shr 8).toByte()), log = false) }
                    log("notified ${command.count} values")
                }

                is Command.SetLongValue -> longValue = TestPeripheral.counting(command.length.toInt())

                is Command.NotifyTwin -> later(command.afterMillis) { notify(twins[command.index.toInt()], command.value) }

                is Command.NotifyLength -> later(0u) {
                    value = TestPeripheral.counting(command.length.toInt())
                    notify(data, value)
                }

                is Command.SetLongValueTo -> longValue = command.value

                is Command.HoldCccdWrite, is Command.DropWhenHeld, is Command.Drop, is Command.ChangeServices, Command.StartBeacon, Command.StopBeacon, is Command.CapMtu ->
                    log("cannot obey $command")
            }
        }

        private fun later(afterMillis: UShort, action: suspend () -> Unit) {
            pending += scope.launch {
                delay(afterMillis.toInt().milliseconds)
                action()
            }
        }

        private suspend fun notify(characteristic: LocalCharacteristic.Notifiable, value: ByteArray, log: Boolean = true) {
            val sent = characteristic.notifyAll(value)
            if (log || !sent) log("notify ${value.size} bytes on ${characteristic.uuid} -> $sent")
        }
    }

    companion object {
        /** What the player logs once it advertises, for a runner that starts the scenarios after it. */
        const val READY = "playing the test peripheral"
    }
}
