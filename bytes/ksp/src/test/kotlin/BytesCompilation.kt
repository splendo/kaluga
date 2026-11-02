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

import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspProcessorOptions
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlinx.serialization.compiler.extensions.SerializationComponentRegistrar
import java.io.ByteArrayOutputStream
import java.io.File

/** A KSP2 compilation of [sources] through [BytesSymbolProcessor], with the kotlinx serialization plugin applied. */
@OptIn(ExperimentalCompilerApi::class)
internal class BytesCompilation(
    vararg sources: SourceFile,
    generate: String = "DATA",
    skipUnsupportedViews: Boolean = false,
    options: Map<String, String> = emptyMap(),
    workingDir: File? = null,
    processors: List<SymbolProcessorProvider> = emptyList(),
) {

    private val compilation = KotlinCompilation().apply {
        workingDir?.let { this.workingDir = it }
        this.sources = sources.toList()
        inheritClassPath = true
        messageOutputStream = ByteArrayOutputStream()
        compilerPluginRegistrars = listOf(SerializationComponentRegistrar())
        useKsp2()
        symbolProcessorProviders += BytesSymbolProcessorProvider()
        kspProcessorOptions += "generate" to generate
        if (skipUnsupportedViews) kspProcessorOptions += "skipUnsupportedViews" to "true"
        kspProcessorOptions += options
        symbolProcessorProviders += processors
    }

    val result: JvmCompilationResult = compilation.compile()

    fun generatedFile(name: String): String = compilation.kspSourcesDir.walkTopDown().first { it.name == name }.readText()

    val generatedFiles: List<File> get() = compilation.kspSourcesDir.walkTopDown().filter { it.isFile }.toList()

    /** The errors KSP reported. */
    val kspErrors: List<String> get() = result.messages.lines().filter { it.startsWith("e: [ksp]") }
}

/** A source file in package `test` with the imports every fixture here uses. */
internal fun layoutSource(body: String): SourceFile = SourceFile.kotlin(
    "Layouts.kt",
    """
    package test

    import com.splendo.kaluga.bytes.ByteOrder
    import com.splendo.kaluga.bytes.strings.Encoding
    import com.splendo.kaluga.bytes.annotations.*
    import kotlinx.serialization.Serializable

    """.trimIndent() + "\n" + body.trimIndent(),
)
