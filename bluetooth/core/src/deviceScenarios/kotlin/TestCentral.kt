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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.Serializable

/**
 * The GATT central the server scenarios run against: `central.py` in the server module's `src/deviceScenarios/python`,
 * or a kaluga client playing it. It finds the [service] the scenario's server advertises, subscribes to its [control]
 * characteristic, carries out every [Message] notified there and answers each with a [Report] written to [result].
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

    const val ECHO_MISMATCH = 0xE0
    const val NO_TARGET = 0xE1
    const val FAILURE = 0xE2

    /** The most an instruction can stage at once: a notification at the default MTU, less the message header. */
    const val STAGE_SIZE = 23 - 3 - 2

    /** The UUID of the characteristic of the service a [target] names: the last byte of its first group. */
    fun uuid(target: Int): UUID = uuidFrom("6b7b00${target.toString(16).padStart(2, '0')}-5d1c-4f0e-9a51-2f6f3c1d8e01")

    /** What a scenario may need from the central beyond the instructions every player carries out. */
    enum class Capability(val description: String) {
        PreparedWrites("send prepared writes of its own"),
    }

    /** Who plays the central, and what it can do. */
    enum class Player(val capabilities: Set<Capability>) {

        /** `central.py` on bumble. */
        Script(Capability.entries.toSet()),

        /** A kaluga client on another device, whose stack splits a long write itself. */
        Kaluga(emptySet()),
    }

    /** An [instruction] as the server notifies it on the control characteristic, under an [id] the result carries back. */
    @Serializable
    class Message(val id: UByte, val instruction: Instruction) {
        val bytes: ByteArray get() = BytesFormat.encodeToByteArray(serializer(), this)

        override fun toString() = "Message($id, $instruction)"

        companion object {
            fun decode(bytes: ByteArray): Message = BytesFormat.decodeFromByteArray(serializer(), bytes)
        }
    }

    /** An instruction byte, then the instruction's fields. A target is the last byte of the UUID of a characteristic, see [uuid]. */
    @Serializable
    sealed class Instruction {

        /** Stages [bytes] after what is staged already. The result holds the staged length as a little-endian u16. */
        @Serializable
        @SerializedByteValue(0x01)
        class Stage(@Unsized val bytes: ByteArray) : Instruction()

        /** Clears the staged bytes. */
        @Serializable
        @SerializedByteValue(0x02)
        data object Clear : Instruction()

        /** Writes the staged bytes to [target] with response, as a long write when they do not fit in one request. */
        @Serializable
        @SerializedByteValue(0x03)
        class Write(val target: UByte) : Instruction() {
            constructor(target: Int) : this(target.toUByte())
        }

        /** Sends one Prepare Write Request of [length] staged bytes from [start] at [offset] of [target]. */
        @Serializable
        @SerializedByteValue(0x04)
        class Prepare(val target: UByte, val offset: UShort, val start: UShort, val length: UShort) : Instruction() {
            constructor(target: Int, offset: Int, start: Int, length: Int) : this(target.toUByte(), offset.toUShort(), start.toUShort(), length.toUShort())
        }

        /** Sends an Execute Write Request with these [flags]: 1 writes the prepared queue, 0 cancels it. */
        @Serializable
        @SerializedByteValue(0x05)
        class Execute(val flags: UByte) : Instruction() {
            constructor(write: Boolean) : this(if (write) 1u else 0u)
        }

        /** Disconnects, after answering. */
        @Serializable
        @SerializedByteValue(0x06)
        data object Disconnect : Instruction()

        /** Subscribes to [target] and forwards each notification as a [Report.Forwarded]. */
        @Serializable
        @SerializedByteValue(0x07)
        class Subscribe(val target: UByte) : Instruction() {
            constructor(target: Int) : this(target.toUByte())
        }

        @Serializable
        @SerializedByteValue(0x08)
        class Unsubscribe(val target: UByte) : Instruction() {
            constructor(target: Int) : this(target.toUByte())
        }

        /** Requests an MTU of [mtu]. The result holds the MTU the central then has as a little-endian u16. */
        @Serializable
        @SerializedByteValue(0x09)
        class RequestMtu(val mtu: UShort) : Instruction() {
            constructor(mtu: Int) : this(mtu.toUShort())
        }

        override fun toString() = "${this::class.simpleName}(${BytesFormat.encodeToByteArray(serializer(), this).toHexString()})"
    }

    /** What the central writes to the result characteristic. */
    @Serializable
    sealed class Report {

        /** A notification of [target] the central received. */
        @Serializable
        @SerializedByteValue(0x00)
        class Forwarded(val target: UByte, @Unsized val value: ByteArray) : Report()

        /**
         * The result of the message with this [id]: [status] 0 for success, the ATT error code, [ECHO_MISMATCH] when an echoed
         * part of a prepared write differed from what was sent, [NO_TARGET] for a missing target or [FAILURE] for anything else.
         */
        @Serializable
        @Fallback
        class Result(@Discriminator val id: UByte, val status: UByte, @Unsized val data: ByteArray) : Report()

        val bytes: ByteArray get() = BytesFormat.encodeToByteArray(serializer(), this)

        companion object {
            fun decode(bytes: ByteArray): Report = BytesFormat.decodeFromByteArray(serializer(), bytes)
        }
    }
}
