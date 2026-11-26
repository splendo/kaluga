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
import com.splendo.kaluga.bluetooth.server.TestCentral
import com.splendo.kaluga.bluetooth.server.TestCentral.Instruction
import com.splendo.kaluga.bluetooth.server.TestCentral.Report
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Plays the [TestCentral] with a kaluga [BluetoothClient], for the server scenarios on another device in place of
 * `central.py`. Runs when the platform says this test run plays it: it scans for the scenarios' servers and serves each
 * one that advertises, until none has advertised for a minute.
 *
 * Its stack splits a long write itself and sends no prepared write of the client's choosing, so it answers
 * [Instruction.Prepare] and [Instruction.Execute] with [TestCentral.FAILURE], and the scenarios that send them are skipped
 * against this player.
 */
class KalugaTestCentral {

    @Test
    fun playTheTestCentral() {
        val platform = peripheralScenarioPlatform
        if (!platform.playsTestCentral) {
            platform.skip("this test run does not play the test central")
            return
        }
        platform.setUp()
        runBlocking {
            val session = PeripheralSession(platform)
            try {
                Player(session).play()
            } finally {
                session.client.stopScanning(BluetoothClient.CleanMode.REMOVE_ALL)
            }
        }
    }

    private class Player(private val session: PeripheralSession) {

        private companion object {
            val FIRST_SERVER = 10.minutes
            val IDLE = 1.minutes
        }

        private var staged = byteArrayOf()

        suspend fun play() = with(session) {
            scope.launch { client.devices().collect { } }
            platform.awaitScanStart()
            client.startScanning(setOf(TestCentral.service), BluetoothClient.CleanMode.REMOVE_ALL, connectionSettings)
            log(READY)
            val first = withTimeoutOrNull(FIRST_SERVER) { client.devices().first { it.isNotEmpty() }.first() } ?: fail("no server advertised within $FIRST_SERVER")
            var next: ConnectableDevice? = first
            var served = 0
            while (next != null) {
                serve(next)
                served++
                next = withTimeoutOrNull(IDLE) { nextAdvertiser() }
            }
            log("served $served connections, none advertised for $IDLE")
            assertTrue(served > 0)
        }

        // A server advertises again for the next scenario, possibly under a new address.
        private suspend fun nextAdvertiser(): ConnectableDevice = with(session) {
            val known = client.devices().first().map { it.identifier }.toSet()
            channelFlow {
                client.devices().collectLatest { devices ->
                    coroutineScope {
                        devices.forEach { device ->
                            launch {
                                if (device.identifier in known) device.info.drop(1).first()
                                send(device)
                            }
                        }
                    }
                }
            }.first()
        }

        private suspend fun serve(device: ConnectableDevice) = with(session) {
            log("serving ${device.identifier}")
            if (withTimeoutOrNull(30.seconds) { device.connect() } != true) {
                log("connecting failed")
                return@with
            }
            try {
                val service = within(30.seconds, "discover") { device.discoveredServices().first() }.firstOrNull { it.uuid == TestCentral.service }
                if (service == null) {
                    log("the server has no test service")
                    return@with
                }
                val result = service.characteristics.first { it.uuid == TestCentral.result }
                val instructions = Channel<ByteArray>(Channel.UNLIMITED)
                val subscribed = service.characteristics.first { it.uuid == TestCentral.control }.subscribe { instructions.trySend(it) }
                if (subscribed !is RemoteCharacteristic.SubscriptionResult.DidSubscribe) {
                    log("subscribing to the control characteristic failed: $subscribed")
                    return@with
                }
                val disconnected = scope.async { device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first() }
                val subscriptions = mutableMapOf<UByte, RemoteCharacteristic.Subscription>()
                while (true) {
                    val bytes = select<ByteArray?> {
                        instructions.onReceive { it }
                        disconnected.onAwait { null }
                    } ?: break
                    val message = TestCentral.Message.decode(bytes)
                    log("instruction $message")
                    val (status, data) = carryOut(device, service, message.instruction, result, subscriptions)
                    val written = result.write(Report.Result(message.id, status.toUByte(), data).bytes)
                    if (written !is GattResponse.WriteSuccess) log("writing the result failed: $written")
                    if (message.instruction == Instruction.Disconnect) device.disconnect()
                }
                staged = byteArrayOf()
            } finally {
                withTimeoutOrNull(20.seconds) { device.disconnect() }
                within(20.seconds, "link closed") { platform.awaitLinkClosed(device) }
            }
        }

        private suspend fun carryOut(
            device: ConnectableDevice,
            service: RemoteService,
            instruction: Instruction,
            result: RemoteCharacteristic,
            subscriptions: MutableMap<UByte, RemoteCharacteristic.Subscription>,
        ): Pair<Int, ByteArray> {
            fun target(target: UByte) = service.characteristics.firstOrNull { it.uuid == TestCentral.uuid(target.toInt()) }
            return when (instruction) {
                is Instruction.Stage -> {
                    staged += instruction.bytes
                    0 to u16(staged.size)
                }

                Instruction.Clear -> {
                    staged = byteArrayOf()
                    0 to byteArrayOf()
                }

                is Instruction.Write -> target(instruction.target)?.let { status(it.write(staged.copyOf())) to byteArrayOf() } ?: (TestCentral.NO_TARGET to byteArrayOf())

                is Instruction.Prepare, is Instruction.Execute -> TestCentral.FAILURE to byteArrayOf()

                Instruction.Disconnect -> 0 to byteArrayOf()

                is Instruction.Subscribe -> target(instruction.target)?.let { characteristic ->
                    when (val subscription = characteristic.subscribe { value -> session.scope.launch { result.write(Report.Forwarded(instruction.target, value).bytes) } }) {
                        is RemoteCharacteristic.SubscriptionResult.DidSubscribe -> {
                            subscriptions[instruction.target] = subscription.subscription
                            0 to byteArrayOf()
                        }

                        is RemoteCharacteristic.SubscriptionResult.FailedToSubscribe -> status(subscription.response) to byteArrayOf()
                    }
                } ?: (TestCentral.NO_TARGET to byteArrayOf())

                is Instruction.Unsubscribe -> subscriptions.remove(instruction.target)?.let { status(it.unsubscribe()) to byteArrayOf() }
                    ?: (TestCentral.NO_TARGET to byteArrayOf())

                is Instruction.RequestMtu -> when (val response = device.requestMtu(instruction.mtu.toInt()).response.await()) {
                    is GattResponse.MTUSuccess -> 0 to u16(response.mtu)
                    else -> TestCentral.FAILURE to byteArrayOf()
                }
            }
        }

        private fun status(response: GattResponse.WriteResponse) = when (response) {
            is GattResponse.WriteSuccess -> 0
            is GattResponse.Error -> response.statusCode
            else -> TestCentral.FAILURE
        }

        private fun u16(value: Int) = byteArrayOf(value.toByte(), (value shr 8).toByte())
    }

    companion object {
        /** What the player logs once it scans, for a runner that starts the scenarios after it. */
        const val READY = "playing the test central"
    }
}
