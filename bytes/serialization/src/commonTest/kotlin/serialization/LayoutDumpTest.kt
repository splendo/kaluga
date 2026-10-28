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
import com.splendo.kaluga.bytes.annotations.ByOrdinal
import com.splendo.kaluga.bytes.annotations.ByteStuffedXor
import com.splendo.kaluga.bytes.annotations.Checksum
import com.splendo.kaluga.bytes.annotations.FlagIndex
import com.splendo.kaluga.bytes.annotations.FlagWidth
import com.splendo.kaluga.bytes.annotations.ItemSize
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.LengthPrefix
import com.splendo.kaluga.bytes.annotations.NullIfEmpty
import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.annotations.PresentWhenAllSet
import com.splendo.kaluga.bytes.annotations.Range
import com.splendo.kaluga.bytes.annotations.Reserved
import com.splendo.kaluga.bytes.annotations.Scalar
import com.splendo.kaluga.bytes.annotations.SerializedByteValue
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.SizePolymorphic
import com.splendo.kaluga.bytes.annotations.Unsigned
import com.splendo.kaluga.bytes.annotations.ValueSize
import com.splendo.kaluga.bytes.layout.LayoutCompiler
import com.splendo.kaluga.bytes.layout.dump
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.EmptySerializersModule
import kotlin.test.Test
import kotlin.test.assertEquals

class LayoutDumpTest {

    @Serializable
    @SerialName("Measurement")
    data class Measurement(
        @FlagIndex(0) val active: Boolean,
        val note: String?,
        @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val count: Int,
        @Scalar(decimalExponent = 2) @Size(Length.`16_BIT`) @Unsigned val temperature: Double,
        @Range(min = -40.0, max = 85.5) @Size(Length.`8_BIT`) val ratio: Double,
        @Reserved(before = 1) @Size(Length.`24_BIT`) val serial: UInt,
        @PresentWhenAllSet(0) val extra: Short?,
    )

    @Serializable
    @ByOrdinal(offset = 1)
    enum class Mode { IDLE, RUNNING, STOPPED }

    @Serializable
    enum class Scale {
        @SerializedByteValue(0x0A)
        CELSIUS,

        @SerializedByteValue(0x0B)
        FAHRENHEIT,
    }

    @Serializable
    @SerialName("Modes")
    data class Modes(@FlagIndex(0) @FlagWidth(3) val packed: Mode, val scale: Scale, @FlagIndex(4) @FlagWidth(3) @Unsigned val level: Byte)

    @Serializable
    @SerialName("Command")
    sealed class Command {
        @Serializable
        @SerialName("Start")
        @SerializedByteValue(0x01)
        data class Start(val delay: Short) : Command()

        @Serializable
        @SerialName("Stop")
        @SerializedByteValue(0x02)
        data object Stop : Command()
    }

    @Serializable
    @SerialName("Payload")
    @SizePolymorphic
    sealed class Payload {
        @Serializable
        @SerialName("Fixed")
        data class Fixed(val value: Int) : Payload()

        @Serializable
        @SerialName("Tail")
        data class Tail(@LengthPrefix val text: String) : Payload()
    }

    @Serializable
    @SerialName("Frame")
    @Prefix([0x7E])
    @Postfix([0x7F])
    @Checksum(width = 16, polynomial = 0x1021u, init = 0xFFFFu)
    @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
    data class Frame(
        val command: Command,
        @ItemSize(Length.`16_BIT`) val readings: List<Int>,
        @NullIfEmpty @ValueSize(Length.`8_BIT`) val tags: Map<String, Int>,
        @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00) val label: String,
    )

    @Test
    fun measurement() = assertDump(
        Measurement.serializer(),
        """
        0 Measurement: struct order=LSB/LSB header=4 static=?
          0 active: bool order=LSB/LSB bits=0+1 bytes=0
          1 note: string(UTF_8 prefix8) order=LSB/LSB bits=1+1 nullable bytes=?
          2 count: natural(8,16,32 signed) order=LSB/LSB bits=2+2 bytes=?
          3 temperature: scalar(16 unsigned x1 e10=2 e2=0 offset=0) order=LSB/LSB bytes=2
          4 ratio: range(8 min=-40.0 max=85.5) order=LSB/LSB bytes=1
          5 serial: natural(24 unsigned) order=LSB/LSB reserved=1/0 bytes=4
          6 extra: natural(16 signed) order=LSB/LSB nullable presentWhen=[0] bytes=?
        """,
    )

    @Test
    fun modes() = assertDump(
        Modes.serializer(),
        """
        0 Modes: struct order=LSB/LSB header=7 static=3
          0 packed: enum order=LSB/LSB bits=0+3 bytes=1
            = value 0=01
            = value 1=02
            = value 2=03
            0 IDLE: struct order=LSB/LSB bytes=0
            1 RUNNING: struct order=LSB/LSB bytes=0
            2 STOPPED: struct order=LSB/LSB bytes=0
          1 scale: enum order=LSB/LSB bytes=1
            = value 0=0a
            = value 1=0b
            0 CELSIUS: struct order=LSB/LSB bytes=0
            1 FAHRENHEIT: struct order=LSB/LSB bytes=0
          2 level: natural(flags=3 unsigned) order=LSB/LSB bits=4+3 bytes=0
        """,
    )

    @Test
    fun sizePolymorphicPayload() = assertDump(
        Payload.serializer(),
        """
        0 Payload: sealed order=LSB/LSB static=?
          = discriminator Fixed=46 69 78 65 64
          = discriminator Tail=54 61 69 6c
          = size 4 -> Fixed
          = size * -> Tail
          0 type: string(UTF_8 prefix8) order=LSB/LSB bytes=?
          1 value: contextual order=LSB/LSB bytes=?
            0 Fixed: struct order=LSB/LSB bytes=4
              0 value: natural(32 signed) order=LSB/LSB bytes=4
            1 Tail: struct order=LSB/LSB bytes=?
              0 text: string(UTF_8 prefix8) order=LSB/LSB bytes=?
        """,
    )

    @Test
    fun frame() = assertDump(
        Frame.serializer(),
        """
        0 Frame: struct order=LSB/MSB prefix=7e postfix=7f checksum=crc16(poly=1021 init=ffff xorOut=0) header=1 static=?
          0 command: sealed order=LSB/MSB bytes=?
            = discriminator Start=01
            = discriminator Stop=02
            0 type: string(UTF_8 prefix8) order=LSB/MSB bytes=?
            1 value: contextual order=LSB/MSB bytes=2
              0 Start: struct order=LSB/MSB bytes=2
                0 delay: natural(16 signed) order=MSB/MSB bytes=2
              1 Stop: struct order=LSB/MSB bytes=0
          1 readings: list(count(8)) order=LSB/MSB bytes=?
            0 0: natural(16 signed) order=MSB/MSB bytes=2
          2 tags: map(count(8) nullIfEmpty) order=LSB/MSB bits=0+1 nullable bytes=?
            0 0: string(UTF_8 prefix8) order=LSB/MSB bytes=?
            1 1: natural(8 signed) order=MSB/MSB bytes=1
          3 label: string(UTF_8 stuffed(xor(escape=7d delimiter=00 key=20) terminator=00)) order=LSB/MSB stuffing=xor(escape=7d delimiter=00 key=20) terminator=00 bytes=?
        """,
    )

    @Test
    fun compilingTwiceDumpsIdentically() {
        val shape = SerialDescriptorShape(Frame.serializer().descriptor, EmptySerializersModule())
        assertEquals(LayoutCompiler.compile(shape).dump(), LayoutCompiler.compile(shape).dump())
    }

    private fun assertDump(serializer: KSerializer<*>, expected: String) {
        val actual = BinaryDescriptorRegistry.layout(serializer.descriptor, EmptySerializersModule()).dump()
        assertEquals(expected.trimIndent().trim(), actual.trim())
    }
}
