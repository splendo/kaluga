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
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.Serializable

/**
 * The service a [CentralSession] serves to the GATT central run by `src/deviceScenarios/python/central.py`.
 * The UUIDs and the instructions mirror that script.
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

    /** The most an instruction can stage at once: a notification at the default MTU, less the instruction header. */
    const val STAGE_SIZE = 23 - 3 - 2

    private fun uuid(short: Int): UUID = uuidFrom("6b7b00${short.toString(16).padStart(2, '0')}-5d1c-4f0e-9a51-2f6f3c1d8e01")

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
