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
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.annotations.ByteStuffed
import com.splendo.kaluga.bytes.annotations.ByteStuffedCobs
import com.splendo.kaluga.bytes.annotations.ByteStuffedSlip
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Terminal
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

// CSafe framing: 0xF1 start flag, 0xF2 stop flag, body byte-stuffed with the default 0xF3/0x03 scheme.
@Serializable
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
private data class CSafeFrame(@Size(Length.`8_BIT`) @Unsigned val command: Int, @Size(Length.`8_BIT`) @Unsigned val value: Int)

@Serializable
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
private data class CSafePacket(@Unsized val content: List<Byte>)

@Serializable
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
@Checksum(8, 0x07u, 0x00u)
private data class CSafeChecksummedFrame(@Size(Length.`8_BIT`) @Unsigned val command: Int, @Size(Length.`8_BIT`) @Unsigned val value: Int)

// Big-endian field VALUES with a sequentially-accumulated (excludeStructure) frame - the combination
// real big-endian frames use. Stuffing checks only the accumulation direction, so it is allowed here.
@Serializable
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
private data class BigEndianStuffedFrame(@Size(Length.`16_BIT`) @Unsigned val value: Int)

// A byte-stuffed string: the stuffing annotation is itself a 0x00-terminated marking (no @NullTerminated needed).
// The XOR scheme escapes 0x00 (here the UTF-16 high byte of each ASCII char) so it never collides with the terminator.
@Serializable
private data class StuffedStringHolder(
    @Encoded(Encoding.UTF_16)
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
    val name: String,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

// A byte-stuffed list: the stuffing annotation implies a 0x00-terminated collection (no @NullTerminated needed).
// Items may contain the 0x00 terminator value; stuffing keeps it unambiguous.
@Serializable
private data class StuffedListHolder(
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
    val values: List<Byte>,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

// The XOR scheme's own delimiter is the terminator (0x7C = '|'), which it escapes within the content.
@Serializable
private data class TerminalStringHolder(
    @Encoded(Encoding.ASCII)
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
    val name: String,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

@Serializable
private data class TerminalListHolder(
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
    val values: List<Byte>,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

// COBS eliminates 0x00, so it uses the default 0x00 terminator; content may contain 0x00.
@Serializable
private data class CobsListHolder(
    @ByteStuffedCobs
    val values: List<Byte>,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

// SLIP's delimiter is 0xC0, so the terminator defaults to 0xC0 (no @Terminal needed); 0xC0/0xDB in content are escaped.
@Serializable
private data class SlipListHolder(
    @ByteStuffedSlip
    val values: List<Byte>,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

// @Terminal without stuffing is a plain byte-terminated string; the content must not encode the terminator.
@Serializable
private data class PlainTerminatedString(
    @Encoded(Encoding.ASCII)
    @Terminal(0x0A)
    val line: String,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

@Serializable
private data class PlainTerminatedList(
    @Terminal(0x0A)
    val values: List<Byte>,
    @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
)

class BytesFormatStuffingTest : BytesFormatTestBase(BytesFormatBackend.Factory) {

    @Test
    fun encodeByteStuffing() {
        // A content byte in 0xF0..0xF3 is escaped as 0xF3 followed by its low two bits; the start (0xF1)
        // and stop (0xF2) flags frame the stuffed region untouched.
        validateRoundTrip(
            CSafeFrame(command = 0xF1, value = 0x05),
            CSafeFrame.serializer(),
            byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0x01, 0x05, 0xF2.toByte()),
        )

        // A body free of escaped bytes is framed as-is.
        validateRoundTrip(
            CSafeFrame(command = 0x10, value = 0x20),
            CSafeFrame.serializer(),
            byteArrayOf(0xF1.toByte(), 0x10, 0x20, 0xF2.toByte()),
        )

        // Each of the four escaped values, including the escape byte 0xF3 itself.
        validateRoundTrip(
            CSafePacket(listOf(0xF0.toByte(), 0xF1.toByte(), 0xF2.toByte(), 0xF3.toByte(), 0x0A)),
            CSafePacket.serializer(),
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
    fun byteStuffedFrameShorterThanItsPrefixAndPostfixIsTruncated() {
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> {
            BytesFormat.decodeFromByteArray(CSafeFrame.serializer(), byteArrayOf(0xF1.toByte()))
        }
    }

    @Test
    fun byteStuffingStuffsChecksum() {
        // The checksum is computed over the unstuffed body and then itself stuffed, so the frame still
        // round-trips and stays framed by the untouched start/stop flags.
        val frame = CSafeChecksummedFrame(command = 0x02, value = 0x03)
        val encoded = BytesFormat.encodeToByteArray(CSafeChecksummedFrame.serializer(), frame)
        assertEquals(frame, BytesFormat.decodeFromByteArray(CSafeChecksummedFrame.serializer(), encoded))
        assertEquals(0xF1.toByte(), encoded.first())
        assertEquals(0xF2.toByte(), encoded.last())
    }

    @Test
    fun byteStuffingNullTerminatedString() {
        // "AB" in UTF-16 LE is 41 00 42 00; the 0x00 bytes are escaped (7D, 00^20=20), then the raw 0x00
        // terminator, then the trailing field - proving the stuffed string is self-delimiting.
        validateRoundTrip(
            StuffedStringHolder(name = "AB", trailer = 0x09),
            StuffedStringHolder.serializer(),
            byteArrayOf(0x41, 0x7D, 0x20, 0x42, 0x7D, 0x20, 0x00, 0x09),
        )
    }

    @Test
    fun byteStuffingNullTerminatedList() {
        // Items [0x01, 0x00, 0x7D]: 0x00 -> 7D 20, 0x7D -> 7D 5D; then the raw 0x00 terminator and the trailer.
        validateRoundTrip(
            StuffedListHolder(values = listOf(0x01, 0x00, 0x7D), trailer = 0x09),
            StuffedListHolder.serializer(),
            byteArrayOf(0x01, 0x7D, 0x20, 0x7D, 0x5D, 0x00, 0x09),
        )
    }

    @Test
    fun byteStuffingCustomTerminator() {
        // String terminated by 0x7C ('|'): "A|B" -> the '|' is escaped (7D, 7C^20=5C), then the raw 0x7C terminator.
        validateRoundTrip(
            TerminalStringHolder(name = "A|B", trailer = 0x09),
            TerminalStringHolder.serializer(),
            byteArrayOf(0x41, 0x7D, 0x5C, 0x42, 0x7C, 0x09),
        )

        // List terminated by 0x7C: items [0x01, 0x7C, 0x7D] -> 01, (7C->7D 5C), (7D->7D 5D), then 0x7C, then trailer.
        validateRoundTrip(
            TerminalListHolder(values = listOf(0x01, 0x7C, 0x7D), trailer = 0x09),
            TerminalListHolder.serializer(),
            byteArrayOf(0x01, 0x7D, 0x5C, 0x7D, 0x5D, 0x7C, 0x09),
        )
    }

    @Test
    fun plainByteTerminatorWithoutStuffing() {
        // A newline-terminated ASCII field: "Hi" -> 48 69, then the raw 0x0A terminator, then the trailer.
        validateRoundTrip(
            PlainTerminatedString(line = "Hi", trailer = 0x09),
            PlainTerminatedString.serializer(),
            byteArrayOf(0x48, 0x69, 0x0A, 0x09),
        )

        // A 0x0A-terminated list of bytes (no item equals the terminator).
        validateRoundTrip(
            PlainTerminatedList(values = listOf(0x01, 0x02), trailer = 0x09),
            PlainTerminatedList.serializer(),
            byteArrayOf(0x01, 0x02, 0x0A, 0x09),
        )

        // Encoding fails if the content itself contains the terminator byte (no stuffing to protect it).
        assertFailsWith<IllegalArgumentException> {
            BytesFormat.encodeToByteArray(PlainTerminatedString.serializer(), PlainTerminatedString(line = "a\nb", trailer = 0x00))
        }
    }

    @Test
    fun cobsByteStuffing() {
        // Items [0x11, 0x00, 0x22] -> COBS [0x02, 0x11, 0x02, 0x22], then the raw 0x00 terminator, then the trailer.
        validateRoundTrip(
            CobsListHolder(values = listOf(0x11, 0x00, 0x22), trailer = 0x09),
            CobsListHolder.serializer(),
            byteArrayOf(0x02, 0x11, 0x02, 0x22, 0x00, 0x09),
        )
    }

    @Test
    fun slipByteStuffing() {
        // Items [0xC0, 0x41, 0xDB]: END -> DB DC, ESC -> DB DD; then the raw 0xC0 END terminator, then the trailer.
        validateRoundTrip(
            SlipListHolder(values = listOf(0xC0.toByte(), 0x41, 0xDB.toByte()), trailer = 0x09),
            SlipListHolder.serializer(),
            byteArrayOf(0xDB.toByte(), 0xDC.toByte(), 0x41, 0xDB.toByte(), 0xDD.toByte(), 0xC0.toByte(), 0x09),
        )
    }

    @Test
    fun byteStuffingWithBigEndianFieldValues() {
        // value 0xF012 encodes big-endian as [0xF0, 0x12]; 0xF0 is stuffed to 0xF3 0x00, framed by F1/F2.
        validateRoundTrip(
            BigEndianStuffedFrame(0xF012),
            BigEndianStuffedFrame.serializer(),
            byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0x00, 0x12, 0xF2.toByte()),
        )
    }

    private object CSafeStringWithEscapedTerminalFixtures {
        // CSafe with a @Terminal drawn from the block it escapes (0xF0..0xF2, excluding the 0xF3 escape byte).
        @Serializable
        data class CSafeStringHolder(
            @Encoded(Encoding.ASCII)
            @ByteStuffed
            @Terminal(0xF0.toByte())
            val name: String,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )
    }

    @Test
    fun cSafeStringWithEscapedTerminal() {
        // 0xF0 is within CSafe's escaped block and is not the escape byte, so it can terminate the content.
        validateRoundTrip(
            CSafeStringWithEscapedTerminalFixtures.CSafeStringHolder(name = "A", trailer = 0x09),
            CSafeStringWithEscapedTerminalFixtures.CSafeStringHolder.serializer(),
            byteArrayOf(0x41, 0xF0.toByte(), 0x09),
        )
    }

    private object SlipCustomDelimiterReplacesTerminatorFixtures {
        // SLIP's terminator is its `end` byte, so a different one is chosen through `end` rather than @Terminal.
        @Serializable
        data class SlipCustomEndHolder(
            @ByteStuffedSlip(end = 0x7E)
            val values: List<Byte>,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )
    }

    @Test
    fun slipCustomDelimiterReplacesTerminator() {
        // 0x7E -> DB DC (escaped end), 0x41 stays, then the raw 0x7E terminator.
        validateRoundTrip(
            SlipCustomDelimiterReplacesTerminatorFixtures.SlipCustomEndHolder(values = listOf(0x7E, 0x41), trailer = 0x09),
            SlipCustomDelimiterReplacesTerminatorFixtures.SlipCustomEndHolder.serializer(),
            byteArrayOf(0xDB.toByte(), 0xDC.toByte(), 0x41, 0x7E, 0x09),
        )
    }

    private object ValueClassByteStuffingFixtures {
        @Serializable
        data class TwoBytes(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Size(Length.`8_BIT`) @Unsigned val b: Int)

        // A value class carrying a stuffing annotation, attaching the wire modifier at the use site.
        @Serializable
        @JvmInline
        @ByteStuffedCobs
        value class StuffedValueClass(val value: TwoBytes)

        // A value class wrapping a struct with a prefix/postfix/checksum frame and no stuffing, as a control.
        @Serializable
        @JvmInline
        @Prefix([0x55])
        @Postfix([0xAA.toByte()])
        @Checksum(8, 0x07u, 0x00u)
        value class FramedStructValueClass(val value: TwoBytes)

        @Serializable
        @JvmInline
        @Prefix([0x55])
        @Postfix([0xAA.toByte()])
        @Checksum(8, 0x07u, 0x00u)
        @ByteStuffedCobs
        value class FramedStuffedValueClass(val value: TwoBytes)

        @Serializable
        data class StuffedValueClassField(val child: StuffedValueClass, @Size(Length.`8_BIT`) @Unsigned val trailer: Int)

        @Serializable
        data class FramedStuffedValueClassField(val child: FramedStuffedValueClass, @Size(Length.`8_BIT`) @Unsigned val trailer: Int)
    }

    @Test
    fun valueClassByteStuffing() {
        // Body [0x00, 0x11] -> COBS [0x01, 0x02, 0x11].
        validateRoundTrip(
            ValueClassByteStuffingFixtures.StuffedValueClass(ValueClassByteStuffingFixtures.TwoBytes(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.StuffedValueClass.serializer(),
            byteArrayOf(0x01, 0x02, 0x11),
        )
    }

    @Test
    fun valueClassByteStuffingAsField() {
        // The stuffed value class as a nested field: COBS body [0x01, 0x02, 0x11], its 0x00 terminator, then the trailer.
        validateRoundTrip(
            ValueClassByteStuffingFixtures.StuffedValueClassField(
                ValueClassByteStuffingFixtures.StuffedValueClass(ValueClassByteStuffingFixtures.TwoBytes(a = 0x00, b = 0x11)),
                trailer = 0x09,
            ),
            ValueClassByteStuffingFixtures.StuffedValueClassField.serializer(),
            byteArrayOf(0x01, 0x02, 0x11, 0x00, 0x09),
        )
    }

    @Test
    fun framedValueClassByteStuffingAsField() {
        // Body+checksum are COBS-stuffed inside the 0x55/0xAA frame, followed by the 0x00 terminator, then the parent's trailer.
        val encoded = validateRoundTrip(
            ValueClassByteStuffingFixtures.FramedStuffedValueClassField(
                ValueClassByteStuffingFixtures.FramedStuffedValueClass(ValueClassByteStuffingFixtures.TwoBytes(a = 0x00, b = 0x11)),
                trailer = 0x09,
            ),
            ValueClassByteStuffingFixtures.FramedStuffedValueClassField.serializer(),
        )
        assertEquals(0x55.toByte(), encoded.first())
        assertEquals(0x09.toByte(), encoded.last())
    }

    @Test
    fun framedStructValueClassControl() {
        validateRoundTrip(
            ValueClassByteStuffingFixtures.FramedStructValueClass(ValueClassByteStuffingFixtures.TwoBytes(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.FramedStructValueClass.serializer(),
        )
    }

    @Test
    fun framedValueClassByteStuffing() {
        // The checksum is computed over the unstuffed body, then body+checksum are COBS-stuffed inside the 0x55/0xAA frame.
        val encoded = validateRoundTrip(
            ValueClassByteStuffingFixtures.FramedStuffedValueClass(ValueClassByteStuffingFixtures.TwoBytes(a = 0x00, b = 0x11)),
            ValueClassByteStuffingFixtures.FramedStuffedValueClass.serializer(),
        )
        assertEquals(0x55.toByte(), encoded.first())
        assertEquals(0xAA.toByte(), encoded.last())
        // COBS eliminates 0x00 from the stuffed region; a dropped stuffing would leave the 0x00 body byte.
        assertTrue(encoded.drop(1).dropLast(1).none { it == 0x00.toByte() }, "stuffing dropped: ${encoded.toHexString(separator = " ")}")
    }

    private object NestedStuffedStructureFixtures {
        @Serializable
        data class StuffChild(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Size(Length.`8_BIT`) @Unsigned val b: Int)

        // The child's whole encoding is stuffed and bounded by the scheme's delimiter (0x7C), escaped within the stuffed bytes.
        @Serializable
        data class PlainParentStuffedChild(
            @Size(Length.`8_BIT`) @Unsigned val head: Int,
            @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C) val child: StuffChild,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )

        // CSafe has no delimiter, so without @Terminal the nested stuffed structure is unsized and must be the last field.
        @Serializable
        data class UnsizedStuffedChildParent(@Size(Length.`8_BIT`) @Unsigned val head: Int, @ByteStuffed val child: StuffChild)

        @Serializable
        @Checksum(8, 0x07u, 0x00u)
        data class ChecksummedChild(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Size(Length.`8_BIT`) @Unsigned val b: Int)

        @Serializable
        data class ChecksummedStuffedChildParent(
            @Size(Length.`8_BIT`) @Unsigned val head: Int,
            @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C) val child: ChecksummedChild,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )

        @Serializable
        @Prefix([0xAA.toByte()])
        @Postfix([0xBB.toByte()])
        data class FramedChild(@Size(Length.`8_BIT`) @Unsigned val a: Int, @Size(Length.`8_BIT`) @Unsigned val b: Int)

        @Serializable
        data class FramedStuffedChildParent(
            @Size(Length.`8_BIT`) @Unsigned val head: Int,
            @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C) val child: FramedChild,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )

        // A stuffed root frame containing a stuffed child: the child's bytes are stuffed once per layer.
        @Serializable
        @Prefix([0xF1.toByte()])
        @Postfix([0xF2.toByte()])
        @ByteStuffed
        data class DoubleStuffedRoot(@Size(Length.`8_BIT`) @Unsigned val head: Int, @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C) val child: StuffChild)

        @Serializable
        data class ListChild(@ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00) val values: List<Byte>, @Size(Length.`8_BIT`) @Unsigned val tag: Int)

        @Serializable
        data class StructOverList(
            @Size(Length.`8_BIT`) @Unsigned val head: Int,
            @ByteStuffedXor(escapeByte = 0x7E, delimiter = 0x7C) val child: ListChild,
            @Size(Length.`8_BIT`) @Unsigned val trailer: Int,
        )

        @Serializable
        data class Level3(@Size(Length.`8_BIT`) @Unsigned val v: Int)

        @Serializable
        data class Level2(@ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7A) val inner: Level3, @Size(Length.`8_BIT`) @Unsigned val t2: Int)

        @Serializable
        data class Level1(
            @Size(Length.`8_BIT`) @Unsigned val head: Int,
            @ByteStuffedXor(escapeByte = 0x7E, delimiter = 0x7C) val mid: Level2,
            @Size(Length.`8_BIT`) @Unsigned val t1: Int,
        )
    }

    @Test
    fun nestedStuffedStructureInPlainParent() {
        // child [0x7C, 0x05]: 0x7C -> 7D 5C, 0x05 stays; then the raw 0x7C terminator, framed by head and trailer.
        validateRoundTrip(
            NestedStuffedStructureFixtures.PlainParentStuffedChild(head = 0x01, child = NestedStuffedStructureFixtures.StuffChild(a = 0x7C, b = 0x05), trailer = 0x09),
            NestedStuffedStructureFixtures.PlainParentStuffedChild.serializer(),
            byteArrayOf(0x01, 0x7D, 0x5C, 0x05, 0x7C, 0x09),
        )
    }

    @Test
    fun unsizedNestedStuffedStructure() {
        // CSafe with no @Terminal: child [0xF1, 0x05] -> F3 01 05, no terminator; it is the last, unsized field.
        validateRoundTrip(
            NestedStuffedStructureFixtures.UnsizedStuffedChildParent(head = 0x01, child = NestedStuffedStructureFixtures.StuffChild(a = 0xF1, b = 0x05)),
            NestedStuffedStructureFixtures.UnsizedStuffedChildParent.serializer(),
            byteArrayOf(0x01, 0xF3.toByte(), 0x01, 0x05),
        )
    }

    @Test
    fun nestedStuffedStructureWithChecksum() {
        // The child's CRC is computed over its logical body, then stuffed with it.
        validateRoundTrip(
            NestedStuffedStructureFixtures.ChecksummedStuffedChildParent(head = 0x01, child = NestedStuffedStructureFixtures.ChecksummedChild(a = 0x7C, b = 0x03), trailer = 0x09),
            NestedStuffedStructureFixtures.ChecksummedStuffedChildParent.serializer(),
        )
    }

    @Test
    fun nestedStuffedStructureFramesPrefixAndPostfixUntouched() {
        // As on the root, the child's @Prefix (0xAA) and @Postfix (0xBB) frame the stuffed region untouched: only the
        // body [0x7C, 0x05] is stuffed (0x7C -> 7D 5C), then the raw 0x7C terminator, with 0xAA/0xBB outside it.
        validateRoundTrip(
            NestedStuffedStructureFixtures.FramedStuffedChildParent(head = 0x01, child = NestedStuffedStructureFixtures.FramedChild(a = 0x7C, b = 0x05), trailer = 0x09),
            NestedStuffedStructureFixtures.FramedStuffedChildParent.serializer(),
            byteArrayOf(0x01, 0xAA.toByte(), 0x7D, 0x5C, 0x05, 0x7C, 0xBB.toByte(), 0x09),
        )
    }

    @Test
    fun stuffedChildInsideStuffedRootDoubleStuffs() {
        val encoded = validateRoundTrip(
            NestedStuffedStructureFixtures.DoubleStuffedRoot(head = 0x10, child = NestedStuffedStructureFixtures.StuffChild(a = 0xF1, b = 0x7C)),
            NestedStuffedStructureFixtures.DoubleStuffedRoot.serializer(),
        )
        assertEquals(0xF1.toByte(), encoded.first())
        assertEquals(0xF2.toByte(), encoded.last())
    }

    @Test
    fun stuffedStructureContainingStuffedList() {
        validateRoundTrip(
            NestedStuffedStructureFixtures.StructOverList(
                head = 0x01,
                child = NestedStuffedStructureFixtures.ListChild(values = listOf(0x00, 0x7C, 0x11), tag = 0x22),
                trailer = 0x09,
            ),
            NestedStuffedStructureFixtures.StructOverList.serializer(),
        )
    }

    @Test
    fun threeLevelsOfStuffedStructures() {
        validateRoundTrip(
            NestedStuffedStructureFixtures.Level1(
                head = 0x01,
                mid = NestedStuffedStructureFixtures.Level2(inner = NestedStuffedStructureFixtures.Level3(v = 0x7A), t2 = 0x7C),
                t1 = 0x09,
            ),
            NestedStuffedStructureFixtures.Level1.serializer(),
        )
    }
}
