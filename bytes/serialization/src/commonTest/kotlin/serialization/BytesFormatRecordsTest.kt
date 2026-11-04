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
import com.splendo.kaluga.bytes.annotations.Fallback
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidSealedLayout
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.test.Test
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

    @Serializable
    @Fallback
    data class Unknown(val type: UByte, @Unsized val value: List<UByte>) : Reading

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
        validateEncoding(Unknown(0x99u, listOf(1u, 2u, 3u)), serializer, bytes(0x99, 0x03, 0x01, 0x02, 0x03))
        validateEncoding(Unknown(0x98u, emptyList()), serializer, bytes(0x98, 0x00))
    }

    @Test
    fun readsRecordsBackToBackToTheEndOfTheirBound() {
        val chunk = Chunk(1u, true, 256u, listOf(Note(0, "x"), Unknown(0x55u, listOf(9u)), Temperature(-2, 21), Mark))
        val expected = bytes(0x11, 0x00, 0x01, 0x00, 0x00) +
            bytes(0x30, 0x03, 0x00, 0x00, 'x'.code) +
            bytes(0x55, 0x01, 0x09) +
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
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(serializer, Unknown(0x20u, listOf(1u))) }
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
    data class Other(val type: UShort, val first: Byte, @Unsized val rest: List<Byte>) : Message

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
    data class Unsupported(val code: UByte, @Unsized val arguments: List<UByte>) : Command

    @Test
    fun readsAnyOtherDiscriminatorIntoTheFallbackWithoutALength() {
        validateEncoding(Start(3), Command.serializer(), bytes(0x01, 0x03))
        validateEncoding(Unsupported(0x07u, listOf(1u, 2u)), Command.serializer(), bytes(0x07, 0x01, 0x02))
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
    data class TwoFallbacksB(val type: Byte) : TwoFallbacks

    @Serializable
    @Fallback
    data class TwoFallbacksC(val type: Byte) : TwoFallbacks

    @Serializable
    sealed interface WideType

    @Serializable
    @SerializedByteValue(0x01)
    data class WideTypeA(val a: Byte) : WideType

    @Serializable
    @Fallback
    data class WideTypeB(val type: Short) : WideType

    @Serializable
    sealed interface FlaggedFallback

    @Serializable
    @SerializedByteValue(0x01)
    data class FlaggedFallbackA(val a: Byte) : FlaggedFallback

    @Serializable
    @Fallback
    data class FlaggedFallbackB(val type: Byte, val maybe: Byte?) : FlaggedFallback

    @Test
    fun rejectsRecordsAndFallbacksThatCannotApply() {
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(BySize.serializer(), SizeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(Overflowing.serializer(), OverflowingA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(NotSealed.serializer(), NotSealed(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(TwoFallbacks.serializer(), TwoFallbacksA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(WideType.serializer(), WideTypeA(1)) }
        assertFailsWith<InvalidSealedLayout> { BytesFormat.encodeToByteArray(FlaggedFallback.serializer(), FlaggedFallbackA(1)) }
    }

    private fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}
