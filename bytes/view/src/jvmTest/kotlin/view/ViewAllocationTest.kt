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
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.strings.StringEncodingSettings
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.decodeDouble
import com.splendo.kaluga.bytes.decodeFloat
import com.splendo.kaluga.bytes.decodeInt
import com.splendo.kaluga.bytes.integer.decodeInt24
import com.splendo.kaluga.bytes.integer.decodeInt40
import com.splendo.kaluga.bytes.integer.decodeInt48
import com.splendo.kaluga.bytes.decodeLong
import com.splendo.kaluga.bytes.medfloat.decodeMedFloat16
import com.splendo.kaluga.bytes.medfloat.decodeMedFloat32
import com.splendo.kaluga.bytes.decodeShort
import com.splendo.kaluga.bytes.integer.decodeUInt24
import com.splendo.kaluga.bytes.integer.decodeUInt40
import com.splendo.kaluga.bytes.integer.decodeUInt48
import com.splendo.kaluga.bytes.layout.ChecksumSpec
import kotlin.math.abs
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * What the view runtime allocates per call; run by the `jvmAllocationTest` task, see [Allocations].
 *
 * Fixed-size reads and writes allocate nothing. Reads that return a new object (a string, a list) allocate the same
 * for a value at the start of a small array as for the same value at the start of a large one, so nothing copies the
 * rest of the array.
 */
class ViewAllocationTest {

    private val small = ByteArray(64) { (it * 7).toByte() }
    private val large = ByteArray(64 * 1024) { (it * 7).toByte() }
    private val orders = ByteOrder.entries.toTypedArray()

    private var long = 0L
    private var double = 0.0
    private var boolean = false
    private var any: Any? = null

    // Every measurement is reported before a test fails on any of them.
    private val problems = mutableListOf<String>()

    @AfterTest
    fun reportProblems() {
        assertEquals(emptyList(), problems)
    }

    private fun assertNone(name: String, block: () -> Unit) {
        val perCall = Allocations.perCall(block = block)
        println("allocation $name: $perCall bytes per call")
        if (perCall >= 1.0) problems += "$name allocates $perCall bytes per call"
    }

    private fun assertBounded(name: String, bound: Int, small: () -> Unit, large: () -> Unit) {
        val inSmall = Allocations.perCall(block = small)
        val inLarge = Allocations.perCall(block = large)
        println("allocation $name: $inSmall bytes per call in a small array, $inLarge in a large one")
        if (abs(inSmall - inLarge) >= 1.0) problems += "$name allocates $inSmall bytes per call in a small array but $inLarge in a large one"
        if (inLarge > bound) problems += "$name allocates $inLarge bytes per call, more than $bound"
    }

    @Test
    fun decodesNumbersWithoutAllocating() {
        for (order in orders) {
            assertNone("decodeShort $order") { long = large.decodeShort(3, order).toLong() }
            assertNone("decodeInt24 $order") { long = large.decodeInt24(3, order).value.toLong() }
            assertNone("decodeUInt24 $order") { long = large.decodeUInt24(3, order).value.toLong() }
            assertNone("decodeInt $order") { long = large.decodeInt(3, order).toLong() }
            assertNone("decodeInt40 $order") { long = large.decodeInt40(3, order).value }
            assertNone("decodeUInt40 $order") { long = large.decodeUInt40(3, order).value.toLong() }
            assertNone("decodeInt48 $order") { long = large.decodeInt48(3, order).value }
            assertNone("decodeUInt48 $order") { long = large.decodeUInt48(3, order).value.toLong() }
            assertNone("decodeLong $order") { long = large.decodeLong(3, order) }
            assertNone("decodeFloat $order") { double = large.decodeFloat(3, order).toDouble() }
            assertNone("decodeDouble $order") { double = large.decodeDouble(3, order) }
        }
        assertNone("decodeMedFloat16") { double = large.decodeMedFloat16(3).value }
        assertNone("decodeMedFloat32") { double = large.decodeMedFloat32(3).value }
    }

    @Test
    fun readsAndWritesNumbersWithoutAllocating() {
        val bytes = ByteArray(16)
        for (length in Length.entries) {
            for (order in orders) {
                assertNone("readNatural $length $order") { long = ViewBytes.readNatural(large, 3, length, true, order, large.size) }
                assertNone("readNatural unsigned $length $order") { long = ViewBytes.readNatural(large, 3, length, false, order, large.size) }
                assertNone("writeNatural $length $order") { ViewBytes.writeNatural(bytes, 1, -5L, length, true, order, bytes.size) }
                assertNone("writeNatural unsigned $length $order") { ViewBytes.writeNatural(bytes, 1, 5L, length, false, order, bytes.size) }
            }
        }
        for (order in orders) {
            assertNone("readFloat $order") { double = ViewBytes.readFloat(large, 3, order, large.size).toDouble() }
            assertNone("writeFloat $order") { ViewBytes.writeFloat(bytes, 1, 1.5f, order, bytes.size) }
            assertNone("readDouble $order") { double = ViewBytes.readDouble(large, 3, order, large.size) }
            assertNone("writeDouble $order") { ViewBytes.writeDouble(bytes, 1, 1.5, order, bytes.size) }
            for (encoding in Encoding.entries) {
                assertNone("readChar $encoding $order") { long = ViewBytes.readChar(large, 3, encoding, order, large.size).code.toLong() }
                assertNone("writeChar $encoding $order") { ViewBytes.writeChar(bytes, 1, 'a', encoding, order, bytes.size) }
            }
        }
        assertNone("readMedFloat16") { double = ViewBytes.readMedFloat16(large, 3, large.size) }
        assertNone("writeMedFloat16") { ViewBytes.writeMedFloat16(bytes, 1, 12.5, bytes.size) }
        assertNone("readMedFloat32") { double = ViewBytes.readMedFloat32(large, 3, large.size) }
        assertNone("writeMedFloat32") { ViewBytes.writeMedFloat32(bytes, 1, 12.5, bytes.size) }
    }

    @Test
    fun readsAndWritesBitsWithoutAllocating() {
        val bytes = ByteArray(4)
        assertNone("bit") { boolean = ViewBytes.bit(large, 1, 11, large.size) }
        assertNone("bits") { long = ViewBytes.bits(large, 1, 3, 9, large.size) }
        assertNone("setBit") { ViewBytes.setBit(bytes, 1, 11, true, bytes.size) }
        assertNone("setBits") { ViewBytes.setBits(bytes, 1, 3, 9, 0x155, bytes.size) }
        assertNone("packedNatural") { long = ViewBytes.packedNatural(large, 1, 3, 5, true, large.size) }
        assertNone("setPackedNatural") { ViewBytes.setPackedNatural(bytes, 1, 3, 5, -3, true, bytes.size) }
        val table = arrayOf(byteArrayOf(0x01), byteArrayOf(large[3]))
        assertNone("matchIndex") { long = ViewBytes.matchIndex(large, 3, table, large.size).toLong() }
    }

    @Test
    fun checksChecksumsAndFramesWithoutAllocating() {
        val spec = ChecksumSpec(16, 0x1021u, 0xFFFFu, 0u, reflectIn = false, reflectOut = false)
        val bytes = large.copyOf(64)
        assertNone("update checksum") { ViewChecksum.update(bytes, 2, 40, spec, ByteOrder.LEAST_SIGNIFICANT_FIRST, bytes.size) }
        assertNone("check checksum") { ViewChecksum.check(bytes, 2, 40, spec, ByteOrder.LEAST_SIGNIFICANT_FIRST, bytes.size) }
        val prefix = bytes.copyOfRange(1, 3)
        assertNone("check prefix") { ViewChecksum.checkPrefix(bytes, 1, prefix, bytes.size) }
        assertNone("check postfix") { ViewChecksum.checkPostfix(bytes, 1, prefix, bytes.size) }
    }

    @Test
    fun sizesAndReadsStringsWithoutCopyingTheArray() {
        for (settings in listOf(
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ByteLength),
            StringEncodingSettings(StringEncodingSettings.LengthPrefix.ShortLength, Encoding.UTF_16),
            StringEncodingSettings(StringEncodingSettings.NullTerminated, Encoding.ASCII),
            StringEncodingSettings(StringEncodingSettings.FixedLength(8)),
        )) {
            val encoded = ViewStrings.sizeOf("hello", settings).let {
                ByteArray(it)
            }.also { ViewStrings.write(it, 0, it.size, "hello", settings, ByteOrder.LEAST_SIGNIFICANT_FIRST) }
            val inSmall = encoded.copyOf(64)
            val inLarge = encoded.copyOf(64 * 1024)
            assertNone("size ${settings.endMarking}") { long = ViewStrings.size(inLarge, 0, inLarge.size, settings).toLong() }
            // The String, its array, the StringBuilder with its array and the iterator over the bytes: measured at 128.
            assertBounded("read ${settings.endMarking}", 192, { any = ViewStrings.read(inSmall, 0, inSmall.size, settings) }, {
                any =
                    ViewStrings.read(inLarge, 0, inLarge.size, settings)
            })
        }
    }

    @Test
    fun readsCollectionsWithoutCopyingTheArray() {
        val end = ItemEnd { _, s, _ -> s + 2 }
        val read = ItemRead { b, s, lim -> ViewBytes.readNatural(b, s, Length.`16_BIT`, false, ByteOrder.LEAST_SIGNIFICANT_FIRST, lim) }
        val prefix = StringEncodingSettings.LengthPrefix.WithOverflow()
        assertNone("lengthPrefixCount") { long = ViewCollections.lengthPrefixCount(large, 0, large.size, prefix, ByteOrder.LEAST_SIGNIFICANT_FIRST).toLong() }
        assertNone("lengthPrefixSize") { long = ViewCollections.lengthPrefixSize(large, 0, large.size, prefix).toLong() }
        assertNone("end") { long = ViewCollections.end(large, 0, large.size, 4, null, end).toLong() }
        assertBounded("fixed stride list", 64, { any = FixedStrideList.of(small, 0, small.size, 4, 2, read) }, { any = FixedStrideList.of(large, 0, large.size, 4, 2, read) })
        assertBounded(
            "scanned list",
            128,
            { any = ViewCollections.list(small, 0, small.size, 4, null, end, read).size },
            { any = ViewCollections.list(large, 0, large.size, 4, null, end, read).size },
        )
        // A List boxes its items; what matters is that reading one copies nothing.
        val inSmall = FixedStrideList.of(small, 0, small.size, 4, 2, read)
        val inLarge = FixedStrideList.of(large, 0, large.size, 4, 2, read)
        assertBounded("list item", 24, { long = inSmall[2] }, { long = inLarge[2] })
    }
}
