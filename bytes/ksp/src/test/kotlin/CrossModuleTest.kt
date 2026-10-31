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
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Layouts holding layouts of another module: one compilation stands in for the module, the next depends on its output. */
@OptIn(ExperimentalCompilerApi::class)
class CrossModuleTest {

    // In a package of its own, as the registries of both compilations have the default prefix.
    private val parts = BytesCompilation(
        SourceFile.kotlin(
            "Parts.kt",
            """
            package test.parts

            import com.splendo.kaluga.bytes.annotations.*

            @BytesLayout
            interface BatteryLevel {
                @Size(Length.`8_BIT`) @Unsigned val level: Int
            }

            // Not in alphabetical order, and with a width picked by flag bits.
            @BytesLayout
            interface Reading {
                @Size(Length.`8_BIT`) @Size(Length.`16_BIT`) val value: Int
                @Size(Length.`8_BIT`) @Unsigned val channel: Int
            }

            @BytesLayout
            @Prefix([0x10])
            sealed interface Event {
                @BytesLayout
                @SerializedByteValue(0x01)
                interface Tap : Event {
                    val count: Byte
                }

                @BytesLayout
                @SerializedByteValue(0x02)
                interface Swipe : Event {
                    val reading: Reading
                }
            }

            @BytesLayout(generate = [Generate.VIEW])
            interface Counter {
                val count: Byte
            }
            """.trimIndent(),
        ),
        SourceFile.kotlin(
            "More.kt",
            """
            package test.parts.more

            import com.splendo.kaluga.bytes.annotations.*

            @BytesLayout
            interface BatteryLevel {
                @Size(Length.`16_BIT`) val millivolts: Int
            }
            """.trimIndent(),
        ),
        generate = "DATA,VIEW",
    )

    private val device = layoutSource(
        """
        import test.parts.*

        @BytesLayout
        interface DeviceInformation {
            val batteryLevel: BatteryLevel

            @Size(Length.`16_BIT`) @Unsigned
            val firmware: Int
            val reading: Reading
            val readings: List<Reading>
            val event: Event?
        }

        @BytesLayout
        interface ChargerStatus {
            val charging: Boolean
            val battery: BatteryLevel
        }

        @BytesLayout
        @com.splendo.kaluga.bytes.annotations.ByteOrder(ByteOrder.MOST_SIGNIFICANT_FIRST, excludeStructure = true)
        interface BigEndianReading {
            val reading: Reading
        }

        @BytesLayout
        interface Spares {
            val first: BatteryLevel
            val second: test.parts.more.BatteryLevel
        }

        @BytesLayout(generate = [Generate.VIEW])
        interface Counted {
            val counter: Counter
        }
        """,
    )

    @Test
    fun `views and encodes layouts of another module like BytesFormat`() {
        assertEquals(KotlinCompilation.ExitCode.OK, parts.result.exitCode, parts.result.messages)
        val check = SourceFile.kotlin(
            "Check.kt",
            """
            @file:OptIn(com.splendo.kaluga.bytes.layout.InternalBytesApi::class)

            package test

            import com.splendo.kaluga.bytes.serialization.BytesFormat
            import com.splendo.kaluga.bytes.serialization.layoutDumpOf
            import test.parts.*

            fun check(): List<String> = buildList {
                GeneratedBytesLayouts.dataLayouts.forEach { (serializer, dump) ->
                    val runtime = layoutDumpOf(serializer.descriptor)
                    if (runtime != dump) add(serializer.descriptor.serialName + ":\n" + dump + "--\n" + runtime)
                }
                val events = listOf(null, EventTapData(3), EventSwipeData(ReadingData(0x1234, 7)))
                for (value in listOf(0x12, 0x1234)) for (event in events) {
                    val data = DeviceInformationData(BatteryLevelData(0x64), 0x0102, ReadingData(value, 1), listOf(ReadingData(2, 3), ReadingData(value, 4)), event)
                    val bytes = BytesFormat.encodeToByteArray(DeviceInformationData.serializer(), data)
                    val view = DeviceInformationView.encode(data)
                    if (!view.bytes.contentEquals(bytes)) add("encode " + data + ": " + view.bytes.toList() + " != " + bytes.toList())
                    val wrapped = DeviceInformationView.wrap(bytes)
                    if (wrapped.toData() != data) add("decode " + data + ": " + wrapped.toData())
                    if (wrapped.reading.value != value || wrapped.reading.channel != 1 || wrapped.readings[1].channel != 4) add("getters " + wrapped)
                    if (GeneratedBytesViews.views.none { it.serialName == "test.DeviceInformation" }) add("registry")
                }
                val charger = ChargerStatusView(true, BatteryLevelData(0x32))
                if (!charger.bytes.contentEquals(byteArrayOf(0x01, 0x32))) add("invoke " + charger.bytes.toList())
                (charger.battery as ChargerStatusLayout.BatteryLevelSlice).level = 0x33
                if (charger.battery.level != 0x33 || charger.toData() != ChargerStatusData(true, BatteryLevelData(0x33))) add("slice setter " + charger)

                val bigEndian = BigEndianReadingData(ReadingData(0x1234, 5))
                val bigEndianBytes = BytesFormat.encodeToByteArray(BigEndianReadingData.serializer(), bigEndian)
                if (!BigEndianReadingView.encode(bigEndian).bytes.contentEquals(bigEndianBytes)) add("big endian " + BigEndianReadingView.encode(bigEndian))
                if (BigEndianReadingView.wrap(bigEndianBytes).toData() != bigEndian) add("big endian decode " + BigEndianReadingView(bigEndianBytes))

                val spares = SparesData(BatteryLevelData(1), test.parts.more.BatteryLevelData(0x0203))
                if (SparesView.wrap(BytesFormat.encodeToByteArray(SparesData.serializer(), spares)).toData() != spares) add("spares " + SparesView.encode(spares))

                val counted = CountedView(CounterView(9))
                if (!counted.bytes.contentEquals(byteArrayOf(9)) || counted.counter.count != 9.toByte()) add("view only " + counted)
            }
            """.trimIndent(),
        )
        val compilation = BytesCompilation(device, check, generate = "DATA,VIEW", classpaths = listOf(parts.result.outputDirectory))

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val declaring = compilation.generatedFiles.filter { Regex("""(?m)^\S.*(class|object) (BatteryLevel|Reading|Event)""").containsMatchIn(it.readText()) }.map { it.name }
        assertEquals(emptyList(), declaring, "Only the parts' compilation declares their top-level types")
        val view = compilation.generatedFile("DeviceInformationView.kt")
        assertTrue("    internal object BatteryLevelLayout {" in view && "    public class BatteryLevelSlice internal constructor(" in view, view)
        val spares = compilation.generatedFile("SparesView.kt")
        assertTrue("    internal object BatteryLevelLayout {" in spares && "    internal object TestPartsMoreBatteryLevelLayout {" in spares, spares)
        val bigEndian = compilation.generatedFile("BigEndianReadingView.kt")
        assertTrue("ByteOrder.MOST_SIGNIFICANT_FIRST" in bigEndian && "ReadingSlice(" in bigEndian, bigEndian)
        val problems = compilation.result.classLoader.loadClass("test.CheckKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems.take(10))
    }

    @Test
    fun `generates the views of layouts of another module only in the pass owning their holder`() {
        assertEquals(KotlinCompilation.ExitCode.OK, parts.result.exitCode, parts.result.messages)
        val workingDir = Files.createTempDirectory("bytes-ksp").toFile()
        val compilation = BytesCompilation(
            SourceFile.kotlin(
                "common/Common.kt",
                """
                package test

                import com.splendo.kaluga.bytes.annotations.BytesLayout
                import test.parts.BatteryLevel

                @BytesLayout
                interface Inner {
                    val battery: BatteryLevel
                }
                """.trimIndent(),
            ),
            layoutSource(
                """
                import test.parts.BatteryLevel

                @BytesLayout
                interface Outer {
                    val inner: Inner
                    val battery: BatteryLevel
                }
                """,
            ),
            generate = "VIEW",
            options = mapOf("commonSource" to workingDir.resolve("sources/common").absolutePath),
            workingDir = workingDir,
            classpaths = listOf(parts.result.outputDirectory),
        )

        // The common pass generates InnerView with its own BatteryLevel layout; this pass only nests one in Outer's.
        assertEquals(emptyList(), compilation.kspErrors, compilation.result.messages)
        assertEquals(listOf("OuterView.kt"), compilation.generatedFiles.map { it.name })
        val outer = compilation.generatedFile("OuterView.kt")
        assertTrue("= InnerSlice(b, " in outer && "    internal object BatteryLevelLayout {" in outer, outer)
    }

    @Test
    fun `reports DATA holding a layout of another module without DATA`() {
        assertEquals(KotlinCompilation.ExitCode.OK, parts.result.exitCode, parts.result.messages)
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Holder {
                    val counter: test.parts.Counter
                }
                """,
            ),
            generate = "DATA,VIEW",
            classpaths = listOf(parts.result.outputDirectory),
        )

        assertTrue(
            compilation.kspErrors.any { "the @BytesLayout test.parts.Counter of another module it uses must have generated CounterData there" in it },
            compilation.result.messages,
        )
    }
}
