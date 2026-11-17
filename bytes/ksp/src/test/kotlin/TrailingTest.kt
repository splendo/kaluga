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
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** `@Trailing` properties, an optional tail of a layout that the bytes of a value reach or not, in DATA and VIEW. */
@OptIn(ExperimentalCompilerApi::class)
class TrailingTest {

    private val layouts = layoutSource(
        """
        @BytesLayout
        interface Status {
            @FlagIndex(0) val charging: Boolean
            @FlagIndex(1) @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val level: Int
            val mode: Byte?
            val voltage: UShort
            @Trailing val serviceVersion: UShort?
            @Trailing @FlagIndex(4) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) val uptime: Int?
        }

        @BytesLayout
        @LengthPrefix
        sealed interface Info {
            @BytesLayout
            @SerializedByteValue(0x01)
            interface Device : Info {
                val model: UShort
                @Trailing val serial: ULong?
                @Trailing val word: UShort?
            }

            @BytesLayout
            @SerializedByteValue(0x02)
            interface Battery : Info {
                val percent: UByte
            }

            @BytesLayout
            @Fallback
            @Postfix([0x7F])
            interface Other : Info {
                val first: Byte
                @Trailing val second: Byte?
                @Discriminator val type: UByte
            }
        }

        @BytesLayout
        interface Infos {
            val count: Byte
            @Unsized val infos: List<Info>
        }

        @BytesLayout
        interface Span {
            val low: Short
            val high: Short
        }

        @BytesLayout
        interface Limits {
            val id: Byte
            @Trailing val span: Span?
            @Trailing @Count(2) val points: List<Short>?
            @Trailing val label: String?
        }

        @BytesLayout
        interface Holder {
            val id: Byte
            val limits: Limits
        }

        @BytesLayout
        @Checksum(8, 0x07u, 0x00u)
        @Postfix([0x7F])
        interface Framed {
            val first: Byte
            @Trailing val second: Short?
        }
        """,
    )

    @Test
    fun `reads and writes a tail like BytesFormat`() {
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted
            import com.splendo.kaluga.bytes.layout.TrailingAfterAbsent
            import com.splendo.kaluga.bytes.serialization.BytesFormat
            import kotlinx.serialization.KSerializer

            fun check(): List<String> = buildList {
                fun <T> compare(name: String, serializer: KSerializer<T>, value: T, encode: (T) -> ByteArray, decode: (ByteArray) -> T) {
                    val expected = BytesFormat.encodeToByteArray(serializer, value)
                    val encoded = encode(value)
                    if (!encoded.contentEquals(expected)) add("encode " + name + " " + value + ": " + encoded.toList() + " != " + expected.toList())
                    val decoded = decode(expected)
                    if (decoded != value || BytesFormat.decodeFromByteArray(serializer, expected) != value) add("decode " + name + " " + value + ": " + decoded)
                }
                val status = StatusData(true, 300, null, 0x0102u, 0x0304u, 5)
                for (value in listOf(status, status.copy(uptime = 70000), status.copy(uptime = null), status.copy(serviceVersion = null, uptime = null), status.copy(mode = 7))) {
                    compare("status", StatusData.serializer(), value, { StatusView.encode(it).bytes }, { StatusView.wrap(it).toData() })
                }
                val full = BytesFormat.encodeToByteArray(StatusData.serializer(), status)
                // A view created unchecked, as a Bluetooth client creates one, reads nothing past the end of a shorter value.
                val tailless = StatusView(full.copyOf(5))
                if (tailless.serviceVersion != null || tailless.uptime != null || tailless.voltage != 0x0102.toUShort() || tailless.level != 300) add("tailless " + tailless)
                if (StatusView(full.copyOf(7)).serviceVersion != 0x0304.toUShort() || StatusView(full.copyOf(7)).uptime != null) add("middle " + StatusView(full.copyOf(7)))
                for (size in listOf(4, 6, 8)) {
                    if (runCatching { StatusView.wrap(full.copyOf(size)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("short " + size)
                }
                if (runCatching { StatusView(full.copyOf(6)).serviceVersion }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("inside a trailing field")
                val gap = status.copy(serviceVersion = null)
                val viewGap = runCatching { StatusView.encode(gap) }.exceptionOrNull()
                val formatGap = runCatching { BytesFormat.encodeToByteArray(StatusData.serializer(), gap) }.exceptionOrNull()
                if (viewGap !is TrailingAfterAbsent || viewGap.message != formatGap?.message) add("gap " + viewGap + " " + formatGap)
                if (runCatching { StatusView(true, 1, null, 2u, null, 3) }.exceptionOrNull() !is TrailingAfterAbsent) add("gap invoke")

                val infos = listOf(InfoDeviceData(7u, null, null), InfoBatteryData(50u), InfoDeviceData(0x0102u, 0x1122334455667788u, 0x0A0Bu), InfoDeviceData(1u, 2u, null), InfoOtherData(5, 6, 0x77u), InfoOtherData(5, null, 0x78u))
                for (info in infos) compare("info", InfoData.serializer(), info, { InfoFrame.encode(it as Info).bytes }, { InfoFrame.wrap(it).toData() })
                compare("infos", InfosData.serializer(), InfosData(3, infos), { InfosView.encode(it).bytes }, { InfosView.wrap(it).toData() })
                val read = InfosView.wrap(BytesFormat.encodeToByteArray(InfosData.serializer(), InfosData(3, infos)))
                if ((read.infos[0] as Info.Device).serial != null || (read.infos[2] as Info.Device).word != 0x0A0B.toUShort() || (read.infos[5] as Info.Other).second != null) add("records " + read)

                val limits = listOf(LimitsData(1, SpanData(-1, 1), listOf(2, 3), "ab"), LimitsData(1, SpanData(-1, 1), listOf(2, 3), null), LimitsData(1, SpanData(4, 5), null, null), LimitsData(1, null, null, null))
                for (value in limits) {
                    compare("limits", LimitsData.serializer(), value, { LimitsView.encode(it).bytes }, { LimitsView.wrap(it).toData() })
                    compare("holder", HolderData.serializer(), HolderData(9, value), { HolderView.encode(it).bytes }, { HolderView.wrap(it).toData() })
                }
                // A nested layout the value ends inside of reads nothing past it.
                if (runCatching { LimitsView(byteArrayOf(1, 2, 0)).span?.high }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("span cut short")
                if (runCatching { HolderView.wrap(byteArrayOf(9, 1, 2, 0)) }.exceptionOrNull() !is ByteArrayEndedBeforeSerializationCompleted) add("holder cut short")

                for (value in listOf(FramedData(1, 0x0203), FramedData(1, null))) {
                    compare("framed", FramedData.serializer(), value, { FramedView.encode(it).bytes }, { FramedView.wrap(it).toData() })
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
    fun `reads a trailing property only when the bytes reach it`() {
        val compilation = BytesCompilation(layouts, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val view = compilation.generatedFile("StatusView.kt")
        assertContains(view, "override val serviceVersion: UShort?")
        assertContains(view, "if (!(s + endOfMode(b, s, lim) / 8 + 2 < lim)) return null")
        assertContains(view, "if (!(s + endOfServiceVersion(b, s, lim) / 8 < lim)) return null")
        assertContains(view, "if (uptime != null && serviceVersion == null) throw TrailingAfterAbsent(\"uptime\", \"serviceVersion\")")
        // The flag header is the same with or without the tail: no presence bit is set for it.
        assertTrue("setBit(b, s, 2, true, lim)" in view && "setBit(b, s, 3" !in view, view)
        assertTrue("SIZE_BYTES" !in view, view)
        // A record's leaf ends before its own postfix, where its length ends.
        val frame = compilation.generatedFile("InfoFrame.kt")
        assertContains(frame, "InfoOtherSlice1(b, pos + 2, bodyLimit(pos + 2, end, 1), parent, ")
        assertContains(frame, "else -> InfoOtherSlice1(b, s, bodyLimit(s, lim, 1), null, discriminator)")
        // A nested layout in the tail is bounded by the end of its parent too.
        assertContains(compilation.generatedFile("LimitsView.kt"), "minOf(s + 1 + 4, lim)")
    }

    @Test
    fun `compiles a tail to the layout BytesFormat compiles`() {
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
        assertContains(compilation.generatedFile("StatusData.kt"), "serviceVersion: natural(16 unsigned) order=LSB/LSB trailing bytes=?")
        val problems = compilation.result.classLoader.loadClass("test.ParityKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems)
    }

    @Test
    fun `rejects a tail the end of a value cannot tell apart`() {
        val cases = listOf(
            """
            @BytesLayout
            interface Gap {
                @Trailing val first: Byte?
                val second: Byte
            }
            """ to "'second' of 'test.Gap' follows @Trailing 'first', so it must be @Trailing too",
            """
            @BytesLayout
            interface Required {
                @Trailing val first: Byte
            }
            """ to "@Trailing 'first' of 'test.Required' must be nullable",
            """
            @BytesLayout
            interface Flag {
                val first: Byte
                @Trailing val flag: Boolean?
            }
            """ to "@Trailing 'flag' of 'test.Flag' is a Boolean",
            """
            @BytesLayout
            interface Gated {
                @FlagIndex(0) val gate: Boolean
                @Trailing @PresentWhenAllSet(0) val first: Byte?
            }
            """ to "@Trailing 'first' of 'test.Gated' is @PresentWhenAllSet",
            """
            @BytesLayout
            interface Rest {
                val first: Byte
                @Trailing @Unsized val rest: String?
            }
            """ to "@Trailing 'rest' of 'test.Rest' runs to the end of the value itself",
            """
            @BytesLayout
            interface AfterRest {
                @Unsized val rest: String
                @Trailing val first: Byte?
            }
            """ to "@Trailing 'first' of 'test.AfterRest' follows 'rest', which runs to the end of the value",
            """
            @BytesLayout
            interface Tail {
                val id: Byte
                @Trailing val more: Byte?
            }

            @BytesLayout
            interface NotLast {
                val tail: Tail
                val after: Byte
            }
            """ to "'after' of 'test.NotLast' follows 'tail', whose @Trailing properties run to the end of what holds it",
            """
            @BytesLayout
            interface Tail {
                val id: Byte
                @Trailing val more: Byte?
            }

            @BytesLayout
            interface Tails {
                @Unsized val tails: List<Tail>
            }
            """ to "The items of",
        )
        cases.forEach { (source, message) ->
            listOf("DATA", "VIEW").forEach { generate ->
                val compilation = BytesCompilation(layoutSource(source), generate = generate)
                assertTrue(compilation.kspErrors.any { message in it }, "$generate: " + compilation.result.messages)
            }
        }
    }

    @Test
    fun `reports a tail in a frame accumulated most significant first as not supported by VIEW`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
                interface Mirrored {
                    val first: Short
                    @Trailing val second: Short?
                }
                """,
            ),
            generate = "DATA,VIEW",
        )
        assertTrue(compilation.kspErrors.any { "VIEW not yet supported for @Trailing properties in a frame accumulated MOST_SIGNIFICANT_FIRST" in it }, compilation.result.messages)
    }
}
