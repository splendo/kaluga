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
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.DeltaCoded
import com.splendo.kaluga.bytes.annotations.ItemByteOrder
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.ItemUnsigned
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidDeltaCoding
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Lists stored as their first item and the step from each item to the next, `@DeltaCoded`. */
class BytesFormatDeltaCodedTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    @Serializable
    data class Samples(@DeltaCoded(Length.`8_BIT`) val samples: List<Short>)

    @Serializable
    data class Wide(@DeltaCoded(Length.`16_BIT`) val samples: List<Short>)

    @Serializable
    data class Unsigned(@DeltaCoded(Length.`8_BIT`) val samples: List<UShort>)

    @Serializable
    data class Ints(@DeltaCoded(Length.`8_BIT`) @ItemSize(Length.`16_BIT`) val samples: List<Int>)

    @Serializable
    data class UnsignedInts(@DeltaCoded(Length.`8_BIT`) @ItemSize(Length.`16_BIT`) @ItemUnsigned val samples: IntArray)

    @Serializable
    data class BigEndian(@DeltaCoded(Length.`8_BIT`) @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val samples: ShortArray)

    @Serializable
    data class AfterHeader(val header: Byte, @Unsized @DeltaCoded(Length.`8_BIT`) val samples: List<Short>)

    @Serializable
    @LengthPrefix
    sealed interface Packet

    @Serializable
    @SerializedByteValue(0x01)
    data class Waveform(@DeltaCoded(Length.`8_BIT`) val samples: List<Short>) : Packet

    @Serializable
    @SerializedByteValue(0x02)
    data class Battery(val level: UByte) : Packet

    @Serializable
    data class Chunk(@Unsized val packets: List<Packet>)

    @Serializable
    data class Followed(@DeltaCoded(Length.`8_BIT`) val samples: List<Short>, val after: Byte)

    @Serializable
    data class Counted(@DeltaCoded(Length.`8_BIT`) @Count(2) val samples: List<Short>)

    @Serializable
    data class Optional(@DeltaCoded(Length.`8_BIT`) val samples: List<Short>?)

    @Serializable
    data class Mapped(@DeltaCoded(Length.`8_BIT`) val samples: Map<Short, Short>)

    @Serializable
    data class Single(@DeltaCoded(Length.`8_BIT`) val sample: Int)

    @Serializable
    data class Distinct(@DeltaCoded(Length.`8_BIT`) val samples: Set<Short>)

    @Serializable
    data class Floats(@DeltaCoded(Length.`8_BIT`) val samples: List<Float>)

    @Serializable
    data class WideSteps(@DeltaCoded(Length.`16_BIT`) val samples: List<Byte>)

    @Serializable
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
    data class Mirrored(@DeltaCoded(Length.`8_BIT`) val samples: List<Short>)

    @Test
    fun encodesTheFirstItemThenTheStepToEachNext() {
        validateEncoding(Samples(listOf(16, 18, 15)), bytes(0x10, 0x00, 0x02, 0xFD))
        validateEncoding(Samples(listOf(5000, 5001, 4999)), bytes(0x88, 0x13, 0x01, 0xFE))
        validateEncoding(Wide(listOf(0, 1000, -1000)), bytes(0x00, 0x00, 0xE8, 0x03, 0x30, 0xF8))
        validateEncoding(AfterHeader(7, listOf(16, 18, 15)), bytes(0x07, 0x10, 0x00, 0x02, 0xFD))
    }

    @Test
    fun wrapsStepsAroundTheSizeOfAnItem() {
        validateEncoding(Samples(listOf(Short.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE)), bytes(0xFF, 0x7F, 0x01, 0xFF))
        validateEncoding(Unsigned(listOf(UShort.MAX_VALUE, 0u, 1u)), bytes(0xFF, 0xFF, 0x01, 0x01))
        validateEncoding(Ints(listOf(32767, -32768)), bytes(0xFF, 0x7F, 0x01))
        val unsigned = UnsignedInts(intArrayOf(65535, 0, 2))
        val encoded = BytesFormat.encodeToByteArray(UnsignedInts.serializer(), unsigned)
        assertContentEquals(bytes(0xFF, 0xFF, 0x01, 0x02), encoded)
        assertContentEquals(unsigned.samples, BytesFormat.decodeFromByteArray(UnsignedInts.serializer(), encoded).samples)
    }

    @Test
    fun writesItemsAndStepsInTheItemsByteOrder() {
        val value = BigEndian(shortArrayOf(16, 18, 15))
        val encoded = BytesFormat.encodeToByteArray(BigEndian.serializer(), value)
        assertContentEquals(bytes(0x00, 0x10, 0x02, 0xFD), encoded)
        assertContentEquals(value.samples, BytesFormat.decodeFromByteArray(BigEndian.serializer(), encoded).samples)
    }

    @Test
    fun readsASingleItemAndNoBytesAsAnEmptyList() {
        validateEncoding(Samples(listOf(5)), bytes(0x05, 0x00))
        // BytesFormat writes no value of no bytes, so the empty list is checked within one.
        assertEquals(Samples(emptyList()), BytesFormat.decodeFromByteArray(Samples.serializer(), bytes()))
        validateRoundTrip(Nested(Samples(emptyList())), Nested.serializer(Samples.serializer()), bytes(0x42, 0x23, 0x22))
        validateEncoding(AfterHeader(7, emptyList()), bytes(0x07))
    }

    @Test
    fun runsToTheEndOfARecordSoRecordsCanFollow() {
        val chunk = Chunk(listOf(Waveform(listOf(16, 18, 15)), Battery(100u), Waveform(listOf(5000)), Waveform(emptyList())))
        val expected = bytes(0x01, 0x04, 0x10, 0x00, 0x02, 0xFD) + bytes(0x02, 0x01, 0x64) + bytes(0x01, 0x02, 0x88, 0x13) + bytes(0x01, 0x00)
        validateEncoding(chunk, Chunk.serializer(), expected)
    }

    @Test
    fun rejectsBytesThatAreNotAFirstItemAndWholeSteps() {
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Samples.serializer(), bytes(0x10)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Wide.serializer(), bytes(0x10, 0x00, 0x02)) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { BytesFormat.decodeFromByteArray(Chunk.serializer(), bytes(0x01, 0x01, 0x10)) }
    }

    @Test
    fun throwsForAStepOrAnItemThatDoesNotFit() {
        val step = assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(Samples.serializer(), Samples(listOf(0, 128))) }
        assertEquals("The step to item 1 of a delta-coded list, from 0 to 128, is 128, which does not fit a signed 8-bit step", step.message)
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(Samples.serializer(), Samples(listOf(0, -129))) }
        val item = assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(Ints.serializer(), Ints(listOf(0, 40000))) }
        assertEquals("Item 1 of a delta-coded list, 40000, does not fit a signed 16-bit item", item.message)
        assertFailsWith<IllegalArgumentException> { BytesFormat.encodeToByteArray(Ints.serializer(), Ints(listOf(-40000))) }
    }

    @Test
    fun letsNothingFollowIt() {
        assertFailsWith<DataAfterUnconstrainedData> { BytesFormat.encodeToByteArray(Followed.serializer(), Followed(listOf(1), 2)) }
        assertFailsWith<DataAfterUnconstrainedData> { BytesFormat.encodeToByteArray(Followed.serializer(), Followed(emptyList(), 2)) }
    }

    @Test
    fun rejectsWhatItCannotApplyTo() {
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Counted.serializer(), Counted(listOf(1, 2))) }
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Optional.serializer(), Optional(null)) }
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Mapped.serializer(), Mapped(emptyMap())) }
        val single = assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Single.serializer(), Single(5)) }
        assertEquals("@DeltaCoded only applies to a list of integers, but 'sample' is kotlin.Int", single.message)
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.decodeFromByteArray(Single.serializer(), bytes(0x05, 0x00, 0x00, 0x00)) }
        // Steps of 0 repeat an item, which a set would drop.
        val distinct = assertFailsWith<InvalidDeltaCoding> { BytesFormat.decodeFromByteArray(Distinct.serializer(), bytes(0x05, 0x00, 0x00, 0x00)) }
        assertEquals("@DeltaCoded only applies to a list of integers, but 'samples' is kotlin.collections.LinkedHashSet", distinct.message)
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Distinct.serializer(), Distinct(setOf(5))) }
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Floats.serializer(), Floats(emptyList())) }
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(WideSteps.serializer(), WideSteps(emptyList())) }
        assertFailsWith<InvalidDeltaCoding> { BytesFormat.encodeToByteArray(Mirrored.serializer(), Mirrored(emptyList())) }
    }

    @Test
    fun dumpsItsStep() {
        val dump = layoutDumpOf(Samples.serializer().descriptor)
        assertEquals(true, "unmarked delta(step=8)" in dump, dump)
    }

    private fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }
}
