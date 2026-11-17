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

package com.splendo.kaluga.bytes.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Type-length-value records: a sealed layout with `@LengthPrefix`, and `@Fallback` subtypes, in DATA and VIEW. */
@OptIn(ExperimentalCompilerApi::class)
class RecordsTest {

    private val layouts = layoutSource(
        """
        interface Timed {
            @FlagIndex(0)
            @Size(Length.`8_BIT`)
            @Size(Length.`16_BIT`)
            @Size(Length.`32_BIT`)
            val step: Int
        }

        @BytesLayout
        @LengthPrefix
        sealed interface Reading {
            @BytesLayout
            @SerializedByteValue(0x20)
            interface Temperature : Reading, Timed {
                val celsius: Short
            }

            @BytesLayout
            @SerializedByteValue(0x30)
            interface Note : Reading, Timed {
                @Unsized val text: String
            }

            @BytesLayout
            @SerializedByteValue(0x40)
            interface Mark : Reading

            @BytesLayout
            @Fallback
            interface Other : Reading, Timed {
                @Discriminator val type: UByte
                @Unsized val value: List<Byte>
            }
        }

        @BytesLayout
        interface Chunk {
            @FlagIndex(0) @FlagWidth(4) val rewinds: UByte
            @FlagIndex(4) val fill: Boolean
            val first: UInt
            @Unsized val readings: List<Reading>
        }

        @BytesLayout
        interface Followed {
            val reading: Reading
            val maybe: Reading?
            val after: Byte
        }

        @BytesLayout
        @LengthPrefix(lengthAsShort = true)
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        @Prefix([0x7E])
        @Checksum(8, 0x07u, 0x00u)
        sealed interface Message {
            @BytesLayout
            @SerializedByteValue(0x01, 0x00)
            interface Value : Message {
                val value: Short
            }

            @BytesLayout
            @Fallback
            interface Other : Message {
                @Discriminator val type: UShort
                @Unsized val rest: List<Byte>
            }
        }

        @BytesLayout
        @LengthPrefix
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        sealed interface Strict {
            @BytesLayout
            @SerializedByteValue(0x01, 0x02)
            interface A : Strict {
                val a: Byte
            }
        }

        @BytesLayout
        sealed interface Command {
            @BytesLayout
            @SerializedByteValue(0x01)
            interface Start : Command {
                val profile: Byte
            }

            @BytesLayout
            @Fallback
            @Prefix([0x55])
            interface Unsupported : Command {
                @Discriminator val code: UByte
                @Unsized val arguments: List<Byte>
            }
        }
        """,
    )

    @Test
    fun `reads and writes records like BytesFormat`() {
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val readings = listOf(
                    ReadingTemperatureData(1000, -5),
                    ReadingNoteData(0, "hi"),
                    ReadingMarkData,
                    ReadingOtherData(70000, 0x99u, listOf(1, 2, 3)),
                    ReadingNoteData(-70000, ""),
                )
                for (reading in readings) {
                    val expected = BytesFormat.encodeToByteArray(ReadingData.serializer(), reading)
                    val frame = ReadingFrame.encode(reading)
                    if (!frame.bytes.contentEquals(expected)) add("encode " + reading + ": " + frame.bytes.toList() + " != " + expected.toList())
                    if (ReadingFrame.wrap(expected).toData() != reading) add("decode " + reading + ": " + ReadingFrame(expected))
                    if (ReadingFrame.valueStart(expected) != 2 || ReadingFrame.valueEnd(expected) != expected.size || ReadingFrame.end(expected) != expected.size) {
                        add("bounds " + reading)
                    }
                    for (maybe in listOf(null, reading)) {
                        val data = FollowedData(reading, maybe, 7)
                        val bytes = BytesFormat.encodeToByteArray(FollowedData.serializer(), data)
                        val view = FollowedView.encode(data)
                        if (!view.bytes.contentEquals(bytes)) add("encode " + data + ": " + view.bytes.toList() + " != " + bytes.toList())
                        if (FollowedView.wrap(bytes).toData() != data || view.after != 7.toByte()) add("decode " + data)
                    }
                }
                val chunk = ChunkData(1u, true, 256u, readings)
                val bytes = BytesFormat.encodeToByteArray(ChunkData.serializer(), chunk)
                val view = ChunkView.encode(chunk)
                if (!view.bytes.contentEquals(bytes)) add("encode chunk: " + view.bytes.toList() + " != " + bytes.toList())
                val read = ChunkView.wrap(bytes)
                if (read.toData() != chunk) add("decode chunk " + read.toData())
                if (read.readings.size != readings.size || (read.readings[0] as Reading.Temperature).celsius != (-5).toShort()) add("list " + read.readings)
                val other = read.readings[3] as Reading.Other
                if (other.type != 0x99.toUByte() || other.step != 70000 || other.value != listOf<Byte>(1, 2, 3)) add("fallback " + other)
                if (other != ChunkView.wrap(bytes).readings[3] || other.hashCode() != ChunkView.wrap(bytes).readings[3].hashCode()) add("fallback equality " + other)
                if (other.toString() != "ReadingOtherSlice1(step=70000, type=153, value=[1, 2, 3])") add("fallback string " + other)

                // Records are walked by their length alone.
                var at = 5
                val types = mutableListOf<Int>()
                while (at < bytes.size) {
                    types += bytes[at].toInt() and 0xFF
                    at = ReadingFrame.end(bytes, at)
                }
                if (types != listOf(0x20, 0x30, 0x40, 0x99, 0x30)) add("walk " + types)

                // A later version appended bytes to a temperature; they are skipped.
                val longer = byteArrayOf(0, 0, 0, 0, 0, 0x20, 6, 0, 2, 0x10, 0, 0xAA.toByte(), 0xBB.toByte(), 0x40, 0)
                val decoded = BytesFormat.decodeFromByteArray(ChunkData.serializer(), longer)
                val viewed = ChunkView.wrap(longer)
                if (viewed.toData() != decoded || decoded.readings != listOf(ReadingTemperatureData(2, 16), ReadingMarkData)) add("trailing " + viewed.toData())
                if (runCatching { ChunkView.wrap(byteArrayOf(0, 0, 0, 0, 0, 0x20, 2, 0, 2)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("short value")
                if (runCatching { ChunkView.wrap(byteArrayOf(0, 0, 0, 0, 0, 0x40, 2, 0)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("short record")
                if (runCatching { ReadingFrame.encode(ReadingOtherData(0, 0x20u, emptyList())) }.exceptionOrNull() !is IllegalArgumentException) add("fallback with a known type")
                if (runCatching { CommandFrame.encode(CommandUnsupportedData(0x01u, emptyList())) }.exceptionOrNull() !is IllegalArgumentException) add("fallback with a known command")
                if (runCatching { ReadingFrame.encode(ReadingNoteData(0, "x".repeat(254))) }.exceptionOrNull() !is IllegalArgumentException) add("length overflow")

                for (message in listOf(MessageValueData(0x0102), MessageOtherData(0x0300u, listOf(4, 5)))) {
                    val expected = BytesFormat.encodeToByteArray(MessageData.serializer(), message)
                    val frame = MessageFrame.encode(message)
                    if (!frame.bytes.contentEquals(expected)) add("encode " + message + ": " + frame.bytes.toList() + " != " + expected.toList())
                    if (MessageFrame.wrap(expected).toData() != message) add("decode " + message)
                    if (MessageFrame.valueStart(expected) != 5 || MessageFrame.valueEnd(expected) != expected.size - 1 || MessageFrame.end(expected) != expected.size) {
                        add("bounds " + message)
                    }
                }

                // A record written from its type and its value bytes held apart, of a known type or not.
                val write: (ByteArray, Int, UByte, ByteArray, Int, Int) -> Int = ReadingFrame.Companion::writeRecord
                val size: (Int) -> Int = ReadingFrame.Companion::recordSize
                val temperature = BytesFormat.encodeToByteArray(ReadingData.serializer(), ReadingTemperatureData(2, 16))
                val value = temperature.copyOfRange(2, temperature.size)
                val target = ByteArray(10)
                if (write(target, 3, 0x20u, byteArrayOf(9) + value, 1, value.size + 1) != 3 + temperature.size || !target.copyOfRange(3, 3 + temperature.size).contentEquals(temperature)) {
                    add("writeRecord " + target.toList())
                }
                if (size(value.size) != temperature.size || !ReadingFrame.encodeRecord(0x20u, value).contentEquals(temperature)) add("encodeRecord " + ReadingFrame.encodeRecord(0x20u, value).toList())
                val unknown = ReadingFrame.encodeRecord(0x99u, value)
                if (BytesFormat.decodeFromByteArray(ReadingData.serializer(), unknown) != ReadingOtherData(2, 0x99u, listOf(0x10, 0))) add("unknown record " + unknown.toList())
                if (runCatching { ReadingFrame.encodeRecord(0x20u, ByteArray(256)) }.exceptionOrNull() !is IllegalArgumentException) add("record value too long")
                if (runCatching { ReadingFrame.writeRecord(ByteArray(5), 0, 0x20u, value) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("record target too short")
                val messageValue: (UShort, ByteArray, Int, Int) -> ByteArray = MessageFrame.Companion::encodeRecord
                val message = BytesFormat.encodeToByteArray(MessageData.serializer(), MessageValueData(0x0102))
                if (!messageValue(0x0100u, byteArrayOf(1, 2), 0, 2).contentEquals(message)) add("framed record " + messageValue(0x0100u, byteArrayOf(1, 2), 0, 2).toList())

                // The value of a record held apart from its type, viewed as a list of records gives it.
                val wrap: (UByte, ByteArray, Int, Int) -> Reading = ReadingFrame.Companion::wrapValue
                val held = byteArrayOf(9) + value + byteArrayOf(9)
                val known = wrap(0x20u, held, 1, held.size - 1)
                if ((known as Reading.Temperature).celsius != 16.toShort() || known.step != 2 || known != ReadingFrame.wrap(temperature).value) add("wrapValue " + known)
                val unknownValue = ReadingFrame.wrapValue(0x99u, held, 1, held.size - 1) as Reading.Other
                if (unknownValue.type != 0x99.toUByte() || unknownValue.step != 2 || unknownValue.value != listOf<Byte>(0x10, 0)) add("wrapValue unknown " + unknownValue)
                if ((unknownValue as ReadingOtherSlice1).toData() != ReadingOtherData(2, 0x99u, listOf(0x10, 0)) || unknownValue != ReadingFrame.wrap(unknown).value) {
                    add("wrapValue unknown data " + unknownValue)
                }
                if (unknownValue == ReadingFrame.wrapValue(0x98u, held, 1, held.size - 1)) add("wrapValue discriminator equality")
                held[3] = 0x11
                if (known.celsius != 0x11.toShort()) add("wrapValue in place " + known)
                if (MessageFrame.wrapValue(0x0100u, byteArrayOf(1, 2)) != MessageFrame.wrap(message).value) add("wrapValue framed " + MessageFrame.wrapValue(0x0100u, byteArrayOf(1, 2)))
                if ((MessageFrame.wrapValue(0x0300u, byteArrayOf(4)) as Message.Other).type != 0x0300.toUShort()) add("wrapValue framed fallback")
                if (runCatching { StrictFrame.wrapValue(0x05u, byteArrayOf(1)) }.exceptionOrNull() !is NoSuchElementException) add("wrapValue without a fallback")
                if (!StrictFrame.encodeRecord(0x05u, byteArrayOf(1)).contentEquals(byteArrayOf(0, 5, 1, 1))) add("encodeRecord without a fallback")
                if ((StrictFrame.wrapValue(0x0102u, byteArrayOf(7)) as Strict.A).a != 7.toByte()) add("wrapValue without a fallback " + StrictFrame.wrapValue(0x0102u, byteArrayOf(7)))

                for (command in listOf(CommandStartData(3), CommandUnsupportedData(7u, listOf(1, 2)))) {
                    val expected = BytesFormat.encodeToByteArray(CommandData.serializer(), command)
                    val frame = CommandFrame.encode(command)
                    if (!frame.bytes.contentEquals(expected)) add("encode " + command + ": " + frame.bytes.toList() + " != " + expected.toList())
                    if (CommandFrame.wrap(expected).toData() != command) add("decode " + command)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        assertEquals(emptyList(), compilation.generatedWarnings)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `counts the frame of a leaf in the length`() {
        val framed = layoutSource(
            """
            @BytesLayout
            @LengthPrefix
            sealed interface Framed {
                @BytesLayout
                @SerializedByteValue(0x10)
                @Postfix([0x55])
                interface Ended : Framed {
                    @Unsized val data: List<Byte>
                }

                @BytesLayout
                @SerializedByteValue(0x11)
                @Checksum(8, 0x07u, 0x00u)
                interface Summed : Framed {
                    val value: Byte
                }
            }

            @BytesLayout
            interface Followed {
                val record: Framed
                val after: Byte
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted

            fun check(): List<String> = buildList {
                val ended = byteArrayOf(0x10, 3, 1, 2, 0x55)
                if (FramedFrame.wrap(ended).toData() != FramedEndedData(listOf(1, 2))) add("ended " + FramedFrame(ended))
                val cut = byteArrayOf(0x11, 1, 7, 0x15)
                if (runCatching { FollowedView.wrap(cut) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("cut checksum")
                if ((FramedFrame.wrapValue(0x10u, byteArrayOf(1, 2, 0x55)) as Framed.Ended).data != listOf<Byte>(1, 2)) add("ended value held apart")
                if (runCatching { FramedFrame.wrapValue(0x11u, byteArrayOf()) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("cut checksum held apart")
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(framed, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems)
    }

    @Test
    fun `compiles records to the layout BytesFormat compiles`() {
        val parity = SourceFile.kotlin(
            "Parity.kt",
            """
            @file:OptIn(com.splendo.kaluga.bytes.layout.InternalBytesApi::class)

            package test

            import com.splendo.kaluga.bytes.serialization.layoutDumpOf

            fun check(): List<String> = GeneratedBytesLayouts.dataLayouts.mapNotNull { (serializer, dump) ->
                val runtime = layoutDumpOf(serializer.descriptor)
                if (runtime == dump) null else serializer.descriptor.serialName + ":\n" + dump + "--\n" + runtime
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, parity)

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val dump = compilation.generatedFile("ReadingData.kt")
        assertTrue("= length prefix8" in dump && "= discriminator * -> test.Reading.Other" in dump && "type: discriminator natural" in dump, dump)
        val problems = compilation.result.classLoader.loadClass("test.ParityKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems)
    }

    @Test
    fun `rejects records and fallbacks that cannot apply`() {
        val cases = listOf(
            """
            @BytesLayout
            @LengthPrefix
            interface Plain {
                val value: Byte
            }
            """ to "@LengthPrefix on 'test.Plain' only applies to a sealed class",
            """
            @BytesLayout
            @LengthPrefix
            @SizePolymorphic
            sealed interface BySize {
                @BytesLayout
                interface A : BySize {
                    val a: Byte
                }
            }
            """ to "needs subtypes told apart by a discriminator, not by @SizePolymorphic",
            """
            @BytesLayout
            sealed interface Wide {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Wide {
                    val a: Byte
                }

                @BytesLayout
                @Fallback
                interface B : Wide {
                    @Discriminator val type: Short
                }
            }
            """ to "The @Discriminator property 'type' of @Fallback 'test.Wide.B' holds the discriminator, so it must be a number of 1 byte(s)",
            """
            @BytesLayout
            sealed interface Unmarked {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Unmarked {
                    val a: Byte
                }

                @BytesLayout
                @Fallback
                interface B : Unmarked {
                    val type: UByte
                }
            }
            """ to "@Fallback 'test.Unmarked.B' needs exactly one @Discriminator property to hold the discriminator it was read for, but has none",
            """
            interface Typed {
                @Discriminator val type: UByte
            }

            @BytesLayout
            sealed interface Twice {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Twice {
                    val a: Byte
                }

                @BytesLayout
                @Fallback
                interface B : Twice, Typed {
                    @Discriminator val again: UByte
                }
            }
            """ to "@Fallback 'test.Twice.B' needs exactly one @Discriminator property to hold the discriminator it was read for, but has 'type', 'again'",
            """
            @BytesLayout
            sealed interface Known {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Known {
                    @Discriminator val a: UByte
                }
            }
            """ to "@Discriminator on a of test.Known.A only applies to a property of a @Fallback subtype",
            """
            @BytesLayout
            interface Plain {
                @Discriminator val a: UByte
            }
            """ to "@Discriminator on a of test.Plain only applies to a property of a @Fallback subtype",
            """
            @Serializable
            @JvmInline
            @Prefix([0x7A])
            value class Kind(val value: UByte)

            @BytesLayout
            sealed interface Framed {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Framed {
                    val a: Byte
                }

                @BytesLayout
                @Fallback
                interface B : Framed {
                    @Discriminator val kind: Kind
                    @Unsized val rest: List<Byte>
                }
            }
            """ to
                "The @Discriminator property 'kind' of @Fallback 'test.Framed.B' takes no bytes in it, so it cannot have a prefix, a postfix, a checksum or byte stuffing around it",
            """
            @BytesLayout
            @LengthPrefix
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            sealed interface Mirrored {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Mirrored {
                    val a: Byte
                }
            }
            """ to "@LengthPrefix on 'test.Mirrored' needs a frame accumulated LEAST_SIGNIFICANT_FIRST",
            """
            @BytesLayout
            @Fallback
            interface Alone {
                @Discriminator val type: UByte
            }
            """ to "@Fallback on test.Alone only applies to a subtype of a sealed @BytesLayout",
        )
        cases.forEach { (source, message) ->
            listOf("DATA", "VIEW").forEach { generate ->
                val compilation = BytesCompilation(layoutSource(source), generate = generate)
                assertTrue(compilation.kspErrors.any { message in it }, "$generate: " + compilation.result.messages)
            }
        }
    }
}
