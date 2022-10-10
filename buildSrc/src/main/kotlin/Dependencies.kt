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

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

data class Dependency(private val group: String, private val name: String, private val version: String? = null) {
    val notation = "$group:$name${version?.let { ":$it" } ?: ""}"
}

fun KotlinDependencyHandler.expose(dependency: Dependency) = api(dependency.notation)
fun KotlinDependencyHandler.implement(dependency: Dependency) = implementation(dependency.notation)

fun DependencyHandler.expose(dependency: Dependency) {
    add("api", dependency.notation)
}
fun DependencyHandler.implement(dependency: Dependency) {
    add("implementation", dependency.notation)
}
fun DependencyHandler.implementForDebug(dependency: Dependency) {
    add("debugImplementation", dependency.notation)
}
fun DependencyHandler.implementForTest(dependency: Dependency) {
    add("testImplementation", dependency.notation)
}
fun DependencyHandler.implementForAndroidTest(dependency: Dependency) {
    add("androidTestImplementation", dependency.notation)
}

object Dependencies {

    object Kotlin {
        private const val group = "org.jetbrains.kotlin"
        val Test = Dependency(group, "kotlin-test")
        val JUnit = Dependency(group, "kotlin-test-junit")
    }

    object KotlinX {
        object Coroutines {
            private const val version = "1.6.3-native-mt"
            private const val group = "org.jetbrains.kotlinx"
            val Android = Dependency(group, "kotlinx-coroutines-android", version)
            val Core = Dependency(group, "kotlinx-coroutines-core", version)
            val Swing = Dependency(group, "kotlinx-coroutines-swing", version)
            val Js = Dependency(group, "kotlinx-coroutines-js", version)
            val PlayServices = Dependency(group, "kotlinx-coroutines-play-services", version)
            val Test = Dependency(group, "kotlinx-coroutines-test", version)
            val Debug = Dependency(group, "kotlinx-coroutines-debug", version)
        }

        object Serialization {
            private const val version =  "1.4.0"
            private const val group = "org.jetbrains.kotlinx"
            val Core = Dependency(group, "kotlinx-serialization-core", version)
            val Json = Dependency(group, "kotlinx-serialization-json", version)
        }
    }

    object Android {
        internal const val groupBase = "com.google.android"
        private const val materialBase = "$groupBase.material"

        val Material = Dependency(materialBase, "material", "1.6.1")
        val MaterialComposeThemeAdapter = Dependency(materialBase, "compose-theme-adapter", "1.1.17")

        object Play {
            private const val group = "$groupBase.play"
            val Core = Dependency(group, "core", "1.10.3")
            val CoreKtx = Dependency(group, "core-ktx", "1.8.1")
        }
        object PlayServices {
            private const val group = "$groupBase.gms"
            private const val version = "20.0.0"
            val Location = Dependency(group, "play-services-location", version)
        }
    }

    object AndroidX {

        private const val groupBase = "androidx"
        private const val fragmentGroup = "$groupBase.fragment"
        private const val fragmentVersion = "1.5.3"

        object Activity {
            private const val group = "$groupBase.activity"
            val Compose = Dependency(group, "activity-compose", "1.6.0")
        }
        val AppCompat = Dependency("$groupBase.appcompat", "appcompat", "1.5.1")
        val ArchCore = Dependency("$groupBase.arch.core", "core-testing", "2.1.0")
        val Browser = Dependency("$groupBase.browser", "browser", "1.4.0")
        object Compose {
            private const val version = "1.2.1"
            private const val composeGroupBase = "$groupBase.compose"

            val Foundation = Dependency("$composeGroupBase.foundation", "foundation", version)
            val Material = Dependency("$composeGroupBase.material", "material", version)
            val UI = Dependency("$composeGroupBase.ui", "ui", version)
            val UITooling = Dependency("$composeGroupBase.ui", "ui-tooling", version)
        }
        val ConstraintLayout = Dependency("$groupBase.constraintlayout", "constraintlayout", "2.1.4")
        val Core = Dependency("$groupBase.core", "core", "1.9.0")
        val Fragment = Dependency(fragmentGroup, "fragment", fragmentVersion)
        val FragmentKtx = Dependency(fragmentGroup, "fragment-ktx", fragmentVersion)
        object Lifecycle {
            private const val group = "$groupBase.lifecycle"
            private const val version = "2.5.1"

            val LiveData = Dependency(group, "lifecycle-livedata-ktx", version)
            val Runtime = Dependency(group, "lifecycle-runtime-ktx", version)
            val Service = Dependency(group, "lifecycle-service", version)
            val ViewModel = Dependency(group, "lifecycle-viewmodel-ktx", version)
            val ViewModelCompose = Dependency(group, "lifecycle-viewmodel-compose", "2.5.1")
        }
        object Navigation {
            private const val group = "$groupBase.navigation"
            private const val version = "2.5.2"

            val Compose = Dependency(group, "navigation-compose", version)
        }
        object Test {
            private const val group = "$groupBase.test"
            private const val versionPostfix = ""
            private const val version = "1.4.0$versionPostfix"
            private const val espressoVersion = "3.4.0$versionPostfix"
            private const val junitVersion = "1.1.3$versionPostfix"
            private const val uiAutomatorVersion = "2.2.0"

            val Core = Dependency(group, "core", version)
            val CoreKtx = Dependency(group, "core-ktx", version)
            val Espresso = Dependency("$group.espresso", "espresso-core", version)
            val JUnit = Dependency("$group.ext", "junit", junitVersion)
            val Rules = Dependency(group, "rules", version)
            val Runner = Dependency(group, "runner", version)
            val UIAutomator = Dependency("$group.uiautomator", "uiautomator", uiAutomatorVersion)
        }
    }

    val BLEScanner = Dependency("no.nordicsemi.android.support.v18", "scanner", "1.6.0")

    object Koin {
        private const val group = "io.insert-koin"
        private const val version = "3.2.2"
        val Android = Dependency(group, "koin-android", version)
        val Core = Dependency(group, "koin-core", version)
    }

    val Napier = Dependency("io.github.aakira", "napier", "2.4.0")

    object Stately {
        private const val group = "co.touchlab"
        private const val version = "1.2.3"
        private const val isolateVersion = "1.2.3"
        val Common = Dependency(group, "stately-common", version)
        val Concurrency = Dependency(group, "stately-concurrency", version)
        val Isolate = Dependency(group, "stately-isolate", isolateVersion)
        val IsoCollections = Dependency(group, "stately-iso-collections", isolateVersion)
    }

    val JUnit = Dependency("junit", "junit", "4.13.2")

    object Mockito {
        private const val group = "org.mockito"
        private const val version = "3.11.2"

        val Android = Dependency(group, "mockito-android", version)
        val Core = Dependency(group, "mockito-core", version)
    }

    object ByteBuddy {
        private const val group = "net.bytebuddy"
        private const val version = "1.11.3"

        val Android = Dependency(group, "byte-buddy-android", version)
        val Agent = Dependency(group, "byte-buddy-agent", version)
    }
}
