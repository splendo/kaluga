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

import com.splendo.kaluga.bytes.ByteOrder
import com.splendo.kaluga.bytes.strings.Encoding
import com.splendo.kaluga.bytes.annotations.ByteStuffed
import com.splendo.kaluga.bytes.annotations.ByteStuffedCobs
import com.splendo.kaluga.bytes.annotations.ByteStuffedSlip
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.Encoded
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Terminal
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.Unsized
import kotlin.jvm.JvmInline
import kotlinx.serialization.Serializable

// CSafe framing: 0xF1 start flag, 0xF2 stop flag, body byte-stuffed with the default 0xF3/0x03 scheme.
@BytesLayout
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
interface CSafeFrame {
    @Size(Length.`8_BIT`)
    @Unsigned
    val command: Int

    @Size(Length.`8_BIT`)
    @Unsigned
    val value: Int
}

@BytesLayout
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
interface CSafePacket {
    @Unsized val content: List<Byte>
}

@BytesLayout
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
@Checksum(8, 0x07u, 0x00u)
interface CSafeChecksummedFrame {
    @Size(Length.`8_BIT`)
    @Unsigned
    val command: Int

    @Size(Length.`8_BIT`)
    @Unsigned
    val value: Int
}

// Big-endian field VALUES with a sequentially-accumulated (excludeStructure) frame - the combination
// real big-endian frames use. Stuffing checks only the accumulation direction, so it is allowed here.
@BytesLayout
@Prefix([0xF1.toByte()])
@Postfix([0xF2.toByte()])
@ByteStuffed
@com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
interface BigEndianStuffedFrame {
    @Size(Length.`16_BIT`)
    @Unsigned
    val value: Int
}

// A byte-stuffed string: the stuffing annotation is itself a 0x00-terminated marking (no @NullTerminated needed).
// The XOR scheme escapes 0x00 (here the UTF-16 high byte of each ASCII char) so it never collides with the terminator.
@BytesLayout
interface StuffedStringHolder {
    @Encoded(Encoding.UTF_16)
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
    val name: String

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

// A byte-stuffed list: the stuffing annotation implies a 0x00-terminated collection (no @NullTerminated needed).
// Items may contain the 0x00 terminator value; stuffing keeps it unambiguous.
@BytesLayout
interface StuffedListHolder {
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
    val values: List<Byte>

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

// The XOR scheme's own delimiter is the terminator (0x7C = '|'), which it escapes within the content.
@BytesLayout
interface TerminalStringHolder {
    @Encoded(Encoding.ASCII)
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
    val name: String

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

@BytesLayout
interface TerminalListHolder {
    @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
    val values: List<Byte>

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

// COBS eliminates 0x00, so it uses the default 0x00 terminator; content may contain 0x00.
@BytesLayout
interface CobsListHolder {
    @ByteStuffedCobs
    val values: List<Byte>

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

// SLIP's delimiter is 0xC0, so the terminator defaults to 0xC0 (no @Terminal needed); 0xC0/0xDB in content are escaped.
@BytesLayout
interface SlipListHolder {
    @ByteStuffedSlip
    val values: List<Byte>

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

// @Terminal without stuffing is a plain byte-terminated string; the content must not encode the terminator.
@BytesLayout
interface PlainTerminatedString {
    @Encoded(Encoding.ASCII)
    @Terminal(0x0A)
    val line: String

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

@BytesLayout
interface PlainTerminatedList {
    @Terminal(0x0A)
    val values: List<Byte>

    @Size(Length.`8_BIT`)
    @Unsigned
    val trailer: Int
}

object CSafeStringWithEscapedTerminalFixtures {
    // CSafe with a @Terminal drawn from the block it escapes (0xF0..0xF2, excluding the 0xF3 escape byte).
    @BytesLayout
    interface Holder {
        @Encoded(Encoding.ASCII)
        @ByteStuffed
        @Terminal(0xF0.toByte())
        val name: String

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }
}

object SlipCustomDelimiterReplacesTerminatorFixtures {
    // SLIP's terminator is its `end` byte, so a different one is chosen through `end` rather than @Terminal.
    @BytesLayout
    interface Holder {
        @ByteStuffedSlip(end = 0x7E)
        val values: List<Byte>

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }
}

object ValueClassByteStuffingFixtures {
    @BytesLayout
    interface TwoBytes {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }

    // A value class carrying a stuffing annotation, attaching the wire modifier at the use site.
    @Serializable
    @JvmInline
    @ByteStuffedCobs
    value class StuffedValueClass(@Serializable(with = ValueClassByteStuffingFixturesTwoBytesData.InterfaceSerializer::class) val value: TwoBytes)

    // A value class wrapping a layout with a prefix/postfix/checksum frame and no stuffing, as a control.
    @Serializable
    @JvmInline
    @Prefix([0x55])
    @Postfix([0xAA.toByte()])
    @Checksum(8, 0x07u, 0x00u)
    value class FramedStructValueClass(@Serializable(with = ValueClassByteStuffingFixturesTwoBytesData.InterfaceSerializer::class) val value: TwoBytes)

    @Serializable
    @JvmInline
    @Prefix([0x55])
    @Postfix([0xAA.toByte()])
    @Checksum(8, 0x07u, 0x00u)
    @ByteStuffedCobs
    value class FramedStuffedValueClass(@Serializable(with = ValueClassByteStuffingFixturesTwoBytesData.InterfaceSerializer::class) val value: TwoBytes)

    @BytesLayout
    interface StuffedValueClassField {
        val child: StuffedValueClass

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }

    @BytesLayout
    interface FramedStuffedValueClassField {
        val child: FramedStuffedValueClass

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }
}

object NestedStuffedStructureFixtures {
    @BytesLayout
    interface StuffChild {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }

    // The child's whole encoding is stuffed and bounded by the scheme's delimiter (0x7C), escaped within the stuffed bytes.
    @BytesLayout
    interface PlainParentStuffedChild {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
        val child: StuffChild

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }

    // CSafe has no delimiter, so without @Terminal the nested stuffed structure is unsized and must be the last field.
    @BytesLayout
    interface UnsizedStuffedChildParent {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffed
        val child: StuffChild
    }

    @BytesLayout
    @Checksum(8, 0x07u, 0x00u)
    interface ChecksummedChild {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }

    @BytesLayout
    interface ChecksummedStuffedChildParent {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
        val child: ChecksummedChild

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }

    @BytesLayout
    @Prefix([0xAA.toByte()])
    @Postfix([0xBB.toByte()])
    interface FramedChild {
        @Size(Length.`8_BIT`)
        @Unsigned
        val a: Int

        @Size(Length.`8_BIT`)
        @Unsigned
        val b: Int
    }

    @BytesLayout
    interface FramedStuffedChildParent {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
        val child: FramedChild

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }

    // A stuffed root frame containing a stuffed child: the child's bytes are stuffed once per layer.
    @BytesLayout
    @Prefix([0xF1.toByte()])
    @Postfix([0xF2.toByte()])
    @ByteStuffed
    interface DoubleStuffedRoot {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7C)
        val child: StuffChild
    }

    @BytesLayout
    interface ListChild {
        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
        val values: List<Byte>

        @Size(Length.`8_BIT`)
        @Unsigned
        val tag: Int
    }

    @BytesLayout
    interface StructOverList {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7E, delimiter = 0x7C)
        val child: ListChild

        @Size(Length.`8_BIT`)
        @Unsigned
        val trailer: Int
    }

    @BytesLayout
    interface Level3 {
        @Size(Length.`8_BIT`)
        @Unsigned
        val v: Int
    }

    @BytesLayout
    interface Level2 {
        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x7A)
        val inner: Level3

        @Size(Length.`8_BIT`)
        @Unsigned
        val t2: Int
    }

    @BytesLayout
    interface Level1 {
        @Size(Length.`8_BIT`)
        @Unsigned
        val head: Int

        @ByteStuffedXor(escapeByte = 0x7E, delimiter = 0x7C)
        val mid: Level2

        @Size(Length.`8_BIT`)
        @Unsigned
        val t1: Int
    }
}
