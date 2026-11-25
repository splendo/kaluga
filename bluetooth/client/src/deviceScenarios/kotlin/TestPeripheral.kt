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

import com.splendo.kaluga.bytes.toHexString
import kotlin.time.Duration

/**
 * The GATT peripheral run by `src/deviceScenarios/python/peripheral.py`. The UUIDs and the control commands mirror
 * that script.
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
    val beaconServiceDataUuid: UUID = uuidFrom("0000fff0-0000-1000-8000-00805f9b34fb")
    val beaconServiceData = byteArrayOf(0x5A, 0x01, 0x02, 0x03)
    const val BEACON_COMPANY = 0xFFFF
    val beaconManufacturerData = byteArrayOf(0xCA.toByte(), 0xFE.toByte(), 0x00, 0x01)

    private fun uuid(short: String): UUID = uuidFrom("6b7a$short-5d1c-4f0e-9a51-2f6f3c1d8e01")

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
