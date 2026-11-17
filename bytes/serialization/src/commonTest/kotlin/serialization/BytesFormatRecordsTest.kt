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

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.annotations.Discriminator
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Reserved
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidSealedLayout
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Type-length-value records: a sealed class with `@LengthPrefix` writes each subtype after its discriminator and its
 * length, and a `@Fallback` subtype keeps the records of types no other subtype has.
 */
class BytesFormatRecordsTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    // Every record starts with a time step whose size the flag header records, as many record streams do.
    @Serializable
    @LengthPrefix
    sealed interface Reading

    @Serializable
    @SerializedByteValue(0x20)
    data class Temperature(@FlagIndex(0) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val step: Int, val celsius: Short) : Reading

    @Serializable
    @SerializedByteValue(0x30)
    data class Note(@FlagIndex(0) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val step: Int, @Unsized val text: String) : Reading

    @Serializable
    @SerializedByteValue(0x40)
    data object Mark : Reading

    // Laid out like the others after the type and the length: its flag header selects the size of its step.
    @Serializable
    @Fallback
    data class Unknown(
        @FlagIndex(0) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val step: Int,
        @Discriminator val type: UByte,
        @Unsized val value: List<UByte>,
    ) : Reading

    @Serializable
    data class Chunk(@FlagIndex(0) @FlagWidth(4) val rewinds: UByte, @FlagIndex(4) val fill: Boolean, val first: UInt, @Unsized val readings: List<Reading>)

    @Serializable
    data class Followed(val reading: Reading, val after: Byte)

    private val serializer: KSerializer<Reading> = Reading.serializer()

    @Test
    fun encodesTheTypeTheLengthOfTheSubtypeAndTheSubtype() {
        // The flags select the 16-bit step, 1000; -5 follows.
        validateEncoding(Temperature(1000, -5), serializer, bytes(0x20, 0x05, 0x01, 0xE8, 0x03, 0xFB, 0xFF))
        validateEncoding(Note(0, "hi"), serializer, bytes(0x30, 0x04, 0x00, 0x00, 'h'.code, 'i'.code))
        validateEncoding(Mark, serializer, bytes(0x40, 0x00))
    }

    @Test
    fun keepsATypeNoSubtypeHasInTheFallback() {
        validateEncoding(Unknown(1000, 0x99u, listOf(1u, 2u, 3u)), serializer, bytes(0x99, 0x06, 0x01, 0xE8, 0x03, 0x01, 0x02, 0x03))
        validateEncoding(Unknown(0, 0x98u, emptyList()), serializer, bytes(0x98, 0x02, 0x00, 0x00))
    }

    @Test
    fun writesAnUnknownRecordBackAsItWas() {
        val record = bytes(0x77, 0x07, 0x02, 0x00, 0x00, 0x01, 0x00, 0xAA, 0xBB)
        val decoded = BytesFormat.decodeFromByteArray(serializer, record)
        assertEquals(Unknown(65536, 0x77u, listOf(0xAAu, 0xBBu)), decoded)
        assertContentEquals(record, BytesFormat.encodeToByteArray(serializer, decoded))
    }

    @Test
    fun readsRecordsBackToBackToTheEndOfTheirBound() {
        val chunk = Chunk(1u, true, 256u, listOf(Note(0, "x"), Unknown(3, 0x55u, listOf(9u)), Temperature(-2, 21), Mark))
        val expected = bytes(0x11, 0x00, 0x01, 0x00, 0x00) +
            bytes(0x30, 0x03, 0x00, 0x00, 'x'.code) +
            bytes(0x55, 0x03, 0x00, 0x03, 0x09) +
            bytes(0x20, 0x04, 0x00, 0xFE, 0x15, 0x00) +
            bytes(0x40, 0x00)
        validateEncoding(chunk, Chunk.serializer(), expected)
    }

    @Test
    fun boundsAnUnsizedLastPropertySoDataCanFollowIt() {
        validateEncoding(Followed(Note(1, "ab"), 7), Followed.serializer(), bytes(0x30, 0x04, 0x00, 0x01, 'a'.code, 'b'.code, 0x07))
    }

    @Test
    fun skipsBytesASubtypeDoesNotRead() {
        // A later version appended two bytes to the temperature.
        val bytes = bytes(0x20, 0x06, 0x00, 0x02, 0x10, 0x00, 0xAA, 0xBB, 0x40, 0x00)
        val chunk = BytesFormat.decodeFromByteArray(Chunk.serializer(), bytes(0x00, 0x00, 0x00, 0x00, 0x00) + bytes)
        assertEquals(listOf(Temperature(2, 16), Mark), chunk.readings)
    }

    @Test
    fun rejectsARecordShorterThanItsSubtypeOrItsLength() {
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(serializer, bytes(0x20, 0x02, 0x00, 0x02)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(serializer, bytes(0x40, 0x02, 0x00)) }
    }

    @Test
    fun rejectsALengthThatDoesNotFit() {
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(serializer, Note(0, "x".repeat(254))) }
    }

    @Test
    fun rejectsAFallbackWithTheTypeOfAnotherSubtype() {
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(serializer, Unknown(0, 0x20u, listOf(1u))) }
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(Command.serializer(), Unsupported(0x01u, emptyList())) }
    }

    @Serializable
    @LengthPrefix(lengthAsShort = true)
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    sealed interface Message

    @Serializable
    @SerializedByteValue(0x01, 0x00)
    data class Text(@Unsized val text: String) : Message

    @Serializable
    @SerializedByteValue(0x02, 0x00)
    data class Value(val value: Short) : Message

    @Serializable
    @Fallback
    data class Other(@Discriminator val type: UShort, val first: Byte, @Unsized val rest: List<Byte>) : Message

    @Test
    fun writesA16BitLengthInTheValueByteOrder() {
        validateEncoding(Text("ab"), Message.serializer(), bytes(0x01, 0x00, 0x00, 0x02, 'a'.code, 'b'.code))
        validateEncoding(Value(0x0102), Message.serializer(), bytes(0x02, 0x00, 0x00, 0x02, 0x01, 0x02))
        // The fallback reads the discriminator as a number in the same order.
        validateEncoding(Other(0x0300u, 4, listOf(5)), Message.serializer(), bytes(0x03, 0x00, 0x00, 0x02, 0x04, 0x05))
    }

    @Serializable
    sealed interface Command

    @Serializable
    @SerializedByteValue(0x01)
    data class Start(val profile: Byte) : Command

    @Serializable
    @Fallback
    data class Unsupported(@Discriminator val code: UByte, @Unsized val arguments: List<UByte>) : Command

    @Test
    fun readsAnyOtherDiscriminatorIntoTheFallbackWithoutALength() {
        validateEncoding(Start(3), Command.serializer(), bytes(0x01, 0x03))
        validateEncoding(Unsupported(0x07u, listOf(1u, 2u)), Command.serializer(), bytes(0x07, 0x01, 0x02))
    }

    @Serializable
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    sealed interface Reversed

    @Serializable
    @SerializedByteValue(0x01)
    data class ReversedKnown(val value: Short) : Reversed

    @Serializable
    @Fallback
    data class ReversedOther(val first: Short, @Discriminator val type: UByte, val second: Byte) : Reversed

    @Test
    fun putsTheDiscriminatorOfTheFallbackWhereAnyOtherGoesInAReversedFrame() {
        listOf(ReversedKnown(0x0102) to bytes(0x01, 0x02, 0x01), ReversedOther(0x0304, 0x09u, 5) to bytes(0x05, 0x03, 0x04, 0x09)).forEach { (value, bytes) ->
            assertContentEquals(bytes, BytesFormat.encodeToByteArray(Reversed.serializer(), value))
            assertEquals(value, BytesFormat.decodeFromByteArray(Reversed.serializer(), bytes))
        }
    }

    @Serializable
    @LengthPrefix
    sealed interface Strict

    @Serializable
    @SerializedByteValue(0x01)
    data class StrictA(val a: Byte) : Strict

    @Test
    fun rejectsADiscriminatorNoSubtypeHasWithoutAFallback() {
        assertFailsWith<NoSuchElementException> { BytesFormat.decodeFromByteArray(Strict.serializer(), bytes(0x05, 0x01, 0x00)) }
        // With a fallback, it writes the record back as it was.
        val other = bytes(0x05, 0x00, 0x00, 0x02, 0x07, 0x08)
        assertContentEquals(other, BytesFormat.encodeToByteArray(Message.serializer(), BytesFormat.decodeFromByteArray(Message.serializer(), other)))
    }

    @Serializable
    @LengthPrefix
    @SizePolymorphic
    sealed interface BySize

    @Serializable
    data class SizeA(val a: Byte) : BySize

    @Serializable
    @LengthPrefix(canOverflow = true)
    sealed interface Overflowing

    @Serializable
    @SerializedByteValue(0x01)
    data class OverflowingA(val a: Byte) : Overflowing

    @Serializable
    @LengthPrefix
    data class NotSealed(val a: Byte)

    @Serializable
    sealed interface TwoFallbacks

    @Serializable
    @SerializedByteValue(0x01)
    data class TwoFallbacksA(val a: Byte) : TwoFallbacks

    @Serializable
    @Fallback
    data class TwoFallbacksB(@Discriminator val type: Byte) : TwoFallbacks

    @Serializable
    @Fallback
    data class TwoFallbacksC(@Discriminator val type: Byte) : TwoFallbacks

    @Serializable
    sealed interface WideType

    @Serializable
    @SerializedByteValue(0x01)
    data class WideTypeA(val a: Byte) : WideType

    @Serializable
    @Fallback
    data class WideTypeB(@Discriminator val type: Short) : WideType

    @Serializable
    sealed interface Unmarked

    @Serializable
    @SerializedByteValue(0x01)
    data class UnmarkedA(val a: Byte) : Unmarked

    @Serializable
    @Fallback
    data class UnmarkedB(val type: Byte, @Unsized val rest: List<Byte>) : Unmarked

    @Serializable
    sealed interface TwiceMarked

    @Serializable
    @SerializedByteValue(0x01)
    data class TwiceMarkedA(val a: Byte) : TwiceMarked

    @Serializable
    @Fallback
    data class TwiceMarkedB(@Discriminator val type: Byte, @Discriminator val again: Byte) : TwiceMarked

    @Serializable
    sealed interface MarkedKnown

    @Serializable
    @SerializedByteValue(0x01)
    data class MarkedKnownA(@Discriminator val a: Byte) : MarkedKnown

    @Serializable
    sealed interface OptionalType

    @Serializable
    @SerializedByteValue(0x01)
    data class OptionalTypeA(val a: Byte) : OptionalType

    @Serializable
    @Fallback
    data class OptionalTypeB(@Discriminator val type: Byte?) : OptionalType

    @Serializable
    sealed interface PaddedType

    @Serializable
    @SerializedByteValue(0x01)
    data class PaddedTypeA(val a: Byte) : PaddedType

    @Serializable
    @Fallback
    data class PaddedTypeB(@Reserved(after = 1) @Discriminator val type: Byte) : PaddedType

    @Serializable
    @JvmInline
    @Prefix([0x7A])
    value class FramedKind(val value: UByte)

    @Serializable
    sealed interface FramedType

    @Serializable
    @SerializedByteValue(0x01)
    data class FramedTypeA(val a: Byte) : FramedType

    @Serializable
    @Fallback
    data class FramedTypeB(@Discriminator val kind: FramedKind, @Unsized val rest: List<Byte>) : FramedType

    @Serializable
    @LengthPrefix
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    sealed interface MirroredRecord

    @Serializable
    @SerializedByteValue(0x01)
    data class MirroredRecordA(val a: Byte) : MirroredRecord

    @Test
    fun rejectsRecordsAndFallbacksThatCannotApply() {
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(BySize.serializer(), SizeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(Overflowing.serializer(), OverflowingA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(NotSealed.serializer(), NotSealed(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(TwoFallbacks.serializer(), TwoFallbacksA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(WideType.serializer(), WideTypeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(OptionalType.serializer(), OptionalTypeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(PaddedType.serializer(), PaddedTypeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(MarkedKnown.serializer(), MarkedKnownA(1)) }
        val unmarked = assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(Unmarked.serializer(), UnmarkedA(1)) }
        assertEquals(
            "@Fallback '${UnmarkedB.serializer().descriptor.serialName}' needs exactly one @Discriminator property to hold the discriminator it was read for, but has none",
            unmarked.message,
        )
        val twice = assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(TwiceMarked.serializer(), TwiceMarkedA(1)) }
        assertEquals(
            "@Fallback '${TwiceMarkedB.serializer().descriptor.serialName}' needs exactly one @Discriminator property to hold the discriminator it was read for, " +
                "but has 'type', 'again'",
            twice.message,
        )
        val framed = assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(FramedType.serializer(), FramedTypeA(1)) }
        assertEquals(
            "The @Discriminator property 'kind' of @Fallback '${FramedTypeB.serializer().descriptor.serialName}' takes no bytes in it, " +
                "so it cannot have a prefix, a postfix, a checksum or byte stuffing around it",
            framed.message,
        )
        val mirroredRecord = assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(MirroredRecord.serializer(), MirroredRecordA(1)) }
        assertContains(
            mirroredRecord.message.orEmpty(),
            "@LengthPrefix on '${MirroredRecord.serializer().descriptor.serialName}' needs a frame accumulated LEAST_SIGNIFICANT_FIRST",
        )
    }

    private fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}
