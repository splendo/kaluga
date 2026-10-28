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

import kotlin.math.abs
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * What generated views allocate per call; run by the `androidHostAllocationTest` task, see [Allocations].
 *
 * Getters of primitives, flags, enums and nullable numbers and setters that rewrite in place allocate nothing. Getters
 * that return an object (a string, a list, a nested view) and `toData` allocate the same for a frame alone in its array
 * as for the same frame at the start of a large array, so nothing copies the rest of the array.
 */
class ViewAllocationTest {

    private val padding = ByteArray(64 * 1024)

    private var int = 0
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
        println("allocation $name: $inSmall bytes per call alone, $inLarge at the start of a large array")
        if (abs(inSmall - inLarge) >= 1.0) problems += "$name allocates $inSmall bytes per call alone but $inLarge at the start of a large array"
        if (inLarge > bound) problems += "$name allocates $inLarge bytes per call, more than $bound"
    }

    private fun assertBoxed(name: String, box: Int, block: () -> Unit) {
        val perCall = Allocations.perCall(block = block)
        println("allocation $name: $perCall bytes per call")
        if (abs(perCall - box) >= 1.0) problems += "$name allocates $perCall bytes per call, not the $box of its box"
    }

    private fun padded(bytes: ByteArray): ByteArray = bytes + padding

    // A String of a few characters, its array, the StringBuilder it is decoded into with its array, and the iterator
    // over the bytes: measured at 128, bounded with room for another array size or JVM.
    private val shortString = 192

    // An Integer with compressed class pointers; a nullable number is boxed as any Int? is.
    private val integerBox = 16

    @Test
    fun readsAndWritesAHeartRateMeasurementInPlace() {
        val view = EncodeHeartRateFixturesHeartRateView(300, true, false, 1000, listOf(RRInterval(0.5), RRInterval(1.0)))
        val large = EncodeHeartRateFixturesHeartRateView.wrapRange(padded(view.bytes), 0, view.bytes.size) as EncodeHeartRateFixturesHeartRateSlice
        assertNone("heart rate of several sizes") { int = view.heartRate }
        assertNone("flag") { boolean = view.contactDetected }
        assertBoxed("nullable number outside the Integer cache", integerBox) { int = view.energyExpended ?: 0 }
        assertNone("flag setter") { view.contactSupported = !boolean }
        assertBounded("list", 128, { any = view.rrIntervals }, { any = large.rrIntervals })
        assertBounded("list item", 160, { double = view.rrIntervals[1].seconds }, { double = large.rrIntervals[1].seconds })
        assertBounded("toData", 512, { any = view.toData() }, { any = large.toData() })
    }

    @Test
    fun readsAndWritesStaticFieldsInPlace() {
        val packet = ReservedBytesAfterFixturesPacketView(1, 2)
        assertNone("static number") { int = packet.b }
        assertNone("static number setter") { packet.b = int + 1 }
        val holder = EncodeEnumFieldFixturesHolderView(SomeEnum.B)
        assertNone("enum") { any = holder.value }
        assertNone("enum setter") { holder.value = SomeEnum.A }
        val reading = EncodeEnumInFlagsFixturesReadingView(SensorContact.Detected, 5)
        assertNone("flag enum") { any = reading.contact }
        assertNone("flag enum setter") { reading.contact = SensorContact.NotDetected }
    }

    @Test
    fun readsNumbersOfSeveralSizesInPlace() {
        val data = EncodeIntegerFixturesContainerData(
            defaultLength = 1,
            `8bit` = 2,
            `16bit` = 3,
            `24bit` = 4,
            `32bit` = 5,
            `64bit` = 6u,
            scalar = 7,
            medFloat16 = 8,
            medFloat32 = 9,
            variableSizeLowRange = 70_000,
            variableSizeHighRange = 70_000,
            unsigned = 10u,
            mostSignificant = 11,
            nullable = 12,
            nullableVariableSizing = 13,
            inlineValue = NumberValueContainer(14),
            nullableInlineValue = NumberValueContainer(15),
            inlineUnsignedValue = NumberValueContainer(16u),
            nullableInlineUnsignedValue = null,
        )
        val view = EncodeIntegerFixturesContainerView.encode(data)
        assertNone("scalar") { int = view.scalar }
        assertNone("MedFloat") { int = view.medFloat32 }
        assertNone("number of several sizes after others") { int = view.variableSizeHighRange }
        assertNone("nullable number of several sizes in the Integer cache") { int = view.nullableVariableSizing ?: 0 }
        assertNone("number after numbers of several sizes") { int = view.mostSignificant }
    }

    @Test
    fun updatesChecksumsInPlace() {
        val view = EncodeWithChecksumFixturesWithChecksumView(5, "hello")
        assertNone("checksummed setter") { view.index = int++ }
        assertNone("checksum check") { boolean = view.isChecksumValid() }
    }

    @Test
    fun readsStringsNestedLayoutsAndCollectionsWithoutCopyingTheArray() {
        val strings = EncodeStringFixturesContainerView.encode(
            EncodeStringFixturesContainerData("hello", "héllo", "hello", "hello", null, "hi", "hello", "hello", "rest"),
        )
        val largeStrings = EncodeStringFixturesContainerView.wrapRange(padded(strings.bytes), 0, strings.bytes.size)
        assertBounded("string", shortString, { any = strings.default }, { any = largeStrings.default })
        assertBounded("string after strings", shortString, { any = strings.lengthPrefix }, { any = largeStrings.lengthPrefix })

        val outer = ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterView(ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesBigEndianFieldsData(0x1234))
        val largeOuter = ByteOrderExcludeStructureNestedInsideLsbParentAllowedFixturesOuterView.wrapRange(padded(outer.bytes), 0, outer.bytes.size)
        assertBounded("nested layout", 48, { int = outer.inner.value }, { int = largeOuter.inner.value })

        val item = EncodeListFixturesItemData(1, 2, 3)
        val items = listOf(item, item, item)
        val lists = EncodeListFixturesListContainerView(items, items, items, items, items, items, items)
        val largeLists = EncodeListFixturesListContainerView.wrapRange(padded(lists.bytes), 0, lists.bytes.size)
        assertBounded("counted list item", 96, { int = lists.shortSized[2].valueB.toInt() }, { int = largeLists.shortSized[2].valueB.toInt() })
        assertBounded("terminated list item", 192, { int = lists.nullTerminated[2].valueB.toInt() }, { int = largeLists.nullTerminated[2].valueB.toInt() })

        val value: EncodeMapFixtures.Value = EncodeMapFixturesValueData(2)
        val entries = mapOf<EncodeMapFixtures.Key, EncodeMapFixtures.Value>(EncodeMapFixturesKeyData(1u) to value, EncodeMapFixturesKeyData(2u) to value)
        val maps = EncodeMapFixturesMapContainerView(entries, entries, entries, entries, entries, entries, entries)
        val largeMaps = EncodeMapFixturesMapContainerView.wrapRange(padded(maps.bytes), 0, maps.bytes.size)
        assertBounded("map", 512, { any = maps.lengthPrefix }, { any = largeMaps.lengthPrefix })
    }
}
