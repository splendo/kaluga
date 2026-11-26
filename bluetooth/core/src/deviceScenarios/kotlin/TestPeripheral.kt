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

import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * The GATT peripheral the client scenarios run against: `peripheral.py` in the client module's
 * `src/deviceScenarios/python`, or a kaluga server playing it. The UUIDs, the values and the [Command]s are what every
 * player of it serves.
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
    val errorCharacteristicValue = "ok".encodeToByteArray()

    const val NAME = "kaluga-netsim"
    const val BEACON_NAME = "kaluga-beacon"
    val beaconServiceDataUuid: UUID = uuidFrom("0000fff0-0000-1000-8000-00805f9b34fb")
    val beaconServiceData = byteArrayOf(0x5A, 0x01, 0x02, 0x03)
    const val BEACON_COMPANY = 0xFFFF
    val beaconManufacturerData = byteArrayOf(0xCA.toByte(), 0xFE.toByte(), 0x00, 0x01)

    private fun uuid(short: String): UUID = uuidFrom("6b7a$short-5d1c-4f0e-9a51-2f6f3c1d8e01")

    /** [length] bytes counting up from 0, as the peripheral fills its long values. */
    fun counting(length: Int) = ByteArray(length) { it.toByte() }

    /** What a scenario may need from the peripheral beyond serving its attributes and the commands every player obeys. */
    enum class Capability(val description: String) {
        HoldCccdWrite("hold the answer to a CCCD write"),
        HoldReadUntilLinkEnds("hold the answer to a read until the link ends"),
        EndLink("end the link"),
        ChangeServices("change its services while connected"),
        RefusePairing("refuse to pair"),
        CapMtu("cap the MTU of a connection"),
        ExceedAttributeLength("send more than an attribute holds in one read response or notification"),
        Beacon("advertise a beacon next to itself"),
    }

    /** Who plays the peripheral, and what it can do. */
    enum class Player(val capabilities: Set<Capability>) {

        /** `peripheral.py` on bumble. */
        Script(Capability.entries.toSet()),

        /**
         * A kaluga server on another device, which has no say over the link. A read it leaves unanswered CoreBluetooth
         * answers itself after about ten seconds, and Android sends at most an attribute's length in a read response or
         * a notification.
         */
        Kaluga(emptySet()),
        ;

        companion object {
            /** The player a test run's switch names: `true` for the script, `kaluga` for kaluga, anything else for none. */
            fun named(switch: String?): Player? = when (switch) {
                "true" -> Script
                "kaluga" -> Kaluga
                else -> null
            }
        }
    }

    /**
     * A command the client writes to the control characteristic: a command byte, then the command's fields. A command that
     * holds back an answer has to be armed before the request it holds, as the client sends one ATT request at a time.
     */
    @Serializable
    sealed class Command {

        /** Disarms everything, releases held answers and restores the values every connection starts with. */
        @Serializable
        @SerializedByteValue(0x00)
        data object Reset : Command()

        /** Holds the answer to the next read of the data characteristic for [releaseMillis], or until the link ends when 0. */
        @Serializable
        @SerializedByteValue(0x01)
        class HoldRead(val releaseMillis: UShort) : Command() {
            constructor(releaseAfter: Duration = Duration.ZERO) : this(releaseAfter.millis)
        }

        /** Holds the answer to the next write of the data characteristic's CCCD for [releaseMillis], or until the link ends when 0. */
        @Serializable
        @SerializedByteValue(0x02)
        class HoldCccdWrite(val releaseMillis: UShort) : Command() {
            constructor(releaseAfter: Duration = Duration.ZERO) : this(releaseAfter.millis)
        }

        /** Once a held request arrives, ends the link after [afterMillis]. */
        @Serializable
        @SerializedByteValue(0x03)
        class DropWhenHeld(val afterMillis: UShort) : Command() {
            constructor(after: Duration) : this(after.millis)
        }

        /** Ends the link [afterMillis] after answering this command. */
        @Serializable
        @SerializedByteValue(0x04)
        class Drop(val afterMillis: UShort) : Command() {
            constructor(after: Duration) : this(after.millis)
        }

        /** Notifies [value] on the data characteristic [afterMillis] after answering this command. */
        @Serializable
        @SerializedByteValue(0x05)
        class Notify(val afterMillis: UShort, @Unsized val value: ByteArray) : Command() {
            constructor(value: ByteArray, after: Duration = Duration.ZERO) : this(after.millis, value)
        }

        /** Answers every read and write of the error characteristic with the ATT error [code], or succeeds when 0. */
        @Serializable
        @SerializedByteValue(0x06)
        class AnswerWithError(val code: UByte) : Command() {
            constructor(code: Int) : this(code.toUByte())
        }

        /** Indicates [value] on the indicate characteristic [afterMillis] after answering this command. */
        @Serializable
        @SerializedByteValue(0x07)
        class Indicate(val afterMillis: UShort, @Unsized val value: ByteArray) : Command() {
            constructor(value: ByteArray, after: Duration = Duration.ZERO) : this(after.millis, value)
        }

        /** Notifies [count] values on the data characteristic back to back, each its index as a little-endian u16. */
        @Serializable
        @SerializedByteValue(0x08)
        class NotifyBurst(val count: UShort) : Command() {
            constructor(count: Int) : this(count.toUShort())
        }

        /** Sets the value of the long characteristic to [length] bytes counting up from 0. */
        @Serializable
        @SerializedByteValue(0x09)
        class SetLongValue(val length: UShort) : Command() {
            constructor(length: Int) : this(length.toUShort())
        }

        /** Notifies [value] on twin characteristic [index] (0 or 1) [afterMillis] after answering this command. */
        @Serializable
        @SerializedByteValue(0x0A)
        class NotifyTwin(val afterMillis: UShort, val index: UByte, @Unsized val value: ByteArray) : Command() {
            constructor(index: Int, value: ByteArray, after: Duration = Duration.ZERO) : this(after.millis, index.toUByte(), value)
        }

        /** Adds the extra service and indicates Service Changed [afterMillis] after answering this command. */
        @Serializable
        @SerializedByteValue(0x0B)
        class ChangeServices(val afterMillis: UShort) : Command() {
            constructor(after: Duration = Duration.ZERO) : this(after.millis)
        }

        /** Starts advertising the beacon, a second device with a name, manufacturer data and service data. */
        @Serializable
        @SerializedByteValue(0x0C)
        data object StartBeacon : Command()

        @Serializable
        @SerializedByteValue(0x0D)
        data object StopBeacon : Command()

        /** Notifies [length] bytes counting up from 0 on the data characteristic. */
        @Serializable
        @SerializedByteValue(0x0E)
        class NotifyLength(val length: UShort) : Command() {
            constructor(length: Int) : this(length.toUShort())
        }

        /** Sets the value of the long characteristic to [value]. */
        @Serializable
        @SerializedByteValue(0x0F)
        class SetLongValueTo(@Unsized val value: ByteArray) : Command()

        /** Answers the MTU exchange of the next connection with at most [mtu]. */
        @Serializable
        @SerializedByteValue(0x10)
        class CapMtu(val mtu: UShort) : Command() {
            constructor(mtu: Int) : this(mtu.toUShort())
        }

        val bytes: ByteArray get() = BytesFormat.encodeToByteArray(serializer(), this)

        override fun toString() = "${this::class.simpleName}(${bytes.toHexString()})"

        companion object {
            fun decode(bytes: ByteArray): Command = BytesFormat.decodeFromByteArray(serializer(), bytes)
        }
    }

    private val Duration.millis get() = inWholeMilliseconds.toUShort()
}
