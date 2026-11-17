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

package com.splendo.kaluga.bluetooth.ksp

import com.splendo.kaluga.bytes.ksp.BytesSymbolProcessorProvider
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlinx.serialization.compiler.extensions.SerializationComponentRegistrar
import java.io.ByteArrayOutputStream
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

/** Bluetooth values typed by a layout compiled into another module, which generated what is on the classpath. */
@OptIn(ExperimentalCompilerApi::class)
class CrossModuleLayoutTest {

    private val layouts: JvmCompilationResult = KotlinCompilation().apply {
        sources = listOf(
            SourceFile.kotlin(
                "Layouts.kt",
                """
                package test.layouts

                import com.splendo.kaluga.bytes.annotations.*

                @BytesLayout
                interface Viewed {
                    @Size(Length.`8_BIT`)
                    @Unsigned
                    val level: Int
                }

                @BytesLayout(generate = [Generate.DATA])
                interface Serialized {
                    @Size(Length.`8_BIT`)
                    @Unsigned
                    val level: Int
                }
                """.trimIndent(),
            ),
        )
        inheritClassPath = true
        messageOutputStream = ByteArrayOutputStream()
        compilerPluginRegistrars = listOf(SerializationComponentRegistrar())
        useKsp2()
        symbolProcessorProviders += BytesSymbolProcessorProvider()
        kspProcessorOptions += "generate" to "DATA,VIEW"
    }.compile()

    @Test
    fun `a layout of another module is read through what that module generated`() {
        assertEquals(KotlinCompilation.ExitCode.OK, layouts.exitCode, layouts.messages)
        val compilation = BluetoothCompilation(
            bluetoothSource(
                """
                @BluetoothCharacteristic("2a19")
                interface LevelCharacteristic {
                    @Readable
                    @Notifiable
                    val viewed: test.layouts.Viewed

                    @Writable
                    val serialized: test.layouts.Serialized
                }
                """,
            ),
            // This module's own default does not apply to the other module's layouts.
            options = mapOf("generate" to "DATA"),
            classpaths = listOf(layouts.outputDirectory),
        )
        assertEquals(emptyList(), compilation.kspErrors)
        val remote = compilation.generatedFile("RemoteLevelCharacteristic.kt")
        assertContains(remote, "public val viewed: Flow<ViewedView>")
        assertContains(remote, "import test.layouts.ViewedView")
        assertContains(remote, "format.encodeToByteArray(SerializedData.InterfaceSerializer, serialized)")
    }
}
