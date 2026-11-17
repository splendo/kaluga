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
            interface Other : Reading {
                val type: UByte
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
                val type: UShort
                @Unsized val rest: List<Byte>
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
            interface Unsupported : Command {
                val code: UByte
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
                    ReadingOtherData(0x99u, listOf(1, 2, 3)),
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
                if ((read.readings[3] as Reading.Other).type != 0x99.toUByte()) add("fallback " + read.readings[3])

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
                if (runCatching { ReadingFrame.encode(ReadingOtherData(0x20u, emptyList())) }.exceptionOrNull() !is IllegalArgumentException) add("fallback with a known type")
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
        assertTrue("= length prefix8" in dump && "= discriminator * -> test.Reading.Other" in dump && "reserved=0/1" in dump, dump)
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
                    val type: Short
                }
            }
            """ to "The first property of @Fallback 'test.Wide.B' holds the discriminator, so it must be a number of 1 byte(s)",
            """
            @BytesLayout
            sealed interface Flagged {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface A : Flagged {
                    val a: Byte
                }

                @BytesLayout
                @Fallback
                interface B : Flagged {
                    val type: UByte
                    @FlagIndex(0) val on: Boolean
                }
            }
            """ to "cannot have flag bits, a prefix, a postfix, a checksum or byte stuffing",
            """
            @BytesLayout
            @Fallback
            interface Alone {
                val type: UByte
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
