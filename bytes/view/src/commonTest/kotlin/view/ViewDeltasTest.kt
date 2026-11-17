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

package com.splendo.kaluga.bytes.view

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewDeltasTest {

    private val short = Length.`16_BIT`
    private val byte = Length.`8_BIT`
    private val lsb = ByteOrder.LEAST_SIGNIFICANT_FIRST

    private fun bytes(vararg values: Int): ByteArray = ByteArray(values.size) { values[it].toByte() }

    @Test
    fun readsTheFirstItemThenAddsEveryStep() {
        val b = bytes(0x7E, 0x88, 0x13, 0x01, 0xFE, 0x7F)
        val list = ViewDeltas.shorts(b, 1, 5, short, true, byte, lsb)
        assertEquals(3, list.size)
        assertEquals(listOf<Short>(5000, 5001, 4999), list)
        assertEquals(4999.toShort(), list[2])
        assertFailsWith<IndexOutOfBoundsException> { list[3] }
        val read = mutableListOf<Short>()
        list.forEachShort { read += it }
        assertEquals<List<Short>>(list, read)
        assertContentEquals(shortArrayOf(0, 5000, 5001, 4999), list.copyInto(ShortArray(4), 1))
        assertFailsWith<IndexOutOfBoundsException> { list.copyInto(ShortArray(3), 1) }
        assertEquals(5, ViewDeltas.end(1, 5, short, byte))
    }

    // 5000, 5001, 5000, 4999 and 5002, as Shorts and as Ints.
    private fun lists(b: ByteArray): List<List<Number>> = listOf(
        ViewDeltas.shorts(b, 0, b.size, short, true, byte, lsb),
        ViewDeltas.ints(b, 0, b.size, short, true, byte, lsb),
    )

    private fun samples(): ByteArray = bytes(0x88, 0x13, 0x01, 0xFF, 0xFF, 0x03)

    private fun List<Number>.expected(vararg values: Int): List<Number> = if (this is DeltaCodedShortList) values.map { it.toShort() } else values.toList()

    @Test
    fun keepsTheListContract() {
        for (list in lists(samples())) {
            val expected = list.expected(5000, 5001, 5000, 4999, 5002)
            assertEquals(expected, list)
            assertEquals(list, expected)
            assertEquals(expected.hashCode(), list.hashCode())
            assertEquals(expected.toString(), list.toString())

            val items = list.listIterator()
            val forward = mutableListOf<Number>()
            while (items.hasNext()) {
                assertEquals(forward.size, items.nextIndex())
                forward += items.next()
            }
            assertEquals(expected, forward)
            assertFailsWith<NoSuchElementException> { items.next() }
            val backward = mutableListOf<Number>()
            while (items.hasPrevious()) {
                assertEquals(expected.size - backward.size - 1, items.previousIndex())
                backward += items.previous()
            }
            assertEquals(expected.reversed(), backward)
            assertFailsWith<NoSuchElementException> { items.previous() }

            val fromTwo = list.listIterator(2)
            assertEquals(expected[2], fromTwo.next())
            assertEquals(expected[3], fromTwo.next())
            assertEquals(expected[3], fromTwo.previous())
            assertEquals(expected[2], fromTwo.previous())
            assertEquals(expected[1], fromTwo.previous())
            assertEquals(expected[1], fromTwo.next())
            assertEquals(expected.last(), list.listIterator(list.size).previous())
            assertFailsWith<IndexOutOfBoundsException> { list.listIterator(list.size + 1) }
            assertFailsWith<IndexOutOfBoundsException> { list.listIterator(-1) }

            val middle = list.subList(1, 4)
            assertEquals(expected.subList(1, 4), middle)
            assertEquals(middle, expected.subList(1, 4))
            assertEquals(expected[3], middle[2])
            assertEquals(expected.subList(2, 4), middle.listIterator(1).asSequence().toList())
            assertEquals(expected.subList(2, 3), middle.subList(1, 2))
            assertEquals(expected[1], middle.listIterator(1).previous())
            assertFailsWith<IndexOutOfBoundsException> { middle[3] }
            assertFailsWith<IndexOutOfBoundsException> { middle.subList(2, 4) }
            assertFailsWith<IllegalArgumentException> { list.subList(3, 2) }
            assertEquals(emptyList(), list.subList(5, 5))

            assertEquals(2, list.lastIndexOf(expected[0]))
            assertEquals(0, list.indexOf(expected[0]))
            assertEquals(-1, list.lastIndexOf(list.expected(1)[0]))
            assertEquals(1, middle.lastIndexOf(expected[0]))
            assertEquals(-1, list.subList(3, 5).lastIndexOf(expected[0]))
            assertEquals(expected.asReversed(), list.asReversed())
        }
    }

    @Test
    fun readsForwardFromTheItemBefore() {
        // Overwriting the first item after the iteration passed it shows that every next item adds its step to the
        // item before, rather than adding up the steps from the first item again.
        fun overwritten(read: (List<Number>) -> Iterator<Number>, before: Int, after: Int) {
            val b = samples()
            for (list in lists(b)) {
                b[0] = 0x88.toByte()
                val items = read(list)
                assertEquals(list.expected(before)[0], items.next())
                b[0] = 0x00
                assertEquals(list.expected(after)[0], items.next())
            }
        }
        overwritten({ it.iterator() }, 5000, 5001)
        overwritten({ it.listIterator() }, 5000, 5001)
        overwritten({ it.listIterator(2) }, 5000, 4999)
        overwritten({ it.subList(1, 4).iterator() }, 5001, 5000)
        overwritten({ it.subList(1, 4).listIterator(1) }, 5000, 4999)
        overwritten({ it.subList(1, 5).subList(1, 4).iterator() }, 5000, 4999)
    }

    @Test
    fun wrapsAroundTheSizeOfAnItem() {
        assertEquals(listOf<Short>(32767, -32768, 32767), ViewDeltas.shorts(bytes(0xFF, 0x7F, 0x01, 0xFF), 0, 4, short, true, byte, lsb))
        assertContentEquals(intArrayOf(65535, 0, 1), ViewDeltas.ints(bytes(0xFF, 0xFF, 0x01, 0x01), 0, 4, short, false, byte, lsb).toIntArray())
        assertContentEquals(intArrayOf(16, 18, 15), ViewDeltas.ints(bytes(0x00, 0x10, 0x02, 0xFD), 0, 4, short, true, byte, ByteOrder.MOST_SIGNIFICANT_FIRST).toIntArray())
    }

    @Test
    fun readsNoBytesAsAnEmptyListAndRejectsPartialSteps() {
        assertEquals(emptyList(), ViewDeltas.shorts(bytes(0x10), 1, 1, short, true, byte, lsb))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewDeltas.shorts(bytes(0x10), 0, 1, short, true, byte, lsb) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewDeltas.end(0, 3, short, short) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewDeltas.end(2, 1, short, byte) }
    }

    @Test
    fun writesTheFirstItemThenTheStepToEachNext() {
        val b = ByteArray(4)
        var pos = 0
        var previous = 0L
        listOf(16L, 18L, 15L).forEachIndexed { index, value ->
            pos += ViewDeltas.write(b, pos, b.size, index, previous, value, short, true, byte, lsb)
            previous = value
        }
        assertContentEquals(bytes(0x10, 0x00, 0x02, 0xFD), b)
        assertEquals(4, ViewDeltas.sizeOf(3, short, byte))
        assertFailsWith<IllegalArgumentException> { ViewDeltas.write(b, 2, b.size, 1, 0, 200, short, true, byte, lsb) }
    }
}
