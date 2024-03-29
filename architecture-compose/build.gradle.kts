/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
    kotlin("plugin.serialization")
    id("jacoco")
    id("convention.publication")
    id("org.jetbrains.dokka")
    id("org.jmailen.kotlinter")
}

composeAndroidComponent("architecture.compose")

dependencies {
    api(project(":base"))
    api(project(":architecture"))
    implementationDependency(Dependencies.AndroidX.Compose.Material)
    implementationDependency(Dependencies.AndroidX.Navigation.Compose)
    implementationDependency(Dependencies.KotlinX.Coroutines.Core)
    implementationDependency(Dependencies.AndroidX.Browser)
}
