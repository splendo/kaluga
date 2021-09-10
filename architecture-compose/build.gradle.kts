/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
    id("com.android.library")
    kotlin("android")
    id("jacoco")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra
ext["component_type"] = ext["component_type_compose"]

// if the include is made from the example project shared module we need to go up one more directory
val path_prefix = if (file("../gradle/componentskt.gradle.kts").exists())
    ".." else "../.."

apply(from = "$path_prefix/gradle/android_compose.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

ext["component_type"] = ext["component_type_default"]

dependencies {
    api(project(":architecture"))
    val ext = (gradle as ExtensionAware).extra
    implementation("androidx.compose.foundation:foundation:" + ext["androidx_compose_version"])
    implementation("androidx.compose.ui:ui:" + ext["androidx_compose_version"])
    implementation("androidx.compose.ui:ui-tooling:" + ext["androidx_compose_version"])
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:" + ext["androidx_lifecycle_viewmodel_compose_version"])
    implementation("androidx.activity:activity-compose:" + ext["androidx_activity_compose_version"])
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${ext["kotlinx_coroutines_version"]}!!")
}
