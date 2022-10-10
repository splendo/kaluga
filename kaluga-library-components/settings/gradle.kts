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

pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    // resolutionStrategy {
    //     eachPlugin {
    //
    //         val kalugaAndroidGradlePluginVersion =
    //             settings.extra["kaluga.androidGradlePluginVersion"]
    //         val kalugaKotlinVersion = settings.extra["kaluga.kotlinVersion"]
    //         val kalugaKtLintGradlePluginVersion = settings.extra["kaluga.ktLintGradlePluginVersion"]
    //         val kalugaGoogleServicesGradlePluginVersion = settings.extra["kaluga.googleServicesGradlePluginVersion"]
    //         val kalugaBinaryCompatibilityValidatorVersion = settings.extra["kaluga.binaryCompatibilityValidatorVersion"]
    //
    //         when (requested.id.id) {
    //             "org.jetbrains.kotlin.multiplatform",
    //             "org.jetbrains.kotlin.plugin.serialization",
    //             "org.jetbrains.kotlin.android",
    //             "org.jetbrains.kotlin.kapt",
    //             -> useVersion("$kalugaKotlinVersion")
    //             "com.android.library",
    //             "com.android.application",
    //             -> useVersion("$kalugaAndroidGradlePluginVersion")
    //             "org.jlleitschuh.gradle.ktlint",
    //             "org.jlleitschuh.gradle.ktlint-idea",
    //             -> useVersion("$kalugaKtLintGradlePluginVersion")
    //             "com.google.gms:google-services"
    //             -> useVersion("com.google.gms:google-services:$kalugaGoogleServicesGradlePluginVersion")
    //             "org.jetbrains.kotlinx.binary-compatibility-validator"
    //             -> useVersion("$kalugaBinaryCompatibilityValidatorVersion")
    //         }
    //     }
    // }
}
