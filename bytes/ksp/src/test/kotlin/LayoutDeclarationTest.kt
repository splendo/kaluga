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

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** How the processor reads declarations: types, rounds, source sets and annotation arguments. */
@OptIn(ExperimentalCompilerApi::class)
class LayoutDeclarationTest {

    // Checks every generated DATA layout against the runtime, as the example's LayoutParityTest does.
    private val parity = SourceFile.kotlin(
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

    private fun assertParity(compilation: BytesCompilation) {
        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val problems = compilation.result.classLoader.loadClass("test.ParityKt").getMethod("check").invoke(null) as List<*>
        assertEquals(emptyList<Any?>(), problems)
    }

    @Test
    fun `expands type aliases as kotlinx does`() = assertParity(
        BytesCompilation(
            layoutSource(
                """
                typealias MaybeInt = Int?
                typealias InnerAlias = Inner
                typealias Items<T> = List<T>
                typealias Pairs<K, V> = Map<K, Items<V>>

                @BytesLayout
                interface Inner {
                    val value: Byte
                }

                @BytesLayout
                interface Aliased {
                    val maybe: MaybeInt
                    val inner: InnerAlias
                    val inners: Items<InnerAlias>
                    val numbers: Items<MaybeInt>
                    val pairs: Pairs<Byte, Short>
                }
                """,
            ),
            parity,
        ),
    )

    @Test
    fun `shapes a value class wrapping a layout through the layout's interface serializer`() = assertParity(
        BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Inner {
                    val value: Byte
                }

                @Serializable
                @JvmInline
                value class Wrapper(@Serializable(with = InnerData.InterfaceSerializer::class) val inner: Inner)

                @BytesLayout
                interface Outer {
                    val wrapper: Wrapper
                }
                """,
            ),
            parity,
        ),
    )

    @Test
    fun `rejects a value class wrapping a layout that kotlinx serializes polymorphically`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Inner {
                    val value: Byte
                }

                @Serializable
                @JvmInline
                value class Wrapper(val inner: Inner)

                @BytesLayout
                interface Outer {
                    val wrapper: Wrapper
                }
                """,
            ),
        )

        assertTrue(compilation.kspErrors.any { "annotate its property with @Serializable(with = InnerData.InterfaceSerializer::class)" in it }, compilation.result.messages)
    }

    @Test
    fun `rejects the numbers kotlinx cannot serialize in DATA but views them`() {
        val source = """
            @BytesLayout(generate = [%s])
            interface Reading {
                val value: com.splendo.kaluga.bytes.integer.Int24
            }
            """
        val data = BytesCompilation(layoutSource(source.replace("%s", "Generate.DATA")), generate = "DATA,VIEW")
        assertTrue(data.kspErrors.any { "which cannot hold a com.splendo.kaluga.bytes.integer.Int24" in it }, data.result.messages)

        val view = BytesCompilation(layoutSource(source.replace("%s", "Generate.VIEW")), generate = "DATA,VIEW")
        assertEquals(KotlinCompilation.ExitCode.OK, view.result.exitCode, view.result.messages)
    }

    @Test
    fun `waits for a layout of a later round and then processes every layout with the whole index`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Other {
                    val value: Byte
                }

                @BytesLayout
                interface Holder {
                    val later: LaterInt
                    val other: Other
                }
                """,
            ),
            processors = listOf(LaterTypeProcessor.Provider),
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val registry = compilation.generatedFile("GeneratedBytesLayouts.kt")
        assertTrue("HolderData" in registry && "OtherData" in registry, registry)
    }

    @Test
    fun `honours an explicit generate equal to the annotation default`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout(generate = [Generate.DATA, Generate.VIEW])
                interface Explicit {
                    val value: Byte
                }

                @BytesLayout
                interface Implicit {
                    val value: Byte
                }
                """,
            ),
            generate = "DATA",
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        val files = compilation.generatedFiles.map { it.name }
        assertTrue("ExplicitView.kt" in files && "ImplicitView.kt" !in files, files.toString())
    }

    @Test
    fun `names the registries by the registry prefix`() {
        val compilation = BytesCompilation(
            layoutSource(
                """
                @BytesLayout
                interface Plain {
                    val value: Byte
                }
                """,
            ),
            generate = "DATA,VIEW",
            options = mapOf("registryPrefix" to "ModuleCommonMain"),
        )

        assertEquals(KotlinCompilation.ExitCode.OK, compilation.result.exitCode, compilation.result.messages)
        assertTrue("internal object ModuleCommonMainBytesLayouts" in compilation.generatedFile("ModuleCommonMainBytesLayouts.kt"))
        assertTrue("internal object ModuleCommonMainBytesViews" in compilation.generatedFile("ModuleCommonMainBytesViews.kt"))
    }

    @Test
    fun `refers to layouts of a common source set from a per-target pass`() {
        val workingDir = Files.createTempDirectory("bytes-ksp").toFile()
        val compilation = BytesCompilation(
            SourceFile.kotlin(
                "common/Common.kt",
                """
                package test

                import com.splendo.kaluga.bytes.annotations.BytesLayout

                @BytesLayout
                interface Inner {
                    val value: Byte
                }
                """.trimIndent(),
            ),
            layoutSource(
                """
                @BytesLayout
                interface Outer {
                    val inner: Inner
                }
                """,
            ),
            options = mapOf("commonSource" to workingDir.resolve("sources/common").absolutePath),
            workingDir = workingDir,
        )

        // The common pass generates InnerData, so only this pass's own layout is generated and nothing is reported.
        assertEquals(emptyList(), compilation.kspErrors, compilation.result.messages)
        val files = compilation.generatedFiles.map { it.name }
        assertTrue("OuterData.kt" in files && "InnerData.kt" !in files, files.toString())
    }

    @Test
    fun `reports what kotlinx and the processor cannot agree on`() {
        val cases = listOf(
            """
            @BytesLayout
            interface Outer {
                @BytesLayout
                interface Inner {
                    val value: Byte
                }
            }

            @BytesLayout
            interface OuterInner {
                val value: Byte
            }
            """ to "as test.Outer.Inner does; rename one of them",
            """
            @BytesLayout
            interface Named {
                val Companion: Byte
            }
            """ to "cannot be named Companion",
            """
            @BytesLayout
            @kotlinx.serialization.SerialName("named")
            interface Named {
                val value: Byte
            }
            """ to "cannot have a @SerialName",
            """
            @BytesLayout
            interface Texts {
                val text: CharSequence
            }
            """ to "layouts from another module are not supported yet",
        )
        cases.forEach { (source, message) ->
            val compilation = BytesCompilation(layoutSource(source))
            assertTrue(compilation.kspErrors.any { message in it }, compilation.result.messages)
        }
    }

    @Test
    fun `re-emits annotation arguments of every kind`() = assertParity(
        BytesCompilation(
            layoutSource(
                """
                annotation class Detail(val level: Int)

                @OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
                @kotlinx.serialization.SerialInfo
                @Target(AnnotationTarget.PROPERTY)
                annotation class Tag(
                    val type: kotlin.reflect.KClass<*>,
                    val detail: Detail,
                    val shorts: ShortArray,
                    val flags: BooleanArray,
                    val chars: CharArray,
                    val quote: Char,
                    val ratio: Double,
                    val scale: Float,
                    val names: Array<String>,
                )

                @BytesLayout
                interface Tagged {
                    @Tag(String::class, Detail(3), [1, 2], [true, false], ['a', '\n'], '\'', Double.NaN, Float.POSITIVE_INFINITY, ["a", "b"])
                    val value: Byte
                }
                """,
            ),
            parity,
        ),
    )
}

/** Generates `typealias LaterInt = Int` in its first round, so a layout using it waits for the second. */
private class LaterTypeProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private var done = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!done) {
            done = true
            environment.codeGenerator.createNewFile(Dependencies(false), "test", "LaterInt").bufferedWriter().use { it.write("package test\n\ntypealias LaterInt = Int\n") }
        }
        return emptyList()
    }

    object Provider : SymbolProcessorProvider {
        override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = LaterTypeProcessor(environment)
    }
}
