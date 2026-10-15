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
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.layout.UnexpectedTerminator
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ViewCollectionsTest {

    // An item frame of one length-prefixed byte string, so items vary in size.
    private val end = ItemEnd { b, s, lim -> s + 1 + ViewBytes.u8(b, s, lim) }
    private val read = ItemRead { b, s, lim -> b.copyOfRange(s + 1, end.end(b, s, lim)).toList() }

    @Test
    fun scansItemsLazily() {
        val bytes = byteArrayOf(1, 0x0A, 2, 0x0B, 0x0C, 0, 0x7F)
        val list = ViewCollections.list(bytes, 0, bytes.size, 3, null, end, read)
        assertEquals(listOf<Byte>(0x0B, 0x0C), list[1])
        assertEquals(3, list.size)
        assertEquals(listOf(listOf<Byte>(0x0A), listOf<Byte>(0x0B, 0x0C), emptyList()), list)
        assertEquals(6, ViewCollections.end(bytes, 0, bytes.size, 3, null, end))
        assertFailsWith<IndexOutOfBoundsException> { list[3] }
    }

    @Test
    fun endsUnboundedItemsAtTheTerminatorOrTheLimit() {
        val bytes = byteArrayOf(1, 0x0A, 1, 0x0B, 0x00, 0x7F)
        assertEquals(2, ViewCollections.list(bytes, 0, bytes.size, ViewCollections.UNBOUNDED, 0x00, end, read).size)
        assertEquals(5, ViewCollections.end(bytes, 0, bytes.size, ViewCollections.UNBOUNDED, 0x00, end))
        assertEquals(2, ViewCollections.list(bytes, 0, 4, ViewCollections.UNBOUNDED, null, end, read).size)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { ViewCollections.end(bytes, 0, 3, ViewCollections.UNBOUNDED, null, end) }
    }

    @Test
    fun walksMirroredItemsDownwards() {
        // Items of one byte each, below a terminator-less run ending at 4 and reaching down to 1.
        val bytes = byteArrayOf(0x7F, 0x03, 0x02, 0x01)
        val end = ItemEnd { _, s, _ -> s - 1 }
        val read = ItemRead { b, s, _ -> b[s - 1] }
        assertEquals(listOf<Byte>(0x01, 0x02, 0x03), ViewCollections.list(bytes, 4, 1, ViewCollections.UNBOUNDED, null, end, read, mirrored = true))
        assertEquals(1, ViewCollections.endDown(bytes, 4, 1, ViewCollections.UNBOUNDED, null, end))
        assertEquals(listOf<Byte>(0x01, 0x02), FixedStrideList.of(bytes, 4, 0, 2, 1, read, mirrored = true))
        val terminated = byteArrayOf(0x00, 0x03, 0x02)
        assertEquals(0, ViewCollections.endDown(terminated, 3, 0, ViewCollections.UNBOUNDED, 0x00, end))
        val prefix = ByteArray(3)
        val overflow = StringEncodingSettings.LengthPrefix.WithOverflow()
        assertEquals(3, ViewCollections.writeLengthPrefixDown(prefix, 3, 300, overflow, ByteOrder.MOST_SIGNIFICANT_FIRST))
        assertEquals(300, ViewCollections.lengthPrefixCountDown(prefix, 3, 0, overflow, ByteOrder.MOST_SIGNIFICANT_FIRST))
        assertEquals(3, ViewCollections.lengthPrefixSizeDown(prefix, 3, overflow))
    }

    @Test
    fun readsMapsEagerly() {
        val bytes = byteArrayOf(0, 1, 0x0A, 0, 1, 0x0B)
        val map = ViewCollections.map(bytes, 0, bytes.size, 2, null, end, read, end, read)
        assertEquals(mapOf(emptyList<Byte>() to listOf<Byte>(0x0B)), map)
    }

    @Test
    fun readsAndWritesFixedStrideItemsInPlace() {
        val bytes = byteArrayOf(9, 1, 2, 3)
        val list = FixedStrideList.of(bytes, 1, bytes.size, 3, 1, { b, s, _ -> b[s] }, { b, s, _, value: Byte -> b[s] = value })
        assertEquals(listOf<Byte>(1, 2, 3), list)
        assertEquals(2.toByte(), list.set(1, 7))
        assertContentEquals(byteArrayOf(9, 1, 7, 3), bytes)
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { FixedStrideList.of(bytes, 1, bytes.size, 4, 1, { b, s, _ -> b[s] }) }
        assertFailsWith<UnsupportedOperationException> { FixedStrideList.of(bytes, 1, bytes.size, 3, 1, { b, s, _ -> b[s] }).set(0, 1) }
    }

    @Test
    fun readsAndWritesLengthPrefixes() {
        val bytes = ByteArray(3)
        val overflow = StringEncodingSettings.LengthPrefix.WithOverflow()
        assertEquals(3, ViewCollections.writeLengthPrefix(bytes, 0, 3, 300, overflow, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertEquals(300, ViewCollections.lengthPrefixCount(bytes, 0, 3, overflow, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertEquals(3, ViewCollections.lengthPrefixSize(bytes, 0, 3, overflow))
        // The sentinel comes first, where the decoder looks for it, also when the count is written big-endian.
        assertEquals(3, ViewCollections.writeLengthPrefix(bytes, 0, 3, 300, overflow, ByteOrder.MOST_SIGNIFICANT_FIRST))
        assertContentEquals(byteArrayOf(0xFF.toByte(), 0x01, 0x2C), bytes)
        assertEquals(300, ViewCollections.lengthPrefixCount(bytes, 0, 3, overflow, ByteOrder.MOST_SIGNIFICANT_FIRST))
        // A one byte count is unsigned, as BytesFormat reads it.
        bytes[0] = 0x80.toByte()
        assertEquals(128, ViewCollections.lengthPrefixCount(bytes, 0, 3, StringEncodingSettings.LengthPrefix.ByteLength, ByteOrder.LEAST_SIGNIFICANT_FIRST))
        assertFailsWith<IllegalArgumentException> {
            ViewCollections.writeLengthPrefix(bytes, 0, 3, 256, StringEncodingSettings.LengthPrefix.ByteLength, ByteOrder.LEAST_SIGNIFICANT_FIRST)
        }
    }

    @Test
    fun rejectsAnItemStartingWithTheTerminator() {
        assertFailsWith<UnexpectedTerminator> { ViewCollections.checkNotTerminator(byteArrayOf(0, 1), 0, 2, 0x00, 0) }
        ViewCollections.checkNotTerminator(byteArrayOf(1, 0), 0, 2, 0x00, 0)
    }
}
