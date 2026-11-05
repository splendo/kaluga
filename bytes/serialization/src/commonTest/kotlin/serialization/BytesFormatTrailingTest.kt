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
package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.DeltaCoded
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.PresentWhenAllSet
import com.splendo.kaluga.bytes.annotations.Reserved
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidTrailingLayout
import com.splendo.kaluga.bytes.layout.TrailingAfterAbsent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * `@Trailing` properties: an optional tail of a structure, present when the bytes of the value reach it, so a value
 * can be sent with or without the fields at its end.
 */
class BytesFormatTrailingTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    // A flag header, a number of several sizes and a nullable property before the tail, and a number of several sizes in it.
    @Serializable
    data class Status(
        @FlagIndex(0) val charging: Boolean,
        @FlagIndex(1) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val level: Int,
        val mode: Byte?,
        val voltage: UShort,
        @Trailing val serviceVersion: UShort?,
        @Trailing @FlagIndex(4) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val uptime: Int?,
    )

    @Serializable
    @LengthPrefix
    sealed interface Info

    @Serializable
    @SerializedByteValue(0x01)
    data class Device(val model: UShort, @Trailing val serial: ULong?, @Trailing val word: UShort?) : Info

    @Serializable
    @SerializedByteValue(0x02)
    data class Battery(val percent: UByte) : Info

    // The discriminator takes no bytes, so it may follow the tail.
    @Serializable
    @Fallback
    data class Unknown(val first: Byte, @Trailing val second: Byte?, @Discriminator val type: UByte) : Info

    @Serializable
    data class Infos(val count: Byte, @Unsized val infos: List<Info>)

    @Serializable
    @Checksum(8, 0x07u, 0x00u)
    @Postfix([0x7F])
    data class Framed(val first: Byte, @Trailing val second: Short?)

    @Serializable
    data class Calibration(val version: Byte, @Trailing @Count(3) val points: List<Short>?, @Trailing val label: String?)

    @Serializable
    data class Span(val low: Short, val high: Short)

    @Serializable
    data class Limits(val id: Byte, @Trailing val span: Span?)

    // A structure ending in a tail runs to the end of what holds it, so it can be the last property.
    @Serializable
    data class Holder(val id: Byte, val limits: Limits)

    private val status = Status(charging = true, level = 300, mode = null, voltage = 0x0102u, serviceVersion = 0x0304u, uptime = 5)
    private val statusBytes = bytes(0x03, 0x2C, 0x01, 0x02, 0x01, 0x04, 0x03, 0x05, 0x00)

    @Test
    fun readsAValueWithoutTheFieldsItsBytesDoNotReach() {
        validateEncoding(status, Status.serializer(), statusBytes)
        validateEncoding(status.copy(uptime = null), Status.serializer(), statusBytes.copyOf(7))
        validateEncoding(status.copy(serviceVersion = null, uptime = null), Status.serializer(), statusBytes.copyOf(5))
        // The flag header keeps its size and the bits the tail selects a size with, whether the tail is there or not.
        validateEncoding(status.copy(mode = 7, uptime = 70000), Status.serializer(), bytes(0x17, 0x2C, 0x01, 0x07, 0x02, 0x01, 0x04, 0x03, 0x70, 0x11, 0x01, 0x00))
        assertEquals(status.copy(uptime = null), BytesFormat.decodeFromByteArray(Status.serializer(), bytes(0x13) + statusBytes.copyOfRange(1, 7)))
    }

    @Test
    fun rejectsBytesEndingInsideATrailingFieldOrBeforeTheTail() {
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Status.serializer(), statusBytes.copyOf(6)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Status.serializer(), statusBytes.copyOf(8)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Status.serializer(), statusBytes.copyOf(4)) }
    }

    @Test
    fun rejectsATrailingFieldAfterANullOne() {
        val error = assertFailsWith<TrailingAfterAbsent> { BytesFormat.encodeToByteArray(Status.serializer(), status.copy(serviceVersion = null)) }
        assertEquals("uptime", error.name)
        assertEquals("serviceVersion", error.absent)
    }

    @Test
    fun boundsTheTailOfARecordByItsLength() {
        val full = Device(0x0102u, 0x1122334455667788u, 0x0A0Bu)
        validateEncoding(full, Info.serializer(), bytes(0x01, 0x0C, 0x02, 0x01, 0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x0B, 0x0A))
        validateEncoding(Device(0x0102u, null, null), Info.serializer(), bytes(0x01, 0x02, 0x02, 0x01))
        validateEncoding(Device(0x0102u, 0x1122334455667788u, null), Info.serializer(), bytes(0x01, 0x0A, 0x02, 0x01, 0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11))
        // Records with and without their tail back to back: each length bounds its own tail.
        val infos = Infos(3, listOf(Device(7u, null, null), Battery(50u), full))
        val expected = bytes(0x03) + bytes(0x01, 0x02, 0x07, 0x00) + bytes(0x02, 0x01, 0x32) +
            bytes(0x01, 0x0C, 0x02, 0x01, 0x88, 0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x0B, 0x0A)
        validateEncoding(infos, Infos.serializer(), expected)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> {
            BytesFormat.decodeFromByteArray(Info.serializer(), bytes(0x01, 0x05, 0x02, 0x01, 0x88, 0x77, 0x66))
        }
    }

    @Test
    fun keepsTheDiscriminatorOfAFallbackWithATail() {
        validateEncoding(Unknown(5, 6, 0x77u), Info.serializer(), bytes(0x77, 0x02, 0x05, 0x06))
        validateEncoding(Unknown(5, null, 0x77u), Info.serializer(), bytes(0x77, 0x01, 0x05))
    }

    @Test
    fun endsTheTailBeforeTheChecksumAndPostfix() {
        val full = BytesFormat.encodeToByteArray(Framed.serializer(), Framed(1, 0x0203))
        val short = BytesFormat.encodeToByteArray(Framed.serializer(), Framed(1, null))
        assertContentEquals(bytes(0x01, 0x03, 0x02), full.copyOf(3))
        assertEquals(5, full.size)
        assertEquals(3, short.size)
        assertEquals(Framed(1, 0x0203), BytesFormat.decodeFromByteArray(Framed.serializer(), full))
        assertEquals(Framed(1, null), BytesFormat.decodeFromByteArray(Framed.serializer(), short))
    }

    @Test
    fun readsCollectionsStringsAndLayoutsInTheTail() {
        validateEncoding(Calibration(1, listOf(2, 3, 4), "ab"), Calibration.serializer(), bytes(0x01, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x02, 'a'.code, 'b'.code))
        validateEncoding(Calibration(1, listOf(2, 3, 4), null), Calibration.serializer(), bytes(0x01, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00))
        validateEncoding(Calibration(1, null, null), Calibration.serializer(), bytes(0x01))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Calibration.serializer(), bytes(0x01, 0x02, 0x00)) }
        validateEncoding(Holder(9, Limits(1, Span(-1, 1))), Holder.serializer(), bytes(0x09, 0x01, 0xFF, 0xFF, 0x01, 0x00))
        validateEncoding(Holder(9, Limits(1, null)), Holder.serializer(), bytes(0x09, 0x01))
        assertNull(BytesFormat.decodeFromByteArray(Limits.serializer(), bytes(0x01)).span)
    }

    @Serializable
    data class AfterTail(@Trailing val first: Byte?, val second: Byte)

    @Serializable
    data class NotNullable(@Trailing val first: Byte)

    @Serializable
    data class TrailingBoolean(val first: Byte, @Trailing val flag: Boolean?)

    @Serializable
    data class TrailingFlags(val first: Byte, @Trailing @FlagIndex(0) @FlagWidth(3) val bits: UByte?)

    @Serializable
    data class TrailingGated(@FlagIndex(0) val gate: Boolean, @Trailing @PresentWhenAllSet(0) val first: Byte?)

    @Serializable
    data class TrailingNullIfEmpty(val first: Byte, @Trailing @NullIfEmpty val items: List<Byte>?)

    @Serializable
    data class TrailingReserved(val first: Byte, @Trailing @Reserved(before = 1) val second: Byte?)

    @Serializable
    data class TrailingUnsized(val first: Byte, @Trailing @Unsized val rest: String?)

    @Serializable
    data class TrailingDeltaCoded(val first: Byte, @Trailing @DeltaCoded(Length.`8_BIT`) val rest: List<Short>?)

    @Serializable
    data class TailAfterUnsized(@Unsized val rest: String, @Trailing val second: Byte?)

    @Serializable
    data class TailNotLast(val limits: Limits, val after: Byte)

    @Serializable
    data class TailsInAList(val items: List<Limits>)

    @Serializable
    data class TailInATail(val id: Byte, @Trailing val limits: Limits?)

    @Test
    fun rejectsATrailingFieldTheEndOfTheValueCannotTellApart() {
        fun <T> rejects(serializer: KSerializer<T>, value: T, message: String) {
            val error = assertFailsWith<InvalidTrailingLayout> { BytesFormat.encodeToByteArray(serializer, value) }
            assertEquals(true, error.message?.contains(message), error.message)
        }
        rejects(AfterTail.serializer(), AfterTail(1, 2), "'second' of '${AfterTail.serializer().descriptor.serialName}' follows @Trailing 'first'")
        rejects(NotNullable.serializer(), NotNullable(1), "must be nullable")
        rejects(TrailingBoolean.serializer(), TrailingBoolean(1, true), "is a Boolean")
        rejects(TrailingFlags.serializer(), TrailingFlags(1, 2u), "is in the flag header")
        rejects(TrailingGated.serializer(), TrailingGated(true, 1), "is @PresentWhenAllSet")
        rejects(TrailingNullIfEmpty.serializer(), TrailingNullIfEmpty(1, listOf(2)), "is @NullIfEmpty")
        rejects(TrailingReserved.serializer(), TrailingReserved(1, 2), "is @Reserved")
        rejects(TrailingUnsized.serializer(), TrailingUnsized(1, "a"), "runs to the end of the value itself")
        rejects(TrailingDeltaCoded.serializer(), TrailingDeltaCoded(1, listOf(2)), "runs to the end of the value itself")
        rejects(TailAfterUnsized.serializer(), TailAfterUnsized("a", 1), "could never be reached")
        rejects(TailNotLast.serializer(), TailNotLast(Limits(1, null), 2), "make 'limits' the last property")
        rejects(TailsInAList.serializer(), TailsInAList(emptyList()), "The items of")
        rejects(TailInATail.serializer(), TailInATail(1, null), "runs to the end of the value itself")
    }

    @Test
    fun dumpsTheTail() {
        val dump = layoutDumpOf(Limits.serializer().descriptor)
        assertEquals(
            """
            0 ${Limits.serializer().descriptor.serialName}: struct order=LSB/LSB static=?
              0 id: natural(8 signed) order=LSB/LSB bytes=1
              1 span: struct order=LSB/LSB trailing bytes=?
                0 low: natural(16 signed) order=LSB/LSB bytes=2
                1 high: natural(16 signed) order=LSB/LSB bytes=2
            """.trimIndent() + "\n",
            dump,
        )
    }

    private fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}
