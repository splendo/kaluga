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

package com.splendo.kaluga.bluetooth.plugin

import com.google.devtools.ksp.gradle.KspGradleSubplugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Properties

class BluetoothPlugin : Plugin<Project> {

    override fun apply(target: Project) = target.run {
        plugins.apply(KotlinMultiplatformPluginWrapper::class)
        plugins.apply(KspGradleSubplugin::class)

        val kalugaVersion = BluetoothPluginVersion.kalugaVersion

        extensions.configure<KotlinMultiplatformExtension> {
            project.dependencies.add(
                "kspCommonMainMetadata",
                "com.splendo.kaluga:bluetooth-ksp:$kalugaVersion"
            )
            targets.configureEach {
                if (name !in listOf("metadata")) {
                    project.dependencies.add(
                        "ksp${name.uppercaseFirstChar()}",
                        "com.splendo.kaluga:bluetooth-ksp:$kalugaVersion"
                    )
                }
            }
            sourceSets.commonMain.dependencies {
                implementation("com.splendo.kaluga:bluetooth-annotations:$kalugaVersion")
                implementation("com.splendo.kaluga:bluetooth:$kalugaVersion")
            }
        }
    }
}

object BluetoothPluginVersion {
    val kalugaVersion: String by lazy {
        BluetoothPluginVersion::class.java
            .classLoader
            .getResourceAsStream("bluetooth.properties")
            ?.use {
                println("Loading version.properties")
                Properties().apply { load(it) }
            }
            ?.getProperty("kalugaVersion")
            ?: error("Bluetooth plugin version not found")
    }
}