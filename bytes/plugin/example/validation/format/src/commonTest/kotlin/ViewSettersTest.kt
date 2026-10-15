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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** Writing through a view's setters must leave exactly the bytes `BytesFormat` encodes for the changed value. */
class ViewSettersTest {

    private fun <T> assertEncodes(serializer: KSerializer<T>, expected: T, bytes: ByteArray) {
        val encoded = BytesFormat.encodeToByteArray(serializer, expected)
        assertContentEquals(encoded, bytes, "Expected ${encoded.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
    }

    @Test
    fun setsFlagPackedNumbers() {
        val view = EncodeNumericInFlagsFixturesPackedView.encode(EncodeNumericInFlagsFixturesPackedData(nibble = 5, wide = 0x123))
        view.nibble = -2
        view.wide = 0x7FF
        assertEncodes(EncodeNumericInFlagsFixturesPackedData.serializer(), EncodeNumericInFlagsFixturesPackedData(nibble = -2, wide = 0x7FF), view.bytes)
        assertEquals(-2, view.nibble)
    }

    @Test
    fun rejectsAFlagPackedNumberThatDoesNotFit() {
        val view = EncodeNumericInFlagsFixturesNibbleView(0xA)
        assertFailsWith<IllegalArgumentException> { view.value = 16 }
        assertContentEquals(byteArrayOf(0x0A), view.bytes)
    }

    @Test
    fun setsNumbersBetweenReservedBytes() {
        val view = ReservedBytesBeforeAndAfterFixturesPacketView(a = 1, b = 2)
        view.b = 0xFFFF
        view.a = 0x1234
        assertEncodes(ReservedBytesBeforeAndAfterFixturesPacketData.serializer(), ReservedBytesBeforeAndAfterFixturesPacketData(a = 0x1234, b = 0xFFFF), view.bytes)
    }

    @Test
    fun setsEnumsAndScaledNumbers() {
        val status = StatusFrameView(Status.OFF)
        status.status = Status.ON
        assertEncodes(StatusFrameData.serializer(), StatusFrameData(Status.ON), status.bytes)

        val range = EncodeRangeFixturesWithHeaderView(header = 1, fraction = 0.0)
        range.fraction = 0.5
        assertEncodes(EncodeRangeFixturesWithHeaderData.serializer(), EncodeRangeFixturesWithHeaderData(header = 1, fraction = 0.5), range.bytes)
    }

    @Test
    fun replacesANestedLayout() {
        val outer = ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterView.encode(
            ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterData(ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesBigEndianFieldsData(1)),
        )
        outer.inner = ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesBigEndianFieldsView(0x0203)
        assertEncodes(
            ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterData.serializer(),
            ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterData(ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesBigEndianFieldsData(0x0203)),
            outer.bytes,
        )
        assertEquals(0x0203, outer.inner.value)
    }

    @Test
    fun comparesViewsByContent() {
        val data = ReservedBytesAfterFixturesPacketData(1, 2)
        val view = data.toReservedBytesAfterFixturesPacketView()
        val same = ReservedBytesAfterFixturesPacketView(1, 2)
        assertTrue(view.contentEquals(same))
        assertEquals(view.contentHashCode(), same.contentHashCode())
        // Value classes cannot override equals, so == compares the backing arrays.
        assertNotEquals(view, same)
        assertNotEquals<Any>(data, view)
        assertEquals(data, view.toData())

        val framed = byteArrayOf(0x7F) + view.bytes
        val slice = ReservedBytesAfterFixturesPacketView.wrapRange(framed, 1, framed.size)
        assertTrue(view.contentEquals(slice))
        assertEquals(slice, ReservedBytesAfterFixturesPacketView.wrapRange(framed.copyOf(), 1, framed.size))
        same.b = 3
        assertFalse(same.contentEquals(slice))
    }
}
