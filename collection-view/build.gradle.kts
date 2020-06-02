/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
}

apply(from = "../gradle/publishable_component.gradle")

val ext = (gradle as ExtensionAware).extra
group = "com.splendo.kaluga"
version = ext["library_version"]!!

android {
    packagingOptions {
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                val ext = (gradle as ExtensionAware).extra
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                api(project(":architecture", ""))
            }
        }
    }
}
