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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.bytes.ByteOrder
import com.splendo.kaluga.base.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private fun Float.as32Bit(): Float = Float.fromBits(toRawBits())

/** A plain inline wrapper, used to exercise inline-class fields. */
@Serializable
@JvmInline
private value class ProbeValueContainer<T>(val value: T)

// ── Probe (1): flexible-size Long selection truncates through Int before checking fit ──────────────────
@Serializable
private data class ProbeLongWideSelection8Bit(@Size(Length.`8_BIT`) @Size(Length.`64_BIT`) val v: Long)

@Serializable
private data class ProbeLongWideSelection16Bit(@Size(Length.`16_BIT`) @Size(Length.`64_BIT`) val v: Long)

// ── Probe (2): a flag-packed enum inside a @SizePolymorphic subtype ─────────────────────────────────────
@Serializable
private enum class ProbePackedMode { OFF, LOW, MED, HIGH }

@Serializable
@SizePolymorphic
private sealed interface ProbeFlagEnumSizeVariant

@Serializable
private data class ProbeFlagEnumPacked(@FlagIndex(0) val mode: ProbePackedMode, @Size(Length.`16_BIT`) val x: Short) : ProbeFlagEnumSizeVariant // header(1) + body(2) = 3 bytes

@Serializable
private data class ProbeFlagEnumWide(@Size(Length.`8_BIT`) val x: Byte, @Size(Length.`8_BIT`) val y: Byte) : ProbeFlagEnumSizeVariant // 2 bytes

@Serializable
private data class ProbeFlagEnumSizeHolder(val payload: ProbeFlagEnumSizeVariant)

// ── Probe (3): a Char field inside a @SizePolymorphic subtype ───────────────────────────────────────────
@Serializable
@SizePolymorphic
private sealed interface ProbeCharSizeVariant

@Serializable
private data class ProbeCharSizeSmall(val c: Char) : ProbeCharSizeVariant // 1 byte

@Serializable
private data class ProbeCharSizeLarge(val c: Char, @Size(Length.`8_BIT`) val x: Byte) : ProbeCharSizeVariant // 2 bytes

@Serializable
private data class ProbeCharSizeHolder(val payload: ProbeCharSizeVariant)

// ── Probe (4): @Reserved(after = n) on a value-class / unsigned property ────────────────────────────────
@Serializable
private data class ProbeReservedAfterValueClass(@Reserved(after = 2) val wrapped: ProbeValueContainer<Byte>, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
private data class ProbeReservedAfterUnsigned(@Reserved(after = 2) val v: UByte, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (5): an empty nested structure placed after body Booleans ─────────────────────────────────────
@Serializable
private data object ProbeEmptyMarker

@Serializable
private data class ProbeEmptyNestedAfterBooleans(val flagA: Boolean, val flagB: Boolean, val marker: ProbeEmptyMarker, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (6): a @FlagIndex-packed value inside a value class that also carries @Prefix/@Postfix ────────
@Serializable
private enum class ProbeMode3 { OFF, LOW, HIGH }

@Serializable
@JvmInline
@Prefix([0x77])
@Postfix([0x88.toByte()])
private value class ProbeFramedFlagValue(@FlagIndex(0) val mode: ProbeMode3)

@Serializable
private data class ProbeFramedFlagHolder(val wrapped: ProbeFramedFlagValue, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (7): Float/Double under @ByteOrder(MOST_SIGNIFICANT_FIRST, excludeStructure = true) ───────────
@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class ProbeMsbExcludeStructureFloats(val a: Float, val b: Double)

// ── Probe (8): a nested structure without @Prefix whose own header follows two parent body Booleans ────
@Serializable
private data class ProbeNestedFlagHeader(@FlagIndex(0) val flag: Boolean)

@Serializable
private data class ProbeNestedLeadingBoolean(val flag: Boolean)

@Serializable
private data class ProbeParentBooleansThenNestedFlagHeader(val boolA: Boolean, val boolB: Boolean, val nested: ProbeNestedFlagHeader, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
private data class ProbeParentBooleansThenNestedLeadingBoolean(
    val boolA: Boolean,
    val boolB: Boolean,
    val nested: ProbeNestedLeadingBoolean,
    @Size(Length.`8_BIT`) val trailer: Byte,
)

// ── Probe (9): @Reserved(after) on a nullable property ──────────────────────────────────────────────────
@Serializable
private data class ProbeReservedAfterNullable(@Reserved(after = 2) val v: Byte?, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (10): several @Size width-selection annotations on a value class carrying @Prefix/@Postfix ───
@Serializable
@JvmInline
@Prefix([0x66])
@Postfix([0x99.toByte()])
private value class ProbeFramedWideSizeValue(@Size(Length.`8_BIT`) @Size(Length.`64_BIT`) val v: Long)

@Serializable
private data class ProbeFramedWideSizeHolder(val wrapped: ProbeFramedWideSizeValue, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (11): a value class wrapping a nullable value, with and without framing ───────────────────────
@Serializable
@JvmInline
private value class ProbeNullableWrapped(val v: Byte?)

@Serializable
private data class ProbeNullableWrappedHolder(val wrapped: ProbeNullableWrapped, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
@JvmInline
@Prefix([0x55])
@Postfix([0xAA.toByte()])
private value class ProbeNullableWrappedFramed(val v: Byte?)

@Serializable
private data class ProbeNullableWrappedFramedHolder(val wrapped: ProbeNullableWrappedFramed, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (12): @LengthPrefix (byte-length) on a String of 128..255 bytes ───────────────────────────────
@Serializable
private data class ProbeLongByteLengthString(@LengthPrefix val s: String, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (13): @LengthPrefix (byte-length, default) on a List of 128..255 items ────────────────────────
@Serializable
private data class ProbeLongByteLengthListItem(val v: Byte)

@Serializable
private data class ProbeLongByteLengthList(@LengthPrefix val items: List<ProbeLongByteLengthListItem>, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (14): a nullable List WITHOUT @NullIfEmpty, empty vs null ─────────────────────────────────────
@Serializable
private data class ProbeNullableListItem(val v: Byte)

@Serializable
private data class ProbeNullableListNoNullIfEmpty(val items: List<ProbeNullableListItem>?, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (15): a List of a nullable @Serializable class, with a null and a non-null item ──────────────
@Serializable
private data class ProbePoint(val x: Byte, val y: Byte)

@Serializable
private data class ProbeNullableItemListHolder(val items: List<ProbePoint?>)

// ── Probe (16): item-level @Prefix/@Postfix/@Checksum on a list item class ──────────────────────────────
@Serializable
@Prefix([0x50])
@Postfix([0x60])
@Checksum(8, 0x07u, 0x00u)
private data class ProbeFramedChecksumItem(val v: Byte)

@Serializable
private data class ProbeFramedChecksumItemListHolder(val items: List<ProbeFramedChecksumItem>, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (17): @LengthPrefix(canOverflow = true) list count in a MOST_SIGNIFICANT_FIRST structure ──────
@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = false)
private data class ProbeMsbOverflowListIncluded(@LengthPrefix(canOverflow = true) val items: List<Byte>)

@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class ProbeMsbOverflowListExcluded(@LengthPrefix(canOverflow = true) val items: List<Byte>)

// ── Probe (18): a multi-byte (@Size(16_BIT)) list count in a MOST_SIGNIFICANT_FIRST structure ───────────
@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = false)
private data class ProbeMsbSizedListIncluded(@Size(Length.`16_BIT`) val items: List<Byte>)

@Serializable
@com.splendo.kaluga.bluetooth.serialization.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class ProbeMsbSizedListExcluded(@Size(Length.`16_BIT`) val items: List<Byte>)

// ── Probe (19): a nullable UByte widened past its natural 8-bit width loses its implied Unsigned() ─────
// annotation, because the registry's inline special-type switch matches on descriptor.serialName, and a
// nullable descriptor's serialName has a trailing "?" (BluetoothBinaryDescriptorRegistry.kt ~:297-311).
@Serializable
private data class ProbeNullableSizedUByteNonNull(@Size(Length.`16_BIT`) val b: UByte, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
private data class ProbeNullableSizedUByte(@Size(Length.`16_BIT`) val b: UByte?, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (20): the same loss for a nullable UInt widened past its natural 32-bit width ─────────────────
@Serializable
private data class ProbeNullableWideUIntNonNull(@Size(Length.`64_BIT`) val v: UInt, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
private data class ProbeNullableWideUInt(@Size(Length.`64_BIT`) val v: UInt?, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (21): the same loss for each item of a widened List<UInt?> ────────────────────────────────────
@Serializable
private data class ProbeNullableWideUIntListNonNull(@ItemSize(Length.`64_BIT`) val items: List<UInt>, @Size(Length.`8_BIT`) val trailer: Byte)

@Serializable
private data class ProbeNullableWideUIntList(@ItemSize(Length.`64_BIT`) val items: List<UInt?>, @Size(Length.`8_BIT`) val trailer: Byte)

// ── Probe (22): @Terminal is suspected ignored for a delimiter-based byte-stuffing scheme (Xor/Slip/Cobs)
// on a String or a List — the scheme's own canonical delimiter is used instead, contradicting the KDoc
// (Terminal, Flags.kt ~:388-393; ByteStuffedSlip, Flags.kt ~:425) which says @Terminal overrides it.
@Serializable
private data class ProbeTerminalOverridesXorStuffedString(
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0xC0.toByte(), additionalEscapedBytes = [0x7E])
    @Terminal(0x7E)
    val s: String,
    @Size(Length.`8_BIT`) val trailer: Byte,
)

@Serializable
private data class ProbeTerminalOverridesSlipStuffedString(
    @ByteStuffedSlip
    @Terminal(0x7E)
    val s: String,
    @Size(Length.`8_BIT`) val trailer: Byte,
)

@Serializable
private data class ProbeTerminalOverridesCobsStuffedString(
    @ByteStuffedCobs
    @Terminal(0x7E)
    val s: String,
    @Size(Length.`8_BIT`) val trailer: Byte,
)

@Serializable
private data class ProbeTerminalOverridesXorStuffedList(
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0xC0.toByte(), additionalEscapedBytes = [0x7E])
    @Terminal(0x7E)
    val items: List<Byte>,
    @Size(Length.`8_BIT`) val trailer: Byte,
)

// ── Probe (23): @ByteStuffed (CSafe) on a String without @Terminal — the KDoc (ByteStuffed, Flags.kt
// ~:364-383) says it defaults to a 0x00 terminator; suspected instead rejected with
// UnsupportedByteStuffing (BluetoothBinaryDescriptorRegistry.kt ~:702-714 requires @Terminal for any
// NonDelimiterByteStuffingScheme, which includes CSafe).
@Serializable
private data class ProbeCSafeStuffedStringWithoutTerminal(@ByteStuffed val s: String, @Size(Length.`8_BIT`) val trailer: Byte)

/**
 * Further probes for encode/decode mismatches in [BluetoothFormat], beyond [BluetoothFormatMismatchProbeTest].
 * Each is written as the behaviour the annotations and their KDoc describe: a failing probe marks a bug, and a
 * fix should turn it green. The passing ones are regression guards for suspicions that turned out fine.
 */
class BluetoothFormatKnownIssueProbeTest {

    private fun <T> decodeOrFail(serializer: KSerializer<T>, encoded: ByteArray): T = try {
        BluetoothFormat.decodeFromByteArray(serializer, encoded)
    } catch (e: Exception) {
        fail("decode threw ${e::class.simpleName}(\"${e.message}\") for encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun longEightAndSixtyFourBitSelectionRoundTrips() {
        val value = ProbeLongWideSelection8Bit(v = 1L shl 32)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeLongWideSelection8Bit.serializer(), value)
        val decoded = decodeOrFail(ProbeLongWideSelection8Bit.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun longSixteenAndSixtyFourBitSelectionRoundTrips() {
        val value = ProbeLongWideSelection16Bit(v = 1L shl 32)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeLongWideSelection16Bit.serializer(), value)
        val decoded = decodeOrFail(ProbeLongWideSelection16Bit.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun longNegativeOutOfIntRangeSelectionRoundTrips() {
        val value = ProbeLongWideSelection8Bit(v = -(1L shl 40))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeLongWideSelection8Bit.serializer(), value)
        val decoded = decodeOrFail(ProbeLongWideSelection8Bit.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun flagPackedEnumInSizePolymorphicSubtypeDispatchesBySize() {
        val value = ProbeFlagEnumSizeHolder(ProbeFlagEnumPacked(mode = ProbePackedMode.HIGH, x = 300))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeFlagEnumSizeHolder.serializer(), value)
        val decoded = decodeOrFail(ProbeFlagEnumSizeHolder.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun charFieldGivesSizePolymorphicSubtypesDistinctFixedSizes() {
        val value = ProbeCharSizeHolder(ProbeCharSizeSmall('A'))
        val encoded = try {
            BluetoothFormat.encodeToByteArray(ProbeCharSizeHolder.serializer(), value)
        } catch (e: Exception) {
            fail("encode threw ${e::class.simpleName}(\"${e.message}\")")
        }
        val decoded = decodeOrFail(ProbeCharSizeHolder.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun reservedAfterOnValueClassPropertyWritesPadding() {
        val value = ProbeReservedAfterValueClass(wrapped = ProbeValueContainer(0x11), trailer = 0x22)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeReservedAfterValueClass.serializer(), value)
        val expected = byteArrayOf(0x11, 0x00, 0x00, 0x22)
        assertTrue(
            encoded.contentEquals(expected),
            "expected ${expected.toHexString(separator = " ")} but got ${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeReservedAfterValueClass.serializer(), encoded)
        assertEquals(value, decoded)
    }

    @Test
    fun reservedAfterOnUnsignedPropertyWritesPadding() {
        val value = ProbeReservedAfterUnsigned(v = 0x11u, trailer = 0x22)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeReservedAfterUnsigned.serializer(), value)
        val expected = byteArrayOf(0x11, 0x00, 0x00, 0x22)
        assertTrue(
            encoded.contentEquals(expected),
            "expected ${expected.toHexString(separator = " ")} but got ${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeReservedAfterUnsigned.serializer(), encoded)
        assertEquals(value, decoded)
    }

    @Test
    fun emptyNestedStructureAfterBodyBooleansRoundTrips() {
        val value = ProbeEmptyNestedAfterBooleans(flagA = true, flagB = false, marker = ProbeEmptyMarker, trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeEmptyNestedAfterBooleans.serializer(), value)
        val decoded = decodeOrFail(ProbeEmptyNestedAfterBooleans.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun flagPackedValueInFramedValueClassRoundTrips() {
        val value = ProbeFramedFlagHolder(wrapped = ProbeFramedFlagValue(ProbeMode3.HIGH), trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeFramedFlagHolder.serializer(), value)
        val decoded = decodeOrFail(ProbeFramedFlagHolder.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun msbExcludeStructureFloatsUseBigEndianWireBytes() {
        val value = ProbeMsbExcludeStructureFloats(a = 1.5f, b = 2.5)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbExcludeStructureFloats.serializer(), value)
        val expected = byteArrayOf(0x3F, 0xC0.toByte(), 0x00, 0x00, 0x40, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        assertTrue(
            encoded.contentEquals(expected),
            "expected ${expected.toHexString(separator = " ")} but got ${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeMsbExcludeStructureFloats.serializer(), encoded)
        assertEquals(value.a.as32Bit(), decoded.a)
        assertEquals(value.b, decoded.b)
    }

    @Test
    fun nestedFlagHeaderAfterParentBooleansRoundTrips() {
        val value = ProbeParentBooleansThenNestedFlagHeader(boolA = true, boolB = false, nested = ProbeNestedFlagHeader(flag = true), trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeParentBooleansThenNestedFlagHeader.serializer(), value)
        val decoded = decodeOrFail(ProbeParentBooleansThenNestedFlagHeader.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun nestedLeadingBooleanAfterParentBooleansRoundTrips() {
        val value = ProbeParentBooleansThenNestedLeadingBoolean(boolA = true, boolB = false, nested = ProbeNestedLeadingBoolean(flag = true), trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeParentBooleansThenNestedLeadingBoolean.serializer(), value)
        val decoded = decodeOrFail(ProbeParentBooleansThenNestedLeadingBoolean.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun reservedAfterOnNullablePropertyWritesPaddingWhenPresent() {
        val value = ProbeReservedAfterNullable(v = 0x11, trailer = 0x22)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeReservedAfterNullable.serializer(), value)
        val decoded = decodeOrFail(ProbeReservedAfterNullable.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun reservedAfterOnNullablePropertySkipsPaddingWhenNull() {
        val value = ProbeReservedAfterNullable(v = null, trailer = 0x22)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeReservedAfterNullable.serializer(), value)
        val decoded = decodeOrFail(ProbeReservedAfterNullable.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun wideSizeSelectionOnFramedValueClassPropertyRoundTrips() {
        val small = ProbeFramedWideSizeHolder(wrapped = ProbeFramedWideSizeValue(5L), trailer = 0x2A)
        val smallEncoded = BluetoothFormat.encodeToByteArray(ProbeFramedWideSizeHolder.serializer(), small)
        val smallDecoded = decodeOrFail(ProbeFramedWideSizeHolder.serializer(), smallEncoded)
        assertEquals(small, smallDecoded, "encoded=${smallEncoded.toHexString(separator = " ")}")

        val large = ProbeFramedWideSizeHolder(wrapped = ProbeFramedWideSizeValue(1L shl 40), trailer = 0x2A)
        val largeEncoded = BluetoothFormat.encodeToByteArray(ProbeFramedWideSizeHolder.serializer(), large)
        val largeDecoded = decodeOrFail(ProbeFramedWideSizeHolder.serializer(), largeEncoded)
        assertEquals(large, largeDecoded, "encoded=${largeEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun valueClassWrappingNullableRoundTrips() {
        val present = ProbeNullableWrappedHolder(wrapped = ProbeNullableWrapped(0x11), trailer = 0x22)
        val presentEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWrappedHolder.serializer(), present)
        val presentDecoded = decodeOrFail(ProbeNullableWrappedHolder.serializer(), presentEncoded)
        assertEquals(present, presentDecoded, "encoded=${presentEncoded.toHexString(separator = " ")}")

        val absent = ProbeNullableWrappedHolder(wrapped = ProbeNullableWrapped(null), trailer = 0x22)
        val absentEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWrappedHolder.serializer(), absent)
        val absentDecoded = decodeOrFail(ProbeNullableWrappedHolder.serializer(), absentEncoded)
        assertEquals(absent, absentDecoded, "encoded=${absentEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun framedValueClassWrappingNullableRoundTrips() {
        val present = ProbeNullableWrappedFramedHolder(wrapped = ProbeNullableWrappedFramed(0x11), trailer = 0x22)
        val presentEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWrappedFramedHolder.serializer(), present)
        val presentDecoded = decodeOrFail(ProbeNullableWrappedFramedHolder.serializer(), presentEncoded)
        assertEquals(present, presentDecoded, "encoded=${presentEncoded.toHexString(separator = " ")}")

        val absent = ProbeNullableWrappedFramedHolder(wrapped = ProbeNullableWrappedFramed(null), trailer = 0x22)
        val absentEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWrappedFramedHolder.serializer(), absent)
        val absentDecoded = decodeOrFail(ProbeNullableWrappedFramedHolder.serializer(), absentEncoded)
        assertEquals(absent, absentDecoded, "encoded=${absentEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun longByteLengthPrefixedStringRoundTrips() {
        val value = ProbeLongByteLengthString(s = "x".repeat(200), trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeLongByteLengthString.serializer(), value)
        val decoded = decodeOrFail(ProbeLongByteLengthString.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun longByteLengthPrefixedListRoundTrips() {
        val value = ProbeLongByteLengthList(items = List(200) { ProbeLongByteLengthListItem(it.toByte()) }, trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeLongByteLengthList.serializer(), value)
        val decoded = decodeOrFail(ProbeLongByteLengthList.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun nullableListWithoutNullIfEmptyRoundTrips() {
        val nullValue = ProbeNullableListNoNullIfEmpty(items = null, trailer = 0x2A)
        val nullEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableListNoNullIfEmpty.serializer(), nullValue)
        val nullDecoded = decodeOrFail(ProbeNullableListNoNullIfEmpty.serializer(), nullEncoded)
        assertEquals(nullValue, nullDecoded, "null case, encoded=${nullEncoded.toHexString(separator = " ")}")

        val emptyValue = ProbeNullableListNoNullIfEmpty(items = emptyList(), trailer = 0x2A)
        val emptyEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableListNoNullIfEmpty.serializer(), emptyValue)
        val emptyDecoded = decodeOrFail(ProbeNullableListNoNullIfEmpty.serializer(), emptyEncoded)
        assertEquals(emptyValue, emptyDecoded, "empty-list case, encoded=${emptyEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun listOfNullableSerializableClassRoundTrips() {
        val value = ProbeNullableItemListHolder(items = listOf(ProbePoint(0x01, 0x02), null))
        val encoded = BluetoothFormat.encodeToByteArray(ProbeNullableItemListHolder.serializer(), value)
        val decoded = decodeOrFail(ProbeNullableItemListHolder.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun itemLevelPrefixPostfixChecksumOnListItemRoundTrips() {
        val value = ProbeFramedChecksumItemListHolder(
            items = listOf(ProbeFramedChecksumItem(0x11), ProbeFramedChecksumItem(0x22)),
            trailer = 0x2A,
        )
        val encoded = BluetoothFormat.encodeToByteArray(ProbeFramedChecksumItemListHolder.serializer(), value)
        val decoded = decodeOrFail(ProbeFramedChecksumItemListHolder.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureIncludedRoundTrips() {
        val value = ProbeMsbOverflowListIncluded(items = List(300) { it.toByte() })
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbOverflowListIncluded.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbOverflowListIncluded.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun msbOverflowLengthPrefixedListStructureExcludedRoundTrips() {
        val value = ProbeMsbOverflowListExcluded(items = List(300) { it.toByte() })
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbOverflowListExcluded.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbOverflowListExcluded.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun msbSixteenBitListCountStructureIncludedRoundTrips() {
        val value = ProbeMsbSizedListIncluded(items = listOf(1, 2, 3, 4).map { it.toByte() })
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbSizedListIncluded.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbSizedListIncluded.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun msbSixteenBitListCountStructureExcludedRoundTrips() {
        val value = ProbeMsbSizedListExcluded(items = listOf(1, 2, 3, 4).map { it.toByte() })
        val encoded = BluetoothFormat.encodeToByteArray(ProbeMsbSizedListExcluded.serializer(), value)
        val decoded = decodeOrFail(ProbeMsbSizedListExcluded.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun nullableSizedUByteMatchesNonNullWireBytes() {
        val nonNull = ProbeNullableSizedUByteNonNull(b = 200u, trailer = 0x2A)
        val nonNullEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableSizedUByteNonNull.serializer(), nonNull)

        val nullable = ProbeNullableSizedUByte(b = 200u, trailer = 0x2A)
        val nullableEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableSizedUByte.serializer(), nullable)

        val tail = nullableEncoded.copyOfRange(nullableEncoded.size - nonNullEncoded.size, nullableEncoded.size)
        assertTrue(
            tail.contentEquals(nonNullEncoded),
            "nullable UByte@16_BIT should encode the same wire bytes as the non-null declaration: " +
                "non-null=${nonNullEncoded.toHexString(separator = " ")} nullable=${nullableEncoded.toHexString(separator = " ")}",
        )

        val decoded = decodeOrFail(ProbeNullableSizedUByte.serializer(), nullableEncoded)
        assertEquals(nullable, decoded, "round trip: encoded=${nullableEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun nullableWideUIntMatchesNonNullWireBytes() {
        val nonNull = ProbeNullableWideUIntNonNull(v = 3_000_000_000u, trailer = 0x2A)
        val nonNullEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWideUIntNonNull.serializer(), nonNull)

        val nullable = ProbeNullableWideUInt(v = 3_000_000_000u, trailer = 0x2A)
        val nullableEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWideUInt.serializer(), nullable)

        val tail = nullableEncoded.copyOfRange(nullableEncoded.size - nonNullEncoded.size, nullableEncoded.size)
        assertTrue(
            tail.contentEquals(nonNullEncoded),
            "nullable UInt@64_BIT should encode the same wire bytes as the non-null declaration: " +
                "non-null=${nonNullEncoded.toHexString(separator = " ")} nullable=${nullableEncoded.toHexString(separator = " ")}",
        )

        val decoded = decodeOrFail(ProbeNullableWideUInt.serializer(), nullableEncoded)
        assertEquals(nullable, decoded, "round trip: encoded=${nullableEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun nullableWideUIntListItemMatchesNonNullWireBytes() {
        val nonNull = ProbeNullableWideUIntListNonNull(items = listOf(3_000_000_000u), trailer = 0x2A)
        val nonNullEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWideUIntListNonNull.serializer(), nonNull)

        val nullable = ProbeNullableWideUIntList(items = listOf(3_000_000_000u), trailer = 0x2A)
        val nullableEncoded = BluetoothFormat.encodeToByteArray(ProbeNullableWideUIntList.serializer(), nullable)

        val tail = nullableEncoded.copyOfRange(nullableEncoded.size - nonNullEncoded.size, nullableEncoded.size)
        assertTrue(
            tail.contentEquals(nonNullEncoded),
            "nullable List<UInt?>@64_BIT item should encode the same wire bytes as the non-null declaration: " +
                "non-null=${nonNullEncoded.toHexString(separator = " ")} nullable=${nullableEncoded.toHexString(separator = " ")}",
        )

        val decoded = decodeOrFail(ProbeNullableWideUIntList.serializer(), nullableEncoded)
        assertEquals(nullable, decoded, "round trip: encoded=${nullableEncoded.toHexString(separator = " ")}")
    }

    @Test
    fun terminalOverridesXorStuffedStringDelimiter() {
        val value = ProbeTerminalOverridesXorStuffedString(s = "hi", trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeTerminalOverridesXorStuffedString.serializer(), value)
        assertEquals(
            0x7E.toByte(),
            encoded[encoded.size - 2],
            "expected @Terminal(0x7E) to override the Xor scheme's own delimiter (0xC0) per its KDoc " +
                "(Terminal, Flags.kt ~388-393); encoded=${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeTerminalOverridesXorStuffedString.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun terminalOverridesSlipStuffedStringDelimiter() {
        val value = ProbeTerminalOverridesSlipStuffedString(s = "hi", trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeTerminalOverridesSlipStuffedString.serializer(), value)
        assertEquals(
            0x7E.toByte(),
            encoded[encoded.size - 2],
            "expected @Terminal(0x7E) to override the Slip scheme's own delimiter (end=0xC0) per its KDoc " +
                "(ByteStuffedSlip, Flags.kt ~425); encoded=${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeTerminalOverridesSlipStuffedString.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun terminalOverridesCobsStuffedStringDelimiter() {
        val value = ProbeTerminalOverridesCobsStuffedString(s = "hi", trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeTerminalOverridesCobsStuffedString.serializer(), value)
        assertEquals(
            0x7E.toByte(),
            encoded[encoded.size - 2],
            "expected @Terminal(0x7E) to override the Cobs scheme's own delimiter (0x00) per the Terminal KDoc " +
                "(Flags.kt ~388-393); encoded=${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeTerminalOverridesCobsStuffedString.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun terminalOverridesXorStuffedListDelimiter() {
        val value = ProbeTerminalOverridesXorStuffedList(items = listOf(0x01, 0x02), trailer = 0x2A)
        val encoded = BluetoothFormat.encodeToByteArray(ProbeTerminalOverridesXorStuffedList.serializer(), value)
        assertEquals(
            0x7E.toByte(),
            encoded[encoded.size - 2],
            "expected @Terminal(0x7E) to override the Xor scheme's own delimiter (0xC0) on a List per the " +
                "Terminal KDoc (Flags.kt ~388-393); encoded=${encoded.toHexString(separator = " ")}",
        )
        val decoded = decodeOrFail(ProbeTerminalOverridesXorStuffedList.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun csafeStuffedStringWithoutTerminalDefaultsToNullTerminator() {
        val value = ProbeCSafeStuffedStringWithoutTerminal(s = "hi", trailer = 0x2A)
        val encoded = try {
            BluetoothFormat.encodeToByteArray(ProbeCSafeStuffedStringWithoutTerminal.serializer(), value)
        } catch (e: Exception) {
            fail(
                "expected @ByteStuffed without @Terminal to default to a 0x00 terminator per its KDoc " +
                    "(ByteStuffed, Flags.kt ~364-383), but encode threw ${e::class.simpleName}(\"${e.message}\")",
            )
        }
        val decoded = decodeOrFail(ProbeCSafeStuffedStringWithoutTerminal.serializer(), encoded)
        assertEquals(value, decoded, "encoded=${encoded.toHexString(separator = " ")}")
    }
}
