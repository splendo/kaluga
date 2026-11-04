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

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Runs [BluetoothSymbolProcessor] over [sources] with KSP2 and reads back what it generated. The Bluetooth runtime is
 * not on the JVM, so the generated code is not compiled here; the example build compiles it.
 */
@OptIn(ExperimentalCompilerApi::class)
internal class BluetoothCompilation(
    vararg sources: SourceFile,
    options: Map<String, String> = emptyMap(),
    processors: List<SymbolProcessorProvider> = emptyList(),
    classpaths: List<File> = emptyList(),
) {

    private val compilation = KotlinCompilation().apply {
        this.sources = sources.toList()
        inheritClassPath = true
        this.classpaths = classpaths
        messageOutputStream = ByteArrayOutputStream()
        useKsp2()
        symbolProcessorProviders += BluetoothSymbolProcessorProvider()
        symbolProcessorProviders += processors
        kspProcessorOptions += mapOf("target" to "CLIENT,SERVER", "implementFor" to "BLUETOOTH,SIMULATOR,MOCK") + options
    }

    private val result = compilation.compile()

    fun generatedFile(name: String): String = compilation.kspSourcesDir.walkTopDown().firstOrNull { it.name == name }?.readText()
        ?: error("No $name among ${compilation.kspSourcesDir.walkTopDown().filter { it.isFile }.map { it.name }.toList()}")

    /** Every generated source, one after the other. */
    val generatedSources: String get() = compilation.kspSourcesDir.walkTopDown().filter { it.isFile }.joinToString("\n") { it.readText() }

    /** The errors KSP reported. */
    val kspErrors: List<String> get() = result.messages.lines().filter { it.startsWith("e: [ksp]") }
}

/** A source file in package `test` with the imports every fixture here uses. */
internal fun bluetoothSource(body: String): SourceFile = SourceFile.kotlin(
    "Definitions.kt",
    """
    package test

    import com.splendo.kaluga.bluetooth.annotations.*
    import com.splendo.kaluga.bytes.annotations.BytesLayout
    import com.splendo.kaluga.bytes.annotations.ByteStuffed
    import com.splendo.kaluga.bytes.annotations.Generate
    import com.splendo.kaluga.bytes.annotations.Length
    import com.splendo.kaluga.bytes.annotations.SerializedByteValue
    import com.splendo.kaluga.bytes.annotations.Size
    import com.splendo.kaluga.bytes.annotations.Unsigned
    import kotlinx.serialization.Serializable

    """.trimIndent() + "\n" + body.trimIndent(),
)
