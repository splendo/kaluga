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

/** Lists stored as their first item and the steps between items, `@DeltaCoded`, in DATA and VIEW. */
@OptIn(ExperimentalCompilerApi::class)
class DeltaCodedTest {

    private val layouts = layoutSource(
        """
        @BytesLayout
        interface Samples {
            @DeltaCoded(Length.`8_BIT`) val samples: List<Short>
        }

        @BytesLayout
        interface Shorts {
            val header: UByte
            @DeltaCoded(Length.`8_BIT`) @ItemByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST) val samples: ShortArray
        }

        @BytesLayout
        interface Ints {
            @DeltaCoded(Length.`8_BIT`) @ItemSize(Length.`16_BIT`) @ItemUnsigned val samples: List<Int>
        }

        @BytesLayout
        interface Wide {
            @DeltaCoded(Length.`16_BIT`) val samples: IntArray
        }

        @BytesLayout
        @LengthPrefix
        sealed interface Packet {
            @BytesLayout
            @SerializedByteValue(0x01)
            interface Waveform : Packet {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Short>
            }

            @BytesLayout
            @SerializedByteValue(0x02)
            interface Battery : Packet {
                val level: UByte
            }
        }

        @BytesLayout
        interface Chunk {
            val sequence: UShort
            @Unsized val packets: List<Packet>
        }
        """,
    )

    @Test
    fun `reads and writes delta-coded lists like BytesFormat`() {
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
            import com.splendo.kaluga.bytes.serialization.BytesFormat
            import com.splendo.kaluga.bytes.view.DeltaCodedIntList
            import com.splendo.kaluga.bytes.view.DeltaCodedShortList

            fun bytes(vararg values: Int) = ByteArray(values.size) { values[it].toByte() }

            fun check(): List<String> = buildList {
                val vectors = listOf(
                    bytes(0x10, 0x00, 0x02, 0xFD) to listOf<Short>(16, 18, 15),
                    bytes(0x88, 0x13, 0x01, 0xFE) to listOf<Short>(5000, 5001, 4999),
                    bytes(0xFF, 0x7F, 0x01, 0xFF) to listOf<Short>(32767, -32768, 32767),
                    bytes(0x05, 0x00) to listOf<Short>(5),
                )
                for ((bytes, samples) in vectors) {
                    val data = SamplesData(samples)
                    if (!BytesFormat.encodeToByteArray(SamplesData.serializer(), data).contentEquals(bytes)) add("BytesFormat " + samples)
                    if (!SamplesView.encode(data).bytes.contentEquals(bytes)) add("encode " + samples + ": " + SamplesView.encode(data).bytes.toList())
                    val view = SamplesView.wrap(bytes)
                    val list: DeltaCodedShortList = view.samples
                    if (list != samples || list.size != samples.size || list[samples.size - 1] != samples.last()) add("read " + list)
                    if (!list.toShortArray().contentEquals(samples.toShortArray())) add("array " + list)
                    if (!list.copyInto(ShortArray(samples.size + 1), 1).drop(1).equals(samples)) add("copyInto " + list)
                    if (view.toData() != data) add("decode " + view.toData())
                }
                if (SamplesView.wrap(ByteArray(0)).samples.isNotEmpty()) add("empty")

                val shorts = ShortsData(7u, shortArrayOf(16, 18, 15))
                val shortsBytes = BytesFormat.encodeToByteArray(ShortsData.serializer(), shorts)
                if (!shortsBytes.contentEquals(bytes(0x07, 0x00, 0x10, 0x02, 0xFD))) add("big-endian " + shortsBytes.toList())
                if (!ShortsView.encode(shorts).bytes.contentEquals(shortsBytes)) add("encode big-endian")
                if (!ShortsView.wrap(shortsBytes).samples.contentEquals(shorts.samples)) add("decode big-endian")

                val ints = IntsData(listOf(65535, 0, 2))
                val intsBytes = BytesFormat.encodeToByteArray(IntsData.serializer(), ints)
                if (!intsBytes.contentEquals(bytes(0xFF, 0xFF, 0x01, 0x02))) add("unsigned " + intsBytes.toList())
                if (!IntsView.encode(ints).bytes.contentEquals(intsBytes)) add("encode unsigned")
                val intList: DeltaCodedIntList = IntsView.wrap(intsBytes).samples
                if (intList != ints.samples) add("decode unsigned " + intList)

                val wide = WideData(intArrayOf(0, 1000, -1000))
                val wideBytes = BytesFormat.encodeToByteArray(WideData.serializer(), wide)
                if (!WideView.encode(wide).bytes.contentEquals(wideBytes) || !WideView.wrap(wideBytes).samples.contentEquals(wide.samples)) add("wide")

                val packets = listOf(PacketWaveformData(listOf(16, 18, 15)), PacketBatteryData(100u), PacketWaveformData(listOf(5000)), PacketWaveformData(emptyList()))
                val chunk = ChunkData(1u, packets)
                val chunkBytes = BytesFormat.encodeToByteArray(ChunkData.serializer(), chunk)
                val expected = bytes(0x01, 0x00, 0x01, 0x04, 0x10, 0x00, 0x02, 0xFD, 0x02, 0x01, 0x64, 0x01, 0x02, 0x88, 0x13, 0x01, 0x00)
                if (!chunkBytes.contentEquals(expected)) add("chunk " + chunkBytes.toList())
                if (!ChunkView.encode(chunk).bytes.contentEquals(chunkBytes)) add("encode chunk")
                val read = ChunkView.wrap(chunkBytes)
                if (read.toData() != chunk) add("decode chunk " + read.toData())
                if ((read.packets[2] as Packet.Waveform).samples != listOf<Short>(5000)) add("record " + read.packets[2])

                if (runCatching { SamplesView.wrap(bytes(0x10)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("partial first item")
                if (runCatching { WideView.wrap(bytes(0x10, 0x00, 0x02)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("partial step")
                if (runCatching { ChunkView.wrap(bytes(0x01, 0x00, 0x01, 0x01, 0x10)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("partial record")
                if (runCatching { SamplesView.encode(SamplesData(listOf(0, 128))) }.exceptionOrNull() !is IllegalArgumentException) add("step overflow")
                if (runCatching { IntsView.encode(IntsData(listOf(70000))) }.exceptionOrNull() !is IllegalArgumentException) add("item overflow")
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
    fun `compiles delta-coded lists to the layout BytesFormat compiles`() {
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
        val dump = compilation.generatedFile("SamplesData.kt")
        assertTrue("unmarked delta(step=8)" in dump, dump)
        val problems = compilation.result.classLoader.loadClass("test.ParityKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems)
    }

    @Test
    fun `rejects delta-coded lists that cannot apply`() {
        val cases = listOf(
            """
            @BytesLayout
            interface Followed {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Short>
                val after: Byte
            }
            """ to "'after' follows 'samples' in 'test.Followed', but 'samples' is @DeltaCoded, so it runs to the end of what holds it",
            """
            @BytesLayout
            interface Counted {
                @DeltaCoded(Length.`8_BIT`) @Count(3) val samples: List<Short>
            }
            """ to "@DeltaCoded runs to the end of what holds it, so it cannot be combined with @Count",
            """
            @BytesLayout
            interface Optional {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Short>?
            }
            """ to "@DeltaCoded 'samples' cannot be nullable",
            """
            @BytesLayout
            interface Single {
                @DeltaCoded(Length.`8_BIT`) val sample: Int
            }
            """ to "@DeltaCoded only applies to a list of integers, but 'sample' is kotlin.Int",
            """
            @BytesLayout
            interface Floats {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Float>
            }
            """ to "@DeltaCoded 'samples' needs items that are integers of one @ItemSize",
            """
            @BytesLayout
            interface WideSteps {
                @DeltaCoded(Length.`16_BIT`) val samples: ByteArray
            }
            """ to "@DeltaCoded 'samples' has steps of 2 bytes, more than its items of 1",
            """
            @BytesLayout
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            interface Mirrored {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Short>
            }
            """ to "@DeltaCoded 'samples' needs a list accumulated LEAST_SIGNIFICANT_FIRST",
        )
        cases.forEach { (source, message) ->
            listOf("DATA", "VIEW").forEach { generate ->
                val compilation = BytesCompilation(layoutSource(source), generate = generate)
                assertTrue(compilation.kspErrors.any { message in it }, "$generate, expecting \"$message\": " + compilation.result.messages)
            }
        }
    }

    @Test
    fun `reports items a view does not read from steps yet`() {
        val source = """
            @BytesLayout
            interface Longs {
                @DeltaCoded(Length.`8_BIT`) val samples: List<Long>
            }
            """
        val compilation = BytesCompilation(layoutSource(source), generate = "VIEW")
        assertTrue(compilation.kspErrors.any { "VIEW not yet supported for delta-coded items of type kotlin.collections.List<kotlin.Long>" in it }, compilation.result.messages)
        assertEquals(KotlinCompilation.ExitCode.OK, BytesCompilation(layoutSource(source)).result.exitCode)
    }
}
