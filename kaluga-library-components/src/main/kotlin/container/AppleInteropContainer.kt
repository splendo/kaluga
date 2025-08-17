/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.plugin.container

import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import org.jetbrains.kotlin.gradle.plugin.mpp.DefaultCInteropSettings
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import javax.inject.Inject

open class AppleInteropContainer @Inject constructor(
    objects: ObjectFactory,
) {
    internal val main = mutableListOf<Action<NamedDomainObjectContainer<DefaultCInteropSettings>>>()
    internal val test = mutableListOf<Action<NamedDomainObjectContainer<DefaultCInteropSettings>>>()

    fun main(action: Action<NamedDomainObjectContainer<DefaultCInteropSettings>>) {
        main.add(action)
    }

    fun test(action: Action<NamedDomainObjectContainer<DefaultCInteropSettings>>) {
        test.add(action)
    }
}

abstract class BuildSwiftLibTask @Inject constructor(
    private val execOps: ExecOperations,
    objects: ObjectFactory
) : DefaultTask() {

    @get:InputDirectory
    abstract val srcDir: DirectoryProperty

    @get:OutputDirectory
    abstract val libraryOutputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val headerOutputDir: DirectoryProperty

    @get:Input
    abstract val sdkName: Property<String>

    @get:Input
    abstract val deploymentTarget: Property<String>

    @TaskAction
    fun build() {
        val sdk = sdkName.get()
        val libraryOutputDir = libraryOutputDir.asFile.get()
        libraryOutputDir.mkdirs()

        val libFile = libraryOutputDir.resolve("libswiftinterop.a")

        val headerOutputDir = headerOutputDir.asFile.get()
        headerOutputDir.mkdirs()
        val headerFileTemp = headerOutputDir.resolve("SwiftInterop-Temp.h")
        val headerFile = headerOutputDir.resolve("SwiftInterop.h")

        val sdkPath = execAndCapture(listOf("xcrun", "--sdk", sdk, "--show-sdk-path")).trim()
        val targetTriple = when (sdk) {
            "iphoneos" -> "arm64-apple-ios${deploymentTarget.get()}"
            "iphonesimulator" -> "arm64-apple-ios${deploymentTarget.get()}-simulator"
            else -> error("Unsupported sdk $sdk")
        }

        val swiftFiles = srcDir.asFileTree.matching { include("**/*.swift") }.files.toList()

        execOps.exec {
            commandLine(
                "xcrun", "swiftc",
                "-emit-library",
                "-static",
                "-o", libFile.absolutePath,
                "-sdk", sdkPath,
                "-target", targetTriple,
                "-emit-objc-header",
                "-emit-objc-header-path", headerFileTemp.absolutePath,
                "-Xlinker", "-no_implicit_dylibs",
            )
            args(swiftFiles.map { it.absolutePath })
        }.assertNormalExitValue()

        // SwiftC generated Header file does not import dependencies.
        val frameworkRegex = Regex("""import\s+(\w+)""")
        val frameworksToImport = swiftFiles.fold(setOf("Foundation")) { acc, file ->
            println("File ${file.absolutePath}")
            val result = acc.toMutableSet()
            file.forEachLine { line ->
                println("Line $line")
                frameworkRegex.find(line)?.let { match ->
                    println("MATCH!!")
                    result += match.groupValues[1]
                }
            }
            result
        }

        val writer = BufferedWriter(OutputStreamWriter(FileOutputStream(headerFile)))
        try {
            headerFileTemp.forEachLine { line ->
                if (line == "#include <Foundation/Foundation.h>") {
                    frameworksToImport.forEach { framework ->
                        writer.appendLine("#include <$framework/$framework.h>")
                    }
                } else {
                    writer.appendLine(line)
                }
            }
        } finally {
            writer.close()
        }

        headerFileTemp.delete()
    }

    private fun execAndCapture(command: List<String>): String {
        val output = ByteArrayOutputStream()
        execOps.exec {
            commandLine = command
            standardOutput = output
        }.assertNormalExitValue()
        return output.toString()
    }
}
