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
