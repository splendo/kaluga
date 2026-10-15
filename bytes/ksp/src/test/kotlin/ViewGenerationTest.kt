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

@OptIn(ExperimentalCompilerApi::class)
class ViewGenerationTest {

    private val status = layoutSource(
        """
        @Serializable
        enum class Mode { OFF, ON, AUTO }

        @Serializable
        enum class Scale {
            @SerializedByteValue(0x43)
            CELSIUS,

            @SerializedByteValue(0x46)
            FAHRENHEIT,
        }

        @BytesLayout
        @Prefix([0x10])
        interface Status {
            @FlagIndex(0)
            val active: Boolean

            @FlagIndex(1)
            val mode: Mode
            val alarm: Boolean

            @Size(Length.`16_BIT`)
            @Unsigned
            val level: Int
            val scale: Scale
        }
        """,
    )

    @Test
    fun `generates a zero-copy view for a static layout`() {
        val compilation = BytesCompilation(status, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        assertEquals(GOLDEN, compilation.generatedFile("StatusView.kt"))
    }

    @Test
    fun `encodes and reads like BytesFormat`() {
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val data = StatusData(active = true, mode = Mode.AUTO, alarm = true, level = 0xBEEF, scale = Scale.FAHRENHEIT)
                val expected = BytesFormat.encodeToByteArray(StatusData.serializer(), data)
                val view = StatusView.encode(data)
                if (!view.bytes.contentEquals(expected)) add("encode " + view.bytes.toList() + " != " + expected.toList())
                if (!StatusView(true, Mode.AUTO, true, 0xBEEF, Scale.FAHRENHEIT).contentEquals(view)) add("invoke")
                if (view.toData() != data) add("toData " + view.toData())
                if (view.level != 0xBEEF || view.mode != Mode.AUTO || !view.active || !view.alarm || view.scale != Scale.FAHRENHEIT) add("getters " + view)

                view.mode = Mode.ON
                view.level = 7
                view.alarm = false
                view.scale = Scale.CELSIUS
                val changed = StatusData(active = true, mode = Mode.ON, alarm = false, level = 7, scale = Scale.CELSIUS)
                if (!view.bytes.contentEquals(BytesFormat.encodeToByteArray(StatusData.serializer(), changed))) add("setters " + view.bytes.toList())

                val framed = byteArrayOf(0x55) + view.bytes + 0x66
                val slice = StatusView.wrapRange(framed, 1, framed.size - 1)
                if (!view.contentEquals(slice) || slice != StatusView.wrapRange(framed.copyOf(), 1, framed.size - 1)) add("slice equality")
                (slice as StatusSlice).mode = Mode.OFF
                if (framed[2] != 0x01.toByte() || framed.first() != 0x55.toByte() || framed.last() != 0x66.toByte()) add("slice setter " + framed.toList())
                if (StatusData.from(slice) != changed.copy(mode = Mode.OFF)) add("slice getters " + slice)
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(status, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null)
        assertEquals(emptyList<String>(), problems)
    }

    @Test
    fun `follows the flag header for optional and variable sized properties`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            @Postfix([0x7E])
            interface Inner {
                val flag: Boolean?
                @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Size(Length.`32_BIT`)
                val count: Int
            }

            @BytesLayout
            interface Dynamic {
                @FlagIndex(9) val first: Boolean
                val optional: Byte?
                @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Unsigned
                val sized: UShort?
                @FlagIndex(12) @Size(Length.`8_BIT`) val a: Byte?
                @FlagIndex(13) @Size(Length.`8_BIT`) val b: Byte?
                @PresentWhenAllSet(12, 13) @Size(Length.`8_BIT`) val c: Byte?
                val fence: Byte
                val inner: Inner
                val maybeInner: Inner?
                val bit: Boolean?
                @Size(Length.`32_BIT`) @Size(Length.`64_BIT`) val decimal: Double
                @Size(Length.`16_BIT`) @Size(Length.`32_BIT`) @MedFloat val med: Double?
                @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) @Scalar(decimalExponent = 1) val scaled: Float
                val last: Short
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val inners = listOf(InnerData(null, 1), InnerData(true, 300), InnerData(false, 100000))
                for (optional in listOf<Byte?>(null, -3)) for (bit in listOf(null, true, false)) for (sized in listOf<UShort?>(null, 7u, 60000u))
                    for ((a, b, c) in listOf(Triple<Byte?, Byte?, Byte?>(null, null, null), Triple<Byte?, Byte?, Byte?>(1, 2, 3), Triple<Byte?, Byte?, Byte?>(1, null, null), Triple<Byte?, Byte?, Byte?>(null, 2, 9)))
                        for (inner in inners) for (maybeInner in listOf(null, inners[1])) for (decimal in listOf(0.5, 0.1)) for (med in listOf(null, 1.5, 123456789.0)) {
                            val data = DynamicData(bit != false, optional, sized, a, b, c, 5, inner, maybeInner, bit, decimal, med, 12.3f, -7)
                            val expected = BytesFormat.encodeToByteArray(DynamicData.serializer(), data)
                            val view = DynamicView.encode(data)
                            if (!view.bytes.contentEquals(expected)) add("encode " + data + ": " + view.bytes.toList() + " != " + expected.toList())
                            // An inconsistent @PresentWhenAllSet shifts what BytesFormat reads after it; the view reads the same, minus the postfix check.
                            val decoded = runCatching { BytesFormat.decodeFromByteArray(DynamicData.serializer(), expected) }.getOrNull() ?: continue
                            val read = runCatching { DynamicView(expected).toData() }
                            if (read.getOrNull() != decoded) add("decode " + data + ": " + read + " != " + decoded)
                            if (read.isSuccess && (DynamicView(expected).last != decoded.last || DynamicView(expected).inner.count != decoded.inner.count)) add("getters " + data)
                            val framed = byteArrayOf(1, 2) + expected
                            if (read.isSuccess && !DynamicView.wrapRange(framed, 2, framed.size).let { (it as DynamicSlice).toData() == decoded && view.contentEquals(it) }) add("slice " + data)
                            if (DynamicView(true, optional, sized, a, b, c, 5, inner, maybeInner, bit, decimal, med, 12.3f, -7).bytes.size != DynamicLayout.sizeOf(data.copy(first = true))) add("invoke " + data)
                        }
                val view = DynamicView.encode(DynamicData(true, 1, 2u, 1, 2, 3, 5, inners[2], null, true, 0.5, null, 1f, 3))
                view.last = 0x1234
                view.first = false
                val changed = DynamicData(false, 1, 2u, 1, 2, 3, 5, inners[2], null, true, 0.5, null, 1f, 0x1234)
                if (!view.bytes.contentEquals(BytesFormat.encodeToByteArray(DynamicData.serializer(), changed))) add("setters " + view.bytes.toList())
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val generated = compilation.generatedFile("DynamicView.kt")
        // Header bits whose meaning other properties depend on, and properties that move what follows, get no setter.
        assertTrue("override val optional: Byte?" in generated && "override val a: Byte?" in generated && "override var last: Short" in generated, generated)
        assertTrue("override var first: Boolean" in generated, generated)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `encodes and decodes strings like BytesFormat`() {
        val layouts = layoutSource(
            """
            @Serializable
            @JvmInline
            value class Name(@NullTerminated val name: String)

            @BytesLayout
            @Prefix([0x01])
            @Postfix([0x02, 0x03])
            interface Label {
                @Encoded(Encoding.UTF_16) val text: String
            }

            @BytesLayout
            interface Texts {
                val plain: String
                @Size(Length.`32_BIT`) val fixed: String
                @Encoded(Encoding.UTF_16) @Size(Length.`16_BIT`) val fixedWide: String
                @LengthPrefix(lengthAsShort = true) @Encoded(Encoding.ASCII) val short: String
                @LengthPrefix(canOverflow = true, sentinel = 0xFE.toByte()) val overflow: String
                @NullTerminated @Encoded(Encoding.UTF_16) val terminatedWide: String
                @Terminal(0x7C) val piped: String
                val optional: String?
                val name: Name
                val label: Label
                val count: Byte
                @Unsized val rest: String
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val long = "x".repeat(300)
                for (text in listOf("", "ab", "héllo wörld", long)) for (optional in listOf(null, "", "é")) {
                    val short = if (text == long) "short" else text
                    val data = TextsData(short, short, short, text.filter { it.code < 0x80 }, text, short, short.replace('|', '-'), optional, Name(short), LabelData(short), 7, text)
                    val expected = BytesFormat.encodeToByteArray(TextsData.serializer(), data)
                    val view = TextsView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode " + text + ": " + view.bytes.toList() + " != " + expected.toList())
                    val decoded = runCatching { BytesFormat.decodeFromByteArray(TextsData.serializer(), expected) }
                    val read = runCatching { TextsView(expected).toData() }
                    if (decoded.isSuccess != read.isSuccess || (decoded.isSuccess && read.getOrNull() != decoded.getOrNull())) add("decode " + text + ": " + read + " != " + decoded)
                    if (decoded.isSuccess && TextsView(expected).count != 7.toByte()) add("count " + text)
                }
                val view = TextsView.encode(TextsData("a", "abcd", "ab", "s", "o", "t", "p", null, Name("n"), LabelData("l"), 1, "r"))
                view.fixed = "z"
                view.fixedWide = "w"
                val changed = TextsData("a", "z", "w", "s", "o", "t", "p", null, Name("n"), LabelData("l"), 1, "r")
                if (!view.bytes.contentEquals(BytesFormat.encodeToByteArray(TextsData.serializer(), changed))) add("setters " + view.bytes.toList())
                if (runCatching { TextsView.encode(changed.copy(piped = "a|b")) }.exceptionOrNull() !is IllegalArgumentException) add("terminator in content")
                if (runCatching { TextsView.encode(changed.copy(plain = long)) }.exceptionOrNull() !is IllegalArgumentException) add("too long")
                if (runCatching { TextsView(byteArrayOf(5, 0x41)).plain }.exceptionOrNull() !is com.splendo.kaluga.bytes.layout.ByteArrayEndedBeforeSerializationCompleted) add("truncated")
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val generated = compilation.generatedFile("TextsView.kt")
        assertTrue("override var fixed: String" in generated && "override val plain: String" in generated, generated)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `encodes and decodes collections like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            interface Point {
                val x: Byte
                val y: Byte?
            }

            @BytesLayout
            interface Pair2 {
                val a: Byte
                val b: Byte
            }

            @BytesLayout
            interface Collections {
                val points: List<Point>
                @NullIfEmpty val optional: List<Pair2>?
                @NullIfEmpty val tags: Set<Byte>
                @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val raw: ByteArray
                @LengthPrefix(lengthAsShort = true) val pairs: Array<Pair2>
                @LengthPrefix @FlagIndex(4) val flagged: Collection<Pair2>
                @NullTerminated val terminated: List<Pair2>
                @ItemNullTerminated val names: List<String?>
                val byPoint: Map<Pair2, Short?>
                @Unsized val rest: List<Short>
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat
            import com.splendo.kaluga.bytes.view.FixedStrideList

            fun check(): List<String> = buildList {
                val pairs = listOf(Pair2Data(1, 2), Pair2Data(3, 4))
                for (n in listOf(0, 1, 3)) for (optional in listOf(null, emptyList(), pairs)) {
                    val points = List(n) { PointData(it.toByte(), if (it % 2 == 0) null else (it * 10).toByte()) }
                    val data = CollectionsData(
                        points, optional, List(n) { it.toByte() }.toSet(), ByteArray(n * 100) { it.toByte() }, pairs.take(n).toTypedArray(), pairs.take(n),
                        pairs.take(n), List(n) { if (it == 1) null else "n" + it }, pairs.take(n).associateWith { if (it.a == 1.toByte()) null else it.b.toShort() }, List(n) { (it * 1000).toShort() },
                    )
                    val expected = BytesFormat.encodeToByteArray(CollectionsData.serializer(), data)
                    val view = CollectionsView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode " + n + " " + optional + ": " + view.bytes.toList() + " != " + expected.toList())
                    val decoded = BytesFormat.decodeFromByteArray(CollectionsData.serializer(), expected)
                    val read = runCatching { CollectionsView(expected).toData() }
                    val same = read.getOrNull()?.let {
                        it.points == decoded.points && it.optional == decoded.optional && it.tags == decoded.tags && it.raw.contentEquals(decoded.raw) &&
                            it.pairs.toList() == decoded.pairs.toList() && it.flagged.toList() == decoded.flagged.toList() && it.terminated == decoded.terminated &&
                            it.names == decoded.names && it.byPoint == decoded.byPoint && it.rest == decoded.rest
                    }
                    if (same != true) add("decode " + n + " " + optional + ": " + read)
                    if (CollectionsView(expected).rest.size != n) add("lazy size " + n)
                }
                val view = CollectionsView.encode(CollectionsData(emptyList(), null, emptySet(), ByteArray(0), emptyArray(), pairs, emptyList(), emptyList(), emptyMap(), emptyList()))
                val flagged = view.flagged as FixedStrideList<Pair2>
                flagged[1] = Pair2Data(7, 8)
                val changed = CollectionsData(emptyList(), null, emptySet(), ByteArray(0), emptyArray(), listOf(pairs[0], Pair2Data(7, 8)), emptyList(), emptyList(), emptyMap(), emptyList())
                if (!view.bytes.contentEquals(BytesFormat.encodeToByteArray(CollectionsData.serializer(), changed))) add("set " + view.bytes.toList())
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `encodes nullable collections and framed or nullable items like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            interface Point {
                val x: Byte
                val y: Byte
            }

            @Serializable
            @Postfix([0x01])
            enum class Tagged { A, B }

            @BytesLayout
            @Prefix([0x50])
            @Postfix([0x60])
            @Checksum(8, 0x07u, 0x00u)
            interface Framed {
                val v: Byte
            }

            @BytesLayout
            interface Holder {
                val maybe: List<Byte>?
                val points: List<Point?>
                val tags: List<Tagged>
                val framed: List<Framed>
                val trailer: Byte
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                for (maybe in listOf(null, emptyList(), listOf<Byte>(1, 2))) {
                    val data = HolderData(maybe, listOf(PointData(1, 2), null), listOf(Tagged.A, Tagged.B), listOf(FramedData(0x11), FramedData(0x22)), 0x2A)
                    val expected = BytesFormat.encodeToByteArray(HolderData.serializer(), data)
                    val view = HolderView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode " + maybe + ": " + view.bytes.toList() + " != " + expected.toList())
                    val read = runCatching { HolderView(expected).toData() }
                    if (read.getOrNull() != data) add("decode " + maybe + ": " + read)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `dispatches sealed layouts like BytesFormat`() {
        val layouts = layoutSource(
            """
            @Serializable
            enum class Named { SHORT, LONGER_NAME }

            @BytesLayout
            @Prefix([0x7E])
            @Postfix([0x7F])
            sealed interface Shape {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface Dot : Shape {
                    val x: Byte
                }

                @BytesLayout
                @SerializedByteValue(0x02)
                interface Line : Shape {
                    val length: Short?
                    val name: Named
                }
            }

            @BytesLayout
            @SizePolymorphic
            sealed interface Sized

            @BytesLayout
            interface Two : Sized {
                val value: Short
            }

            @BytesLayout
            interface Rest : Sized {
                @Unsized val values: List<Byte>
            }

            @BytesLayout
            interface Drawing {
                val shape: Shape
                val maybe: Shape?
                val sized: Sized
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val shapes = listOf(ShapeDotData(3), ShapeLineData(null, Named.SHORT), ShapeLineData(300, Named.LONGER_NAME))
                val sizes = listOf(TwoData(0x0102), RestData(emptyList()), RestData(listOf(1)), RestData(listOf(1, 2, 3)))
                for (shape in shapes) {
                    val expected = BytesFormat.encodeToByteArray(ShapeData.serializer(), shape)
                    val frame = ShapeFrame.encode(shape)
                    if (!frame.bytes.contentEquals(expected)) add("encode " + shape + ": " + frame.bytes.toList() + " != " + expected.toList())
                    if (ShapeFrame(expected).toData() != shape) add("decode " + shape + ": " + ShapeFrame(expected))
                    for (maybe in listOf(null, shape)) for (sized in sizes) {
                        val data = DrawingData(shape, maybe, sized)
                        val bytes = BytesFormat.encodeToByteArray(DrawingData.serializer(), data)
                        val view = DrawingView.encode(data)
                        if (!view.bytes.contentEquals(bytes)) add("encode " + data + ": " + view.bytes.toList() + " != " + bytes.toList())
                        val decoded = BytesFormat.decodeFromByteArray(DrawingData.serializer(), bytes)
                        if (DrawingView(bytes).toData() != decoded) add("decode " + data + ": " + DrawingView(bytes).toData() + " != " + decoded)
                    }
                }
                if ((ShapeFrame(byteArrayOf(0x7E, 0x01, 0x09, 0x7F)).value as Shape.Dot).x != 9.toByte()) add("dispatch")
                if (runCatching { ShapeFrame(byteArrayOf(0x7E, 0x03, 0x09, 0x7F)).value }.exceptionOrNull() !is NoSuchElementException) add("unknown discriminator")
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val frame = compilation.generatedFile("ShapeFrame.kt")
        assertTrue("public value class ShapeFrame(" in frame && "public val `value`: Shape" in frame, frame)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `keeps checksums like BytesFormat`() {
        val layouts = layoutSource(
            """
            @Serializable
            @JvmInline
            @Checksum(8, 0x07u, 0x00u)
            value class Guarded(val value: Short)

            @BytesLayout
            @Prefix([0x01])
            @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
            interface Inner {
                val count: Byte
            }

            @BytesLayout
            @Postfix([0x7F])
            @Checksum(16, 0x1021u, 0xFFFFu)
            interface Outer {
                val inner: Inner
                val guarded: Guarded
                val label: String
                val last: Int
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.InvalidChecksumException
            import com.splendo.kaluga.bytes.layout.InvalidPostfix
            import com.splendo.kaluga.bytes.layout.InvalidPrefix
            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                fun expected(data: OuterData) = BytesFormat.encodeToByteArray(OuterData.serializer(), data)
                val data = OuterData(InnerData(3), Guarded(0x1234), "hi", 7)
                val view = OuterView.encode(data)
                if (!view.bytes.contentEquals(expected(data))) add("encode " + view.bytes.toList() + " != " + expected(data).toList())
                if (OuterView.wrap(view.bytes).toData() != data || !view.isChecksumValid()) add("wrap")

                view.last = 9
                if (!view.bytes.contentEquals(expected(data.copy(last = 9)))) add("setter " + view.bytes.toList())
                (view.inner as InnerSlice).count = 4
                if (!view.bytes.contentEquals(expected(data.copy(inner = InnerData(4), last = 9)))) add("nested setter " + view.bytes.toList())
                view.edit {
                    last = 11
                    (inner as InnerSlice).count = 5
                }
                if (!view.bytes.contentEquals(expected(data.copy(inner = InnerData(5), last = 11)))) add("edit " + view.bytes.toList())

                val corrupted = view.bytes.copyOf().also { it[it.size - 4] = (it[it.size - 4] + 1).toByte() }
                if (OuterView(corrupted).isChecksumValid()) add("checksum not checked")
                if (runCatching { OuterView.wrap(corrupted) }.exceptionOrNull() !is InvalidChecksumException) add("wrap checksum")
                if (runCatching { OuterView.wrap(corrupted, validateChecksum = false) }.isFailure) add("wrap without checksum")
                val badPostfix = view.bytes.copyOf().also { it[it.size - 1] = 0 }
                if (runCatching { OuterView.wrap(badPostfix) }.exceptionOrNull() !is InvalidPostfix) add("postfix")
                val badPrefix = view.bytes.copyOf().also { it[0] = 0 }
                if (runCatching { OuterView.wrap(badPrefix) }.exceptionOrNull() !is InvalidPrefix) add("prefix")
                for (bytes in listOf(corrupted, badPostfix, badPrefix)) {
                    val decoded = runCatching { BytesFormat.decodeFromByteArray(OuterData.serializer(), bytes) }.exceptionOrNull()
                    val wrapped = runCatching { OuterView.wrap(bytes) }.exceptionOrNull()
                    if (decoded?.let { it::class } != wrapped?.let { it::class }) add("errors differ: " + decoded + " / " + wrapped)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `mirrors frames accumulated most significant first like BytesFormat`() {
        val layouts = layoutSource(
            """
            @Serializable
            enum class Level {
                @SerializedByteValue(0x01, 0x00)
                LOW,

                @SerializedByteValue(0x00, 0x01)
                HIGH,
            }

            @BytesLayout
            @Prefix([0x10, 0x11])
            @Postfix([0x7F])
            @Checksum(16, 0x8005u, 0x0000u, reflectIn = true, reflectOut = true)
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            interface Mirrored {
                val number: Int
                @Encoded(Encoding.UTF_16) val letter: Char
                val level: Level
                @LengthPrefix(lengthAsShort = true) val name: String
                val values: List<Short>
                @NullTerminated val labels: Map<String, String>
                @Size(Length.`32_BIT`) val fixed: String
                val ratio: Double
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.layout.InvalidChecksumException
            import com.splendo.kaluga.bytes.serialization.BytesFormat
            import com.splendo.kaluga.bytes.serialization.BytesFormat as Format

            fun check(): List<String> = buildList {
                fun expected(data: MirroredData) = BytesFormat.encodeToByteArray(MirroredData.serializer(), data)
                for (n in listOf(0, 1, 3)) {
                    val data = MirroredData(0x01020304 * n, 'é', if (n == 1) Level.HIGH else Level.LOW, "n".repeat(n), List(n) { (it * 300).toShort() }, List(n) { "k" + it to "v".repeat(it) }.toMap(), "f".repeat(n), n * 1.5)
                    val view = MirroredView.encode(data)
                    if (!view.bytes.contentEquals(expected(data))) add("encode " + n + ": " + view.bytes.toList() + " != " + expected(data).toList())
                    val decoded = BytesFormat.decodeFromByteArray(MirroredData.serializer(), expected(data))
                    val read = runCatching { MirroredView.wrap(expected(data)).toData() }
                    if (read.getOrNull() != decoded) add("decode " + n + ": " + read + " != " + decoded)
                    val padded = byteArrayOf(9, 9) + expected(data)
                    if (runCatching { MirroredView.wrap(padded).toData() }.getOrNull() != Format.decodeFromByteArray(MirroredData.serializer(), padded)) add("padded " + n)
                }
                val view = MirroredView.encode(MirroredData(1, 'a', Level.LOW, "x", listOf(1), emptyMap(), "y", 2.25))
                view.number = 0x0A0B0C0D
                view.level = Level.HIGH
                view.fixed = "abcd"
                val changed = MirroredData(0x0A0B0C0D, 'a', Level.HIGH, "x", listOf(1), emptyMap(), "abcd", 2.25)
                if (!view.bytes.contentEquals(expected(changed))) add("setters " + view.bytes.toList() + " != " + expected(changed).toList())
                val corrupted = view.bytes.copyOf().also { it[it.size - 3] = 0x55 }
                if (runCatching { MirroredView.wrap(corrupted) }.exceptionOrNull() !is InvalidChecksumException) add("checksum")
                if (runCatching { BytesFormat.decodeFromByteArray(MirroredData.serializer(), corrupted) }.exceptionOrNull() !is InvalidChecksumException) add("data checksum")
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `stuffs frames, strings and collections like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            @Prefix([0xF1.toByte()])
            @Postfix([0xF2.toByte()])
            @ByteStuffed
            @Checksum(8, 0x07u, 0x00u)
            interface Frame {
                val command: Byte
                @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
                val values: List<Byte>
                @Encoded(Encoding.UTF_16)
                @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
                val name: String
                @ByteStuffedCobs
                val pairs: Map<Byte, Short>
                @ByteStuffedSlip
                val slip: ByteArray
                @Encoded(Encoding.ASCII)
                @ByteStuffed
                @Terminal(0xF2.toByte())
                val label: String
                val last: Byte
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                fun expected(data: FrameData) = BytesFormat.encodeToByteArray(FrameData.serializer(), data)
                val full = FrameData(
                    command = 0xF1.toByte(),
                    values = listOf(0x00, 0x7D, 0x01),
                    name = "AB",
                    pairs = mapOf(0x00.toByte() to 0x0100.toShort(), 0x02.toByte() to 0x7D00.toShort()),
                    slip = byteArrayOf(0xC0.toByte(), 0x41, 0xDB.toByte()),
                    label = "hi",
                    last = 0x7D,
                )
                val empty = FrameData(0x01, emptyList(), "", emptyMap(), byteArrayOf(), "", 0x02)
                for (data in listOf(full, empty)) {
                    val wire = FrameView.encodeToWire(data)
                    if (!wire.contentEquals(expected(data))) add("encode " + wire.toList() + " != " + expected(data).toList())
                    val view = FrameView.fromWire(wire)
                    if (!view.toData().contentMatches(data)) add("fromWire " + view.toData())
                    if (!view.toWire().contentEquals(wire)) add("toWire")
                    if (!FrameView.encode(data).bytes.contentEquals(view.bytes)) add("unstuffed")
                    view.last = 0xF3.toByte()
                    if (!view.toWire().contentEquals(expected(data.copy(last = 0xF3.toByte())))) add("setter " + view.toWire().toList())
                    val badChecksum = expected(data).also { it[it.size - 2] = (it[it.size - 2] + 1).toByte() }
                    for (bytes in listOf(badChecksum, byteArrayOf(0xF1.toByte()), byteArrayOf(0xF1.toByte(), 0xF3.toByte(), 0xF2.toByte()), wire.copyOf(wire.size - 3) + 0xF2.toByte())) {
                        val decoded = runCatching { BytesFormat.decodeFromByteArray(FrameData.serializer(), bytes) }.exceptionOrNull()
                        val wrapped = runCatching { FrameView.fromWire(bytes) }.exceptionOrNull()
                        if (decoded == null || decoded::class != wrapped?.let { it::class }) add("errors differ: " + decoded + " / " + wrapped)
                    }
                }
            }

            private val noSlip = byteArrayOf()

            private fun FrameData.contentMatches(other: FrameData) = copy(slip = noSlip) == other.copy(slip = noSlip) && slip.contentEquals(other.slip)
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `encodes stuffed collections nested at any depth like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            interface Inner {
                @ByteStuffedCobs
                val values: List<Byte>
                val last: Byte
            }

            @BytesLayout
            interface Outer {
                val first: Inner
                val inners: List<Inner>
                @ByteStuffedXor(escapeByte = 0x7D, delimiter = 0x00)
                val own: List<Short>
                val second: Inner
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                val inner = InnerData(listOf(0x00, 0x01, 0x00), 0x7D)
                val data = OuterData(inner, listOf(inner, InnerData(emptyList(), 0x00), inner), listOf(0x0100, 0x7D00), InnerData(listOf(0x02), 0x03))
                val expected = BytesFormat.encodeToByteArray(OuterData.serializer(), data)
                val view = OuterView.encode(data)
                if (!view.bytes.contentEquals(expected)) add("encode " + view.bytes.toList() + " != " + expected.toList())
                val invoked = OuterView(data.first, data.inners, data.own, data.second)
                if (!invoked.bytes.contentEquals(expected)) add("invoke " + invoked.bytes.toList())
                if (OuterView.wrap(expected).toData() != data) add("decode " + OuterView.wrap(expected).toData())
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `rejects byte stuffing BytesFormat does not apply`() {
        for ((layout, feature) in listOf(
            """
            @BytesLayout
            @ByteStuffedCobs
            interface Inner {
                val value: Byte
            }

            @BytesLayout
            interface Outer {
                val inner: Inner
            }
            """ to "byte stuffing on a nested layout",
            """
            @BytesLayout
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
            interface Outer {
                @ByteStuffedCobs
                val values: List<Short>
            }
            """ to "byte-stuffed collections encoded MOST_SIGNIFICANT_FIRST",
            """
            @BytesLayout
            interface Inner {
                val value: Byte
            }

            @BytesLayout
            interface Outer {
                @ByteStuffedCobs
                val values: List<Inner>
            }
            """ to "layouts as items of byte-stuffed collections",
        )) {
            val compilation = BytesCompilation(layoutSource(layout), generate = "VIEW")

            assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilation.result.exitCode, compilation.result.messages)
            assertTrue("VIEW not yet supported for $feature" in compilation.result.messages, compilation.result.messages)
        }
    }

    @Test
    fun `rejects flag bits in a frame accumulated most significant first`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST)
                interface Mirrored {
                    val maybe: Byte?
                }
                """,
            ),
            generate = "VIEW",
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilation.result.exitCode, compilation.result.messages)
        assertTrue("in a frame accumulated MOST_SIGNIFICANT_FIRST in @BytesLayout test.Mirrored" in compilation.result.messages, compilation.result.messages)
    }

    @Test
    fun `reads content to the end of a frame with a checksum like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            @Checksum(8, 0x07u, 0x00u)
            @Postfix([0x55])
            interface Summed {
                val head: Byte
                @Unsized val rest: String
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                for (rest in listOf("", "abc")) {
                    val data = SummedData(0x01, rest)
                    val expected = BytesFormat.encodeToByteArray(SummedData.serializer(), data)
                    val view = SummedView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode: " + view.bytes.toList() + " != " + expected.toList())
                    val read = runCatching { SummedView(expected).toData() }
                    if (read.getOrNull() != data) add("decode: " + read)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `encodes strings in a big-endian frame like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
            interface BigEndianText {
                val text: String
                @Encoded(Encoding.UTF_16) val wide: String
                @LengthPrefix(canOverflow = true) val long: String
                @NullTerminated val tail: String
                val number: Short
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                for (long in listOf("", "x".repeat(300))) {
                    val data = BigEndianTextData("AB", "CD", long, "EF", 0x0102)
                    val expected = BytesFormat.encodeToByteArray(BigEndianTextData.serializer(), data)
                    val view = BigEndianTextView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode: " + view.bytes.toList() + " != " + expected.toList())
                    val read = runCatching { BigEndianTextView(expected).toData() }
                    if (read.getOrNull() != data) add("decode: " + read)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `starts a nested layout after body Booleans on a fresh byte like BytesFormat`() {
        val layouts = layoutSource(
            """
            @BytesLayout
            interface Inner {
                @FlagIndex(0) val flag: Boolean
            }

            @BytesLayout
            interface Leading {
                val flag: Boolean
            }

            @BytesLayout
            interface Empty

            @BytesLayout
            interface Outer {
                val bit: Boolean
                val inner: Inner
                val other: Boolean
                val leading: Leading
                val more: Boolean
                val empty: Empty
                val trailer: Byte
            }
            """,
        )
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat

            fun check(): List<String> = buildList {
                for (flag in listOf(false, true)) {
                    val data = OuterData(true, InnerData(flag), false, LeadingData(!flag), true, EmptyData, 0x2A)
                    val expected = BytesFormat.encodeToByteArray(OuterData.serializer(), data)
                    val view = OuterView.encode(data)
                    if (!view.bytes.contentEquals(expected)) add("encode " + flag + ": " + view.bytes.toList() + " != " + expected.toList())
                    val read = runCatching { OuterView(expected).toData() }
                    if (read.getOrNull() != data) add("decode " + flag + ": " + read)
                }
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(layouts, check, generate = "DATA,VIEW")

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `rejects a layout using a feature VIEW does not support yet`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Optional {
                    @Reserved(before = 1)
                    val value: Byte?
                }
                """,
            ),
            generate = "VIEW",
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, compilation.result.exitCode, compilation.result.messages)
        val errors = compilation.result.messages.lines().filter { it.startsWith("e: [ksp]") }
        assertTrue(
            errors.any {
                "VIEW not yet supported for @Reserved on a nullable property in @BytesLayout test.Optional" in it && "Layouts.kt" in it
            },
            errors.joinToString("\n"),
        )
    }

    @Test
    fun `skips a layout using a feature VIEW does not support yet when asked`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Optional {
                    @Reserved(before = 1)
                    val value: Byte?
                }

                @BytesLayout
                interface Plain {
                    val value: Byte
                }

                @BytesLayout
                interface Outer {
                    val optional: Optional
                }
                """,
            ),
            generate = "DATA,VIEW",
            skipUnsupportedViews = true,
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val warnings = compilation.result.messages.lines().filter { it.startsWith("w: [ksp]") }
        assertTrue(
            warnings.any {
                "VIEW not yet supported for @Reserved on a nullable property in @BytesLayout test.Optional; no view is generated" in it
            },
            warnings.joinToString("\n"),
        )
        assertTrue(warnings.any { "in @BytesLayout test.Outer; no view is generated" in it }, warnings.joinToString("\n"))
        assertEquals(
            listOf("GeneratedBytesLayouts.kt", "GeneratedBytesViews.kt", "OptionalData.kt", "OuterData.kt", "PlainData.kt", "PlainView.kt"),
            compilation.generatedFiles.map { it.name }.sorted(),
        )
    }

    @Test
    fun `shares a nested layout context and adds one where the nesting changes the encoding`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Inner {
                    @Size(Length.`16_BIT`)
                    val value: Int
                }

                @BytesLayout
                interface Same {
                    val inner: Inner
                }

                @BytesLayout
                @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
                interface BigEndian {
                    val inner: Inner
                }
                """,
            ),
            generate = "VIEW",
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        assertTrue("= InnerSlice(b, s, s + 2, parent)" in compilation.generatedFile("SameView.kt"), compilation.generatedFile("SameView.kt"))
        assertTrue("= InnerSlice1(b, s, s + 2, parent)" in compilation.generatedFile("BigEndianView.kt"), compilation.generatedFile("BigEndianView.kt"))
        val inner = compilation.generatedFile("InnerView.kt")
        assertTrue("internal object InnerLayout1 {" in inner && "ByteOrder.MOST_SIGNIFICANT_FIRST" in inner, inner)
    }

    private companion object {
        val GOLDEN = """
            // Generated by the Kaluga bytes KSP processor from test.Status. Do not edit.
            @file:OptIn(InternalBytesApi::class)

            package test

            import com.splendo.kaluga.bytes.ByteOrder
            import com.splendo.kaluga.bytes.annotations.Length
            import com.splendo.kaluga.bytes.layout.InternalBytesApi
            import com.splendo.kaluga.bytes.view.ViewBatch
            import com.splendo.kaluga.bytes.view.ViewBytes
            import com.splendo.kaluga.bytes.view.ViewChecksum
            import com.splendo.kaluga.bytes.view.ViewParent
            import kotlin.Any
            import kotlin.Array
            import kotlin.Boolean
            import kotlin.ByteArray
            import kotlin.Int
            import kotlin.OptIn
            import kotlin.RuntimeException
            import kotlin.String
            import kotlin.jvm.JvmInline

            /**
             * The zero-copy VIEW implementation of [Status], reading and writing [bytes] in place.
             *
             * `==` compares the backing arrays by reference, since a value class cannot override `equals`; use [contentEquals] to compare content.
             */
            @JvmInline
            public value class StatusView(
                public val bytes: ByteArray,
            ) : Status {
                override var active: Boolean
                    get() = StatusLayout.getActive(bytes, 0, bytes.size)
                    set(`value`) {
                        StatusLayout.setActive(bytes, 0, bytes.size, value)
                    }

                override var mode: Mode
                    get() = StatusLayout.getMode(bytes, 0, bytes.size)
                    set(`value`) {
                        StatusLayout.setMode(bytes, 0, bytes.size, value)
                    }

                override var alarm: Boolean
                    get() = StatusLayout.getAlarm(bytes, 0, bytes.size)
                    set(`value`) {
                        StatusLayout.setAlarm(bytes, 0, bytes.size, value)
                    }

                override var level: Int
                    get() = StatusLayout.getLevel(bytes, 0, bytes.size)
                    set(`value`) {
                        StatusLayout.setLevel(bytes, 0, bytes.size, value)
                    }

                override var scale: Scale
                    get() = StatusLayout.getScale(bytes, 0, bytes.size)
                    set(`value`) {
                        StatusLayout.setScale(bytes, 0, bytes.size, value)
                    }

                /**
                 * Checks these bytes the way `BytesFormat` decodes them, throwing its exception for the first problem found.
                 */
                public fun validate(validateChecksum: Boolean = true) {
                    StatusLayout.validate(bytes, 0, bytes.size, validateChecksum)
                }

                /**
                 * Copies this view into a [StatusData].
                 */
                public fun toData(): StatusData = StatusLayout.toData(bytes, 0, bytes.size)

                /**
                 * Whether [other] is a view of [Status] holding the same bytes.
                 */
                public fun contentEquals(other: Status): Boolean = StatusLayout.contentEquals(bytes, 0, bytes.size, other)

                /**
                 * A hash of the bytes of this view, consistent with [contentEquals].
                 */
                public fun contentHashCode(): Int = StatusLayout.contentHashCode(bytes, 0, bytes.size)

                override fun toString(): String = StatusLayout.describe("StatusView", bytes, 0, bytes.size)

                public companion object {
                    /**
                     * The size of every [StatusView] in bytes.
                     */
                    public const val SIZE_BYTES: Int = 5

                    /**
                     * Encodes [value] into a new [StatusView].
                     */
                    public fun encode(`value`: Status): StatusView = StatusView(ByteArray(5).also { StatusLayout.write(it, 0, 5, value) })

                    /**
                     * Encodes a new [StatusView] from its property values.
                     */
                    public operator fun invoke(
                        active: Boolean,
                        mode: Mode,
                        alarm: Boolean,
                        level: Int,
                        scale: Scale,
                    ): StatusView = StatusView(ByteArray(5).also { StatusLayout.writeValues(it, 0, 5, active, mode, alarm, level, scale) })

                    /**
                     * A view of [bytes], checked first as `BytesFormat` checks what it decodes; the constructor checks nothing.
                     */
                    public fun wrap(bytes: ByteArray, validateChecksum: Boolean = true): StatusView {
                        StatusLayout.validate(bytes, 0, bytes.size, validateChecksum)
                        return StatusView(bytes)
                    }

                    /**
                     * A view of [Status] over `[from, to)` of [bytes], checked first like [wrap], reading and writing it in place.
                     */
                    public fun wrapRange(
                        bytes: ByteArray,
                        from: Int,
                        to: Int,
                        validateChecksum: Boolean = true,
                    ): Status {
                        ViewBytes.checkWrapRange(bytes, from, to)
                        StatusLayout.validate(bytes, from, to, validateChecksum)
                        return StatusSlice(bytes, from, to)
                    }
                }
            }

            /**
             * This value as a [StatusView]: itself when it already is one, otherwise a new encoding of it.
             */
            public fun Status.toStatusView(): StatusView = this as? StatusView ?: StatusView.encode(this)

            /**
             * The accessors of [Status] as compiled here: property functions take the array, the frame start and the end of the body.
             */
            internal object StatusLayout {
                public const val SIZE: Int = 5

                private val framePrefix: ByteArray = byteArrayOf(0x10)

                private val scaleTable: Array<ByteArray> = arrayOf(byteArrayOf(0x43), byteArrayOf(0x46))

                public fun getActive(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Boolean = ViewBytes.bit(b, s, 8, lim)

                public fun setActive(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Boolean,
                ) {
                    ViewBytes.setBit(b, s, 8, value, lim)
                }

                public fun getMode(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Mode = ViewBytes.entry(Mode.entries, ViewBytes.bits(b, s, 9, 2, lim).toInt(), "test.Mode")

                public fun setMode(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Mode,
                ) {
                    ViewBytes.setBits(b, s, 9, 2, value.ordinal.toLong(), lim)
                }

                public fun getAlarm(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Boolean = ViewBytes.bit(b, s, 11, lim)

                public fun setAlarm(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Boolean,
                ) {
                    ViewBytes.setBit(b, s, 11, value, lim)
                }

                public fun getLevel(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Int = ViewBytes.readNatural(b, s + 2, Length.`16_BIT`, false, ByteOrder.LEAST_SIGNIFICANT_FIRST, lim).toInt()

                public fun setLevel(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Int,
                ) {
                    ViewBytes.writeNatural(b, s + 2, value.toUInt().toLong(), Length.`16_BIT`, false, ByteOrder.LEAST_SIGNIFICANT_FIRST, lim)
                }

                public fun getScale(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Scale = Scale.entries[ViewBytes.matchIndex(b, s + 4, scaleTable, lim)]

                public fun setScale(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Scale,
                ) {
                    ViewBytes.write(b, s + 4, scaleTable[value.ordinal], lim)
                }

                public fun sizeOfValues(
                    active: Boolean,
                    mode: Mode,
                    alarm: Boolean,
                    level: Int,
                    scale: Scale,
                ): Int = SIZE

                /**
                 * Writes the frame of these values at [s], returning where it ends.
                 */
                public fun writeValues(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    active: Boolean,
                    mode: Mode,
                    alarm: Boolean,
                    level: Int,
                    scale: Scale,
                ): Int {
                    ViewBytes.clear(b, s, s + SIZE)
                    ViewBytes.write(b, s, framePrefix, lim)
                    ViewBytes.setBit(b, s, 8, active, lim)
                    ViewBytes.setBits(b, s, 9, 2, mode.ordinal.toLong(), lim)
                    ViewBytes.setBit(b, s, 11, alarm, lim)
                    ViewBytes.writeNatural(b, s + 2, level.toUInt().toLong(), Length.`16_BIT`, false, ByteOrder.LEAST_SIGNIFICANT_FIRST, lim)
                    ViewBytes.write(b, s + 4, scaleTable[scale.ordinal], lim)
                    val bodyEnd = s + 5
                    return bodyEnd
                }

                public fun validate(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    checkChecksum: Boolean,
                ) {
                    ViewChecksum.checkPrefix(b, s, framePrefix, lim + 0)
                    run {
                        val pos = s + 2
                        getActive(b, s, lim)
                    }
                    run {
                        val pos = s + 2
                        getMode(b, s, lim)
                    }
                    run {
                        val pos = s
                        getAlarm(b, s, lim)
                    }
                    run {
                        val pos = s + 2
                        getLevel(b, s, lim)
                    }
                    run {
                        val pos = s + 4
                        val index = ViewBytes.matchIndex(b, pos + 0, scaleTable, lim)
                    }
                    val end = s + 5
                    ViewBytes.checkRange(end, 0, lim + 0)
                }

                /**
                 * The end of the frame starting at [s], following the flag header like the decoder does.
                 */
                public fun end(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Int = s + SIZE

                public fun sizeOf(`value`: Status): Int = SIZE

                public fun write(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    `value`: Status,
                ): Int = writeValues(b, s, lim, value.active, value.mode, value.alarm, value.level, value.scale)

                public fun toData(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): StatusData = StatusData(active = getActive(b, s, lim), mode = getMode(b, s, lim), alarm = getAlarm(b, s, lim), level = getLevel(b, s, lim), scale = getScale(b, s, lim))

                public fun contentEquals(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                    other: Any?,
                ): Boolean = when (other) {
                    is StatusView -> ViewBytes.rangeEquals(b, s, s + 5, other.bytes, 0, 0 + 5)
                    is StatusSlice -> ViewBytes.rangeEquals(b, s, s + 5, other.sliceBytes, other.sliceStart, other.sliceStart + 5)
                    else -> false
                }

                public fun contentHashCode(
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): Int = ViewBytes.rangeHashCode(b, s, s + 5)

                public fun describe(
                    name: String,
                    b: ByteArray,
                    s: Int,
                    lim: Int,
                ): String = try {
                    name + "(" + "active=" + getActive(b, s, lim) + ", mode=" + getMode(b, s, lim) + ", alarm=" + getAlarm(b, s, lim) + ", level=" + getLevel(b, s, lim) + ", scale=" + getScale(b, s, lim) + ")"
                } catch (e: RuntimeException) {
                    name + "(" + ViewBytes.hex(b, s, s + SIZE) + ")"
                }
            }

            /**
             * A view of [Status] over part of a larger array, as nested layouts and `wrapRange` return it; cast to it to write a nested layout's properties in place.
             */
            public class StatusSlice internal constructor(
                internal val sliceBytes: ByteArray,
                internal val sliceStart: Int,
                internal val sliceLimit: Int,
                internal val sliceParent: ViewParent? = null,
            ) : Status {
                override var active: Boolean
                    get() = StatusLayout.getActive(sliceBytes, sliceStart, sliceLimit)
                    set(`value`) {
                        StatusLayout.setActive(sliceBytes, sliceStart, sliceLimit, value)
                        changed()
                    }

                override var mode: Mode
                    get() = StatusLayout.getMode(sliceBytes, sliceStart, sliceLimit)
                    set(`value`) {
                        StatusLayout.setMode(sliceBytes, sliceStart, sliceLimit, value)
                        changed()
                    }

                override var alarm: Boolean
                    get() = StatusLayout.getAlarm(sliceBytes, sliceStart, sliceLimit)
                    set(`value`) {
                        StatusLayout.setAlarm(sliceBytes, sliceStart, sliceLimit, value)
                        changed()
                    }

                override var level: Int
                    get() = StatusLayout.getLevel(sliceBytes, sliceStart, sliceLimit)
                    set(`value`) {
                        StatusLayout.setLevel(sliceBytes, sliceStart, sliceLimit, value)
                        changed()
                    }

                override var scale: Scale
                    get() = StatusLayout.getScale(sliceBytes, sliceStart, sliceLimit)
                    set(`value`) {
                        StatusLayout.setScale(sliceBytes, sliceStart, sliceLimit, value)
                        changed()
                    }

                private fun changed() {
                    if (sliceParent !== ViewBatch) {
                        sliceParent?.changed()
                    }
                }

                private fun childParent(): ViewParent? = sliceParent

                /**
                 * Copies this view into a [StatusData].
                 */
                public fun toData(): StatusData = StatusLayout.toData(sliceBytes, sliceStart, sliceLimit)

                override fun equals(other: Any?): Boolean = other is StatusSlice && StatusLayout.contentEquals(sliceBytes, sliceStart, sliceLimit, other)

                override fun hashCode(): Int = StatusLayout.contentHashCode(sliceBytes, sliceStart, sliceLimit)

                override fun toString(): String = StatusLayout.describe("StatusSlice", sliceBytes, sliceStart, sliceLimit)
            }

        """.trimIndent()
    }
}
