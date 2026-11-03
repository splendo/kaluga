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

import com.splendo.kaluga.bytes.annotations.ByteStuffedCobs
import com.splendo.kaluga.bytes.annotations.Count
import com.splendo.kaluga.bytes.annotations.ItemNullTerminated
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.InvalidCountLayout
import com.splendo.kaluga.bytes.layout.UnexpectedItemCount
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BytesFormatCountTest {

    @Serializable
    private class Registers(@Count(3) val first: ByteArray, val id: Byte, @Count(2) val middle: List<UShort>, val marker: Byte, @Count(4) val last: ByteArray)

    @Serializable
    @SizePolymorphic
    private sealed interface Block

    @Serializable
    private class Coefficients(@Count(21) val registers: ByteArray) : Block

    @Serializable
    private class Calibration(val index: Byte, @Count(36) val values: ByteArray) : Block

    @Serializable
    private data class Trims(@Count(14) val trims: List<UShort>) : Block

    @Serializable
    private class Record(@Count(2) val raw: ByteArray, val tag: Byte)

    @Serializable
    private data class Optional(@Count(2) val values: List<Byte>?, val after: Byte)

    @Serializable
    private data class Pairs(@Count(2) val pairs: Map<Byte, UShort>, val after: Byte)

    @Serializable
    private data class Names(@Count(2) @ItemNullTerminated val names: List<String>, val after: Byte)

    @Serializable
    private data class Empty(@Count(0) val nothing: List<Int>, val after: Byte)

    @Serializable
    private data class Negative(@Count(-1) val values: List<Byte>)

    @Serializable
    private data class WithLengthPrefix(@Count(2) @LengthPrefix val values: List<Byte>)

    @Serializable
    private data class WithUnsized(@Count(2) @Unsized val values: List<Byte>)

    @Serializable
    private data class WithSize(@Count(2) @Size(Length.`16_BIT`) val values: List<Byte>)

    @Serializable
    private data class WithNullIfEmpty(@Count(2) @NullIfEmpty val values: List<Byte>)

    @Serializable
    private data class WithStuffing(@Count(2) @ByteStuffedCobs val values: List<Byte>)

    @Serializable
    private data class OnNumber(@Count(2) val value: Int)

    private val registers = Registers(byteArrayOf(1, 2, 3), 0x10, listOf(0x0102u, 0x0304u), 0x7F, byteArrayOf(9, 8, 7, 6))
    private val registersBytes = byteArrayOf(1, 2, 3, 0x10, 0x02, 0x01, 0x04, 0x03, 0x7F, 9, 8, 7, 6)

    private fun assertRegisters(expected: Registers, actual: Registers) {
        assertContentEquals(expected.first, actual.first)
        assertEquals(expected.id, actual.id)
        assertEquals(expected.middle, actual.middle)
        assertEquals(expected.marker, actual.marker)
        assertContentEquals(expected.last, actual.last)
    }

    private fun <T> assertEncodes(serializer: KSerializer<T>, value: T, expected: ByteArray) {
        val bytes = BytesFormat.encodeToByteArray(serializer, value)
        assertTrue(bytes.contentEquals(expected), "Expected ${expected.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
    }

    @Test
    fun encodesFixedCountsFirstInTheMiddleAndLast() {
        assertEncodes(Registers.serializer(), registers, registersBytes)
        assertRegisters(registers, BytesFormat.decodeFromByteArray(Registers.serializer(), registersBytes))

        val nested = Nested(registers)
        val nestedBytes = byteArrayOf(0x42, 0x23) + registersBytes + 0x22
        assertEncodes(Nested.serializer(Registers.serializer()), nested, nestedBytes)
        assertRegisters(registers, BytesFormat.decodeFromByteArray(Nested.serializer(Registers.serializer()), nestedBytes).nested)
    }

    @Test
    fun hasAStaticSize() {
        val layout = BinaryDescriptorRegistry.layout(Registers.serializer().descriptor, BytesFormat.serializersModule)
        assertEquals(registersBytes.size, layout.staticByteSize)
        assertEquals(listOf(3, 1, 4, 1, 4), layout.type.children.map { it.staticFieldBytes })
        assertTrue("0 first: list(fixedCount(3)) order=LSB/LSB bytes=3" in layoutDumpOf(Registers.serializer().descriptor), layoutDumpOf(Registers.serializer().descriptor))
    }

    @Test
    fun refusesAnotherNumberOfItems() {
        val tooShort = assertFailsWith<UnexpectedItemCount> {
            BytesFormat.encodeToByteArray(Registers.serializer(), Registers(byteArrayOf(1, 2), 0, listOf(1u, 2u), 0, ByteArray(4)))
        }
        assertEquals("'first' must have exactly 3 items, but has 2", tooShort.message)
        assertFailsWith<UnexpectedItemCount> {
            BytesFormat.encodeToByteArray(Registers.serializer(), Registers(ByteArray(3), 0, listOf(1u, 2u), 0, ByteArray(5)))
        }
        assertFailsWith<UnexpectedItemCount> {
            BytesFormat.encodeToByteArray(Registers.serializer(), Registers(ByteArray(3), 0, listOf(1u), 0, ByteArray(4)))
        }
    }

    @Test
    fun decodingTooFewBytesThrows() {
        for (size in registersBytes.indices) {
            assertFailsWith<ByteArrayEndedBeforeSerializationCompleted>("$size bytes") {
                BytesFormat.decodeFromByteArray(Registers.serializer(), registersBytes.copyOf(size))
            }
        }
    }

    @Test
    fun sizesSizePolymorphicSubtypes() {
        val serializer = Block.serializer()
        val coefficients = ByteArray(21) { it.toByte() }
        val coefficientsDecoded = BytesFormat.decodeFromByteArray(serializer, BytesFormat.encodeToByteArray(serializer, Coefficients(coefficients)))
        assertContentEquals(coefficients, (coefficientsDecoded as Coefficients).registers)

        val values = ByteArray(36) { (it * 3).toByte() }
        val calibrationBytes = BytesFormat.encodeToByteArray(serializer, Calibration(5, values))
        assertContentEquals(byteArrayOf(5) + values, calibrationBytes)
        val calibration = BytesFormat.decodeFromByteArray(serializer, calibrationBytes) as Calibration
        assertEquals(5, calibration.index)
        assertContentEquals(values, calibration.values)

        val trims = Trims(List(14) { (it * 1000).toUShort() })
        assertEquals(28, BytesFormat.encodeToByteArray(serializer, trims).size)
        assertEquals(trims, BytesFormat.decodeFromByteArray(serializer, BytesFormat.encodeToByteArray(serializer, trims)))

        val dump = layoutDumpOf(serializer.descriptor)
        listOf(21, 37, 28).forEach { size -> assertTrue("= size $size -> " in dump, dump) }
    }

    @Test
    fun encodesFixedCountsInListItems() {
        val records = listOf(Record(byteArrayOf(1, 2), 3), Record(byteArrayOf(4, 5), 6))
        val serializer = ListSerializer(Record.serializer())
        val bytes = byteArrayOf(2, 1, 2, 3, 4, 5, 6)
        assertEncodes(serializer, records, bytes)
        val decoded = BytesFormat.decodeFromByteArray(serializer, bytes)
        assertEquals(records.map { it.raw.toList() to it.tag }, decoded.map { it.raw.toList() to it.tag })
    }

    @Test
    fun aNullableFixedCountKeepsItsPresenceFlag() {
        assertEncodes(Optional.serializer(), Optional(null, 9), byteArrayOf(0, 9))
        assertEncodes(Optional.serializer(), Optional(listOf(1, 2), 9), byteArrayOf(1, 1, 2, 9))
        assertEquals(Optional(null, 9), BytesFormat.decodeFromByteArray(Optional.serializer(), byteArrayOf(0, 9)))
        assertEquals(Optional(listOf(1, 2), 9), BytesFormat.decodeFromByteArray(Optional.serializer(), byteArrayOf(1, 1, 2, 9)))
        assertFailsWith<UnexpectedItemCount> { BytesFormat.encodeToByteArray(Optional.serializer(), Optional(emptyList(), 9)) }
        assertNull(BinaryDescriptorRegistry.layout(Optional.serializer().descriptor, BytesFormat.serializersModule).staticByteSize)
    }

    @Test
    fun countsTheEntriesOfAMap() {
        val pairs = Pairs(mapOf(1.toByte() to 0x0203u, 4.toByte() to 0x0506u), 7)
        val bytes = byteArrayOf(1, 3, 2, 4, 6, 5, 7)
        assertEncodes(Pairs.serializer(), pairs, bytes)
        assertEquals(pairs, BytesFormat.decodeFromByteArray(Pairs.serializer(), bytes))
        assertEquals(bytes.size, BinaryDescriptorRegistry.layout(Pairs.serializer().descriptor, BytesFormat.serializersModule).staticByteSize)
    }

    @Test
    fun countsItemsOfVaryingSize() {
        val names = Names(listOf("ab", ""), 5)
        val bytes = byteArrayOf(0x61, 0x62, 0, 0, 5)
        assertEncodes(Names.serializer(), names, bytes)
        assertEquals(names, BytesFormat.decodeFromByteArray(Names.serializer(), bytes))
        assertNull(BinaryDescriptorRegistry.layout(Names.serializer().descriptor, BytesFormat.serializersModule).staticByteSize)
    }

    @Test
    fun aCountOfZeroWritesNothing() {
        assertEncodes(Empty.serializer(), Empty(emptyList(), 5), byteArrayOf(5))
        assertEquals(Empty(emptyList(), 5), BytesFormat.decodeFromByteArray(Empty.serializer(), byteArrayOf(5)))
    }

    @Test
    fun rejectsCountsThatCannotApply() {
        fun <T> assertRejected(serializer: KSerializer<T>, value: T) {
            assertFailsWith<InvalidCountLayout> { BytesFormat.encodeToByteArray(serializer, value) }
        }
        assertRejected(Negative.serializer(), Negative(emptyList()))
        assertRejected(WithLengthPrefix.serializer(), WithLengthPrefix(listOf(1, 2)))
        assertRejected(WithUnsized.serializer(), WithUnsized(listOf(1, 2)))
        assertRejected(WithSize.serializer(), WithSize(listOf(1, 2)))
        assertRejected(WithNullIfEmpty.serializer(), WithNullIfEmpty(listOf(1, 2)))
        assertRejected(WithStuffing.serializer(), WithStuffing(listOf(1, 2)))
        assertRejected(OnNumber.serializer(), OnNumber(2))
    }
}
