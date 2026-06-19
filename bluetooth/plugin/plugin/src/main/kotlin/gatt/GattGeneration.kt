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

package com.splendo.kaluga.bluetooth.plugin.gatt

import com.squareup.kotlinpoet.FileSpec
import java.io.File

/** Parses a set of GATT XML files and generates the Kaluga `@Bluetooth` definitions for a device. */
object GattGeneration {

    /** Parses [xmlFiles] (characteristics and services) and generates all definitions for [deviceName] in [packageName]. */
    fun generate(xmlFiles: List<File>, deviceName: String, packageName: String, useScientificUnits: Boolean = false): List<FileSpec> {
        val definitions = xmlFiles.filter { it.extension.equals("xml", ignoreCase = true) }.map { GattXmlParser.parse(it) }
        val characteristics = definitions.filterIsInstance<GattDefinition.Characteristic>().map { it.value }
        val services = definitions.filterIsInstance<GattDefinition.Service>().map { it.value }
        return BluetoothDefinitionGenerator(packageName, useScientificUnits).generate(deviceName, services, characteristics)
    }

    /** Generates definitions from the XML under [sourceDirectories] and writes them under [outputDirectory] (cleared first). */
    fun generateTo(outputDirectory: File, sourceDirectories: List<File>, deviceName: String, packageName: String, useScientificUnits: Boolean = false) {
        val xmlFiles = sourceDirectories.flatMap { dir -> dir.walkTopDown().filter { it.isFile } }
        writeTo(outputDirectory, generate(xmlFiles, deviceName, packageName, useScientificUnits))
    }

    private fun writeTo(outputDirectory: File, definitions: List<FileSpec>) {
        outputDirectory.deleteRecursively()
        outputDirectory.mkdirs()
        definitions.forEach { it.writeTo(outputDirectory) }
    }
}
