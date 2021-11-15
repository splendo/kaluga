/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, softwarfe
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("convention.publication")
    id("com.android.library")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                val serializationVersion = ext["serialization_version"]
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                implementation(project(":architecture", ""))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-properties:$serializationVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(project(":test-utils", ""))
            }
        }
    }
}
