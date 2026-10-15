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

import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.serialization.serializer

/**
 * The stuffing and terminator tests every backend runs.
 */
abstract class StuffingSuite(backendFactory: ByteLayoutBackendFactory) : BytesFormatTestBase(backendFactory) {

    @Test
    fun plainByteTerminatorWithoutStuffing() {
        // A newline-terminated ASCII field: "Hi" -> 48 69, then the raw 0x0A terminator, then the trailer.
        validateRoundTrip(
            PlainTerminatedStringData(line = "Hi", trailer = 0x09),
            PlainTerminatedStringData.serializer(),
            byteArrayOf(0x48, 0x69, 0x0A, 0x09),
        )

        // A 0x0A-terminated list of bytes (no item equals the terminator).
        validateRoundTrip(
            PlainTerminatedListData(values = listOf(0x01, 0x02), trailer = 0x09),
            PlainTerminatedListData.serializer(),
            byteArrayOf(0x01, 0x02, 0x0A, 0x09),
        )

        // Encoding fails if the content itself contains the terminator byte (no stuffing to protect it).
        assertFailsWith<IllegalArgumentException> {
            createBackend().encode(PlainTerminatedStringData.serializer(), PlainTerminatedStringData(line = "a\nb", trailer = 0x00))
        }
    }

    @Test
    fun encodeByteStuffing() {
        // A content byte in 0xF0..0xF3 is escaped as 0xF3 followed by its low two bits; the start (0xF1)
        // and stop (0xF2) flags frame the stuffed region untouched.
        validateRoundTrip(
            CSafeFrameData(command = 0xF1, value = 0x05),
            CSafeFrameData.serializer(),
            byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0x01, 0x05, 0xF2.toByte()),
        )

        // A body free of escaped bytes is framed as-is.
        validateRoundTrip(
            CSafeFrameData(command = 0x10, value = 0x20),
            CSafeFrameData.serializer(),
            byteArrayOf(0xF1.toByte(), 0x10, 0x20, 0xF2.toByte()),
        )

        // Each of the four escaped values, including the escape byte 0xF3 itself.
        validateRoundTrip(
            CSafePacketData(listOf(0xF0.toByte(), 0xF1.toByte(), 0xF2.toByte(), 0xF3.toByte(), 0x0A)),
            CSafePacketData.serializer(),
            byteArrayOf(
                0xF1.toByte(),
                0xF3.toByte(), 0x00,
                0xF3.toByte(), 0x01,
                0xF3.toByte(), 0x02,
                0xF3.toByte(), 0x03,
                0x0A,
                0xF2.toByte(),
            ),
        )
    }

    @Test
    fun byteStuffingStuffsChecksum() {
        // The checksum is computed over the unstuffed body and then itself stuffed, so the frame still
        // round-trips and stays framed by the untouched start/stop flags.
        val frame = CSafeChecksummedFrameData(command = 0x02, value = 0x03)
        val encoded = createBackend().encode(CSafeChecksummedFrameData.serializer(), frame)
        assertEquals(frame, createBackend().decode(CSafeChecksummedFrameData.serializer(), encoded))
        assertEquals(0xF1.toByte(), encoded.first())
        assertEquals(0xF2.toByte(), encoded.last())
    }

    @Test
    fun byteStuffingNullTerminatedString() {
        // "AB" in UTF-16 LE is 41 00 42 00; the 0x00 bytes are escaped (7D, 00^20=20), then the raw 0x00
        // terminator, then the trailing field - proving the stuffed string is self-delimiting.
        validateRoundTrip(
            StuffedStringHolderData(name = "AB", trailer = 0x09),
            StuffedStringHolderData.serializer(),
            byteArrayOf(0x41, 0x7D, 0x20, 0x42, 0x7D, 0x20, 0x00, 0x09),
        )
    }

    @Test
    fun byteStuffingNullTerminatedList() {
        // Items [0x01, 0x00, 0x7D]: 0x00 -> 7D 20, 0x7D -> 7D 5D; then the raw 0x00 terminator and the trailer.
        validateRoundTrip(
            StuffedListHolderData(values = listOf(0x01, 0x00, 0x7D), trailer = 0x09),
            StuffedListHolderData.serializer(),
            byteArrayOf(0x01, 0x7D, 0x20, 0x7D, 0x5D, 0x00, 0x09),
        )
    }

    @Test
    fun byteStuffingCustomTerminator() {
        // String terminated by 0x7C ('|'): "A|B" -> the '|' is escaped (7D, 7C^20=5C), then the raw 0x7C terminator.
        validateRoundTrip(
            TerminalStringHolderData(name = "A|B", trailer = 0x09),
            TerminalStringHolderData.serializer(),
            byteArrayOf(0x41, 0x7D, 0x5C, 0x42, 0x7C, 0x09),
        )

        // List terminated by 0x7C: items [0x01, 0x7C, 0x7D] -> 01, (7C->7D 5C), (7D->7D 5D), then 0x7C, then trailer.
        validateRoundTrip(
            TerminalListHolderData(values = listOf(0x01, 0x7C, 0x7D), trailer = 0x09),
            TerminalListHolderData.serializer(),
            byteArrayOf(0x01, 0x7D, 0x5C, 0x7D, 0x5D, 0x7C, 0x09),
        )
    }

    @Test
    fun cobsByteStuffing() {
        // Items [0x11, 0x00, 0x22] -> COBS [0x02, 0x11, 0x02, 0x22], then the raw 0x00 terminator, then the trailer.
        validateRoundTrip(
            CobsListHolderData(values = listOf(0x11, 0x00, 0x22), trailer = 0x09),
            CobsListHolderData.serializer(),
            byteArrayOf(0x02, 0x11, 0x02, 0x22, 0x00, 0x09),
        )
    }

    @Test
    fun slipByteStuffing() {
        // Items [0xC0, 0x41, 0xDB]: END -> DB DC, ESC -> DB DD; then the raw 0xC0 END terminator, then the trailer.
        validateRoundTrip(
            SlipListHolderData(values = listOf(0xC0.toByte(), 0x41, 0xDB.toByte()), trailer = 0x09),
            SlipListHolderData.serializer(),
            byteArrayOf(0xDB.toByte(), 0xDC.toByte(), 0x41, 0xDB.toByte(), 0xDD.toByte(), 0xC0.toByte(), 0x09),
        )
    }

    @Test
    fun byteStuffingWithBigEndianFieldValues() {
        // value 0xF012 encodes big-endian as [0xF0, 0x12]; 0xF0 is stuffed to 0xF3 0x00, framed by F1/F2.
        validateRoundTrip(
            BigEndianStuffedFrameData(0xF012),
            BigEndianStuffedFrameData.serializer(),
            byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0x00, 0x12, 0xF2.toByte()),
        )
    }

    @Test
    fun cSafeStringWithEscapedTerminal() {
        // 0xF0 is within CSafe's escaped block and is not the escape byte, so it can terminate the content.
        validateRoundTrip(
            CSafeStringWithEscapedTerminalFixturesHolderData(name = "A", trailer = 0x09),
            CSafeStringWithEscapedTerminalFixturesHolderData.serializer(),
            byteArrayOf(0x41, 0xF0.toByte(), 0x09),
        )
    }

    @Test
    fun slipCustomDelimiterReplacesTerminator() {
        // 0x7E -> DB DC (escaped end), 0x41 stays, then the raw 0x7E terminator.
        validateRoundTrip(
            SlipCustomDelimiterReplacesTerminatorFixturesHolderData(values = listOf(0x7E, 0x41), trailer = 0x09),
            SlipCustomDelimiterReplacesTerminatorFixturesHolderData.serializer(),
            byteArrayOf(0xDB.toByte(), 0xDC.toByte(), 0x41, 0x7E, 0x09),
        )
    }
}

class BytesFormatStuffingTest : StuffingSuite(BytesFormatBackend.Factory) {
    // A root value class is no layout, and views do not stuff nested frames yet, so these run on the DATA classes only.

    @Test
    fun valueClassByteStuffing() {
        // Body [0x00, 0x11] -> COBS [0x01, 0x02, 0x11].
        validateRoundTrip(
            ValueClassByteStuffingFixtures.StuffedValueClass(ValueClassByteStuffingFixturesTwoBytesData(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.StuffedValueClass.serializer(),
            byteArrayOf(0x01, 0x02, 0x11),
        )
    }

    @Test
    fun framedStructValueClassControl() {
        validateRoundTrip(
            ValueClassByteStuffingFixtures.FramedStructValueClass(ValueClassByteStuffingFixturesTwoBytesData(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.FramedStructValueClass.serializer(),
        )
    }

    @Test
    fun framedValueClassByteStuffing() {
        // The checksum is computed over the unstuffed body, then body+checksum are COBS-stuffed inside the 0x55/0xAA frame.
        val encoded = validateRoundTrip(
            ValueClassByteStuffingFixtures.FramedStuffedValueClass(ValueClassByteStuffingFixturesTwoBytesData(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.FramedStuffedValueClass.serializer(),
        )
        assertEquals(0x55.toByte(), encoded.first())
        assertEquals(0xAA.toByte(), encoded.last())
        // COBS eliminates 0x00 from the stuffed region; a dropped stuffing would leave the 0x00 body byte.
        assertTrue(encoded.drop(1).dropLast(1).none { it == 0x00.toByte() }, "stuffing dropped: ${encoded.toHexString(separator = " ")}")
    }

    @Test
    fun stuffedChildInsideStuffedRootDoubleStuffs() {
        val encoded = validateRoundTrip(
            NestedStuffedStructureFixturesDoubleStuffedRootData(head = 0x10, child = NestedStuffedStructureFixturesStuffChildData(a = 0xF1, b = 0x7C)),
            NestedStuffedStructureFixturesDoubleStuffedRootData.serializer(),
        )
        assertEquals(0xF1.toByte(), encoded.first())
        assertEquals(0xF2.toByte(), encoded.last())
    }

    @Test
    fun valueClassByteStuffingAsField() {
        // The stuffed value class as a nested field: COBS body [0x01, 0x02, 0x11], its 0x00 terminator, then the trailer.
        validateRoundTrip(
            ValueClassByteStuffingFixturesStuffedValueClassFieldData(
                ValueClassByteStuffingFixtures.StuffedValueClass(ValueClassByteStuffingFixturesTwoBytesData(a = 0x00, b = 0x11)),
                trailer = 0x09,
            ),
            ValueClassByteStuffingFixturesStuffedValueClassFieldData.serializer(),
            byteArrayOf(0x01, 0x02, 0x11, 0x00, 0x09),
        )
    }

    @Test
    fun nestedStuffedStructureFramesPrefixAndPostfixUntouched() {
        // As on the root, the child's @Prefix (0xAA) and @Postfix (0xBB) frame the stuffed region untouched: only the
        // body [0x7C, 0x05] is stuffed (0x7C -> 7D 5C), then the raw 0x7C terminator, with 0xAA/0xBB outside it.
        validateRoundTrip(
            NestedStuffedStructureFixturesFramedStuffedChildParentData(head = 0x01, child = NestedStuffedStructureFixturesFramedChildData(a = 0x7C, b = 0x05), trailer = 0x09),
            NestedStuffedStructureFixturesFramedStuffedChildParentData.serializer(),
            byteArrayOf(0x01, 0xAA.toByte(), 0x7D, 0x5C, 0x05, 0x7C, 0xBB.toByte(), 0x09),
        )
    }

    @Test
    fun nestedStuffedStructureInPlainParent() {
        // child [0x7C, 0x05]: 0x7C -> 7D 5C, 0x05 stays; then the raw 0x7C terminator, framed by head and trailer.
        validateRoundTrip(
            NestedStuffedStructureFixturesPlainParentStuffedChildData(head = 0x01, child = NestedStuffedStructureFixturesStuffChildData(a = 0x7C, b = 0x05), trailer = 0x09),
            NestedStuffedStructureFixturesPlainParentStuffedChildData.serializer(),
            byteArrayOf(0x01, 0x7D, 0x5C, 0x05, 0x7C, 0x09),
        )
    }

    @Test
    fun framedValueClassByteStuffingAsField() {
        // Body+checksum are COBS-stuffed inside the 0x55/0xAA frame, followed by the 0x00 terminator, then the parent's trailer.
        val encoded = validateRoundTrip(
            ValueClassByteStuffingFixturesFramedStuffedValueClassFieldData(
                ValueClassByteStuffingFixtures.FramedStuffedValueClass(ValueClassByteStuffingFixturesTwoBytesData(a = 0x00, b = 0x11)),
                trailer = 0x09,
            ),
            ValueClassByteStuffingFixturesFramedStuffedValueClassFieldData.serializer(),
        )
        assertEquals(0x55.toByte(), encoded.first())
        assertEquals(0x09.toByte(), encoded.last())
    }

    @Test
    fun threeLevelsOfStuffedStructures() {
        validateRoundTrip(
            NestedStuffedStructureFixturesLevel1Data(
                head = 0x01,
                mid = NestedStuffedStructureFixturesLevel2Data(inner = NestedStuffedStructureFixturesLevel3Data(v = 0x7A), t2 = 0x7C),
                t1 = 0x09,
            ),
            NestedStuffedStructureFixturesLevel1Data.serializer(),
        )
    }

    @Test
    fun stuffedStructureContainingStuffedList() {
        validateRoundTrip(
            NestedStuffedStructureFixturesStructOverListData(
                head = 0x01,
                child = NestedStuffedStructureFixturesListChildData(values = listOf(0x00, 0x7C, 0x11), tag = 0x22),
                trailer = 0x09,
            ),
            NestedStuffedStructureFixturesStructOverListData.serializer(),
        )
    }

    @Test
    fun nestedStuffedStructureWithChecksum() {
        // The child's CRC is computed over its logical body, then stuffed with it.
        validateRoundTrip(
            NestedStuffedStructureFixturesChecksummedStuffedChildParentData(
                head = 0x01,
                child = NestedStuffedStructureFixturesChecksummedChildData(a = 0x7C, b = 0x03),
                trailer = 0x09,
            ),
            NestedStuffedStructureFixturesChecksummedStuffedChildParentData.serializer(),
        )
    }

    @Test
    fun unsizedNestedStuffedStructure() {
        // CSafe with no @Terminal: child [0xF1, 0x05] -> F3 01 05, no terminator; it is the last, unsized field.
        validateRoundTrip(
            NestedStuffedStructureFixturesUnsizedStuffedChildParentData(head = 0x01, child = NestedStuffedStructureFixturesStuffChildData(a = 0xF1, b = 0x05)),
            NestedStuffedStructureFixturesUnsizedStuffedChildParentData.serializer(),
            byteArrayOf(0x01, 0xF3.toByte(), 0x01, 0x05),
        )
    }
}

class ViewStuffingTest : StuffingSuite(ViewBackend.Factory)
