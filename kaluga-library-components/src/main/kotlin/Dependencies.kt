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

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

data class Dependency(val group: String, val name: String, val version: String? = null) {
    val notation = "$group:$name${version?.let { ":$it" } ?: ""}"
}

typealias ModuleDependencyHandler = ModuleDependency.() -> Unit

fun KotlinDependencyHandler.apiDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) = api(dependency.notation) {
    handler?.invoke(this)
}
fun KotlinDependencyHandler.implementationDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) = implementation(dependency.notation) {
    handler?.invoke(this)
}

fun DependencyHandler.apiDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) {
    add("api", dependency.notation).apply {
        (this as? ModuleDependency)?.let {
            handler?.invoke(it)
        }
    }
}
fun DependencyHandler.implementationDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) {
    add("implementation", dependency.notation).apply {
        (this as? ModuleDependency)?.let {
            handler?.invoke(it)
        }
    }
}
fun DependencyHandler.debugImplementationDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) {
    add("debugImplementation", dependency.notation).apply {
        (this as? ModuleDependency)?.let {
            handler?.invoke(it)
        }
    }
}
fun DependencyHandler.testImplementationDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) {
    add("testImplementation", dependency.notation).apply {
        (this as? ModuleDependency)?.let {
            handler?.invoke(it)
        }
    }
}
fun DependencyHandler.androidTestImplementationDependency(dependency: Dependency, handler: ModuleDependencyHandler? = null) {
    add("androidTestImplementation", dependency.notation).apply {
        (this as? ModuleDependency)?.let {
            handler?.invoke(it)
        }
    }
}

object Dependencies {

    object Kotlin {
        private const val group = "org.jetbrains.kotlin"
        val Test = Dependency(group, "kotlin-test")
        val JUnit = Dependency(group, "kotlin-test-junit")
    }

    object KotlinX {

        private const val group = "org.jetbrains.kotlinx"

        object Coroutines {
            private const val version = "1.7.3"
            val Android = Dependency(group, "kotlinx-coroutines-android", version)
            val Core = Dependency(group, "kotlinx-coroutines-core", version)
            val Swing = Dependency(group, "kotlinx-coroutines-swing", version)
            val Js = Dependency(group, "kotlinx-coroutines-core-js", version)
            val PlayServices = Dependency(group, "kotlinx-coroutines-play-services", version)
            val Test = Dependency(group, "kotlinx-coroutines-test", version)
            val Debug = Dependency(group, "kotlinx-coroutines-debug", version)
        }

        object Serialization {
            private const val version =  "1.6.0"
            val Core = Dependency(group, "kotlinx-serialization-core", version)
            val Json = Dependency(group, "kotlinx-serialization-json", version)
        }

        val AtomicFu = Dependency(group, "atomicfu", "0.22.0")
    }

    object Accompanist {
        private const val groupBase = "com.google.accompanist"
        private const val version = "0.32.0"

        val DrawablePainter = Dependency(groupBase, "accompanist-drawablepainter", version)
        val MaterialThemeAdapter = Dependency(groupBase, "accompanist-themeadapter-material", version)
    }

    object Android {
        internal const val groupBase = "com.google.android"
        private const val materialBase = "$groupBase.material"

        val Material = Dependency(materialBase, "material", "1.9.0")

        object Play {
            private const val group = "$groupBase.play"
            val Core = Dependency(group, "core", "1.10.3")
            val CoreKtx = Dependency(group, "core-ktx", "1.8.1")
        }
        object PlayServices {
            private const val group = "$groupBase.gms"
            private const val version = "21.0.1"
            val Location = Dependency(group, "play-services-location", version)
        }
    }

    object AndroidX {

        private const val groupBase = "androidx"
        private const val fragmentGroup = "$groupBase.fragment"
        private const val fragmentVersion = "1.5.7"

        object Activity {
            private const val group = "$groupBase.activity"
            private const val version = "1.7.2"
            val Activity = Dependency(group, "activity", version)
            val Ktx = Dependency(group, "activity-ktx", version)
            val Compose = Dependency(group, "activity-compose", version)
        }
        val AppCompat = Dependency("$groupBase.appcompat", "appcompat", "1.6.1")
        val ArchCore = Dependency("$groupBase.arch.core", "core-testing", "2.2.0")
        val Browser = Dependency("$groupBase.browser", "browser", "1.6.0")
        object Compose {
            private const val version = "1.5.1"
            private const val uiVersion = "1.5.1"
            private const val composeGroupBase = "$groupBase.compose"

            val Foundation = Dependency("$composeGroupBase.foundation", "foundation", version)
            val Material = Dependency("$composeGroupBase.material", "material", version)
            val UI = Dependency("$composeGroupBase.ui", "ui", uiVersion)
            val UITooling = Dependency("$composeGroupBase.ui", "ui-tooling", uiVersion)
            val UIToolingPreview = Dependency("$composeGroupBase.ui", "ui-tooling-preview", uiVersion)
        }
        val ConstraintLayout = Dependency("$groupBase.constraintlayout", "constraintlayout", "2.1.4")
        val Core = Dependency("$groupBase.core", "core", "1.12.0")
        val Fragment = Dependency(fragmentGroup, "fragment", fragmentVersion)
        val FragmentKtx = Dependency(fragmentGroup, "fragment-ktx", fragmentVersion)
        object Lifecycle {
            private const val group = "$groupBase.lifecycle"
            private const val version = "2.6.2"

            val LiveData = Dependency(group, "lifecycle-livedata-ktx", version)
            val Runtime = Dependency(group, "lifecycle-runtime-ktx", version)
            val Service = Dependency(group, "lifecycle-service", version)
            val ViewModel = Dependency(group, "lifecycle-viewmodel-ktx", version)
            val ViewModelCompose = Dependency(group, "lifecycle-viewmodel-compose", "2.6.2")
        }
        object Navigation {
            private const val group = "$groupBase.navigation"
            private const val version = "2.7.2"

            val Compose = Dependency(group, "navigation-compose", version)
        }
        object Test {
            private const val group = "$groupBase.test"
            private const val versionPostfix = ""
            private const val version = "1.5.0$versionPostfix"
            private const val runnerVersion = "1.5.2$versionPostfix"
            private const val espressoVersion = "3.5.1$versionPostfix"
            private const val junitVersion = "1.1.5$versionPostfix"
            private const val uiAutomatorVersion = "2.2.0"

            val Core = Dependency(group, "core", version)
            val CoreKtx = Dependency(group, "core-ktx", version)
            val Espresso = Dependency("$group.espresso", "espresso-core", espressoVersion)
            val JUnit = Dependency("$group.ext", "junit", junitVersion)
            val Rules = Dependency(group, "rules", version)
            val Runner = Dependency(group, "runner", runnerVersion)
            val UIAutomator = Dependency("$group.uiautomator", "uiautomator", uiAutomatorVersion)
        }
    }

    val BLEScanner = Dependency("no.nordicsemi.android.support.v18", "scanner", "1.6.0")

    object Koin {
        private const val group = "io.insert-koin"
        private const val version = "3.5.0"
        private const val androidVersion = "3.5.0"
        private const val composeVersion = "3.5.0"
        val Android = Dependency(group, "koin-android", androidVersion)
        val AndroidXCompose = Dependency(group, "koin-androidx-compose", composeVersion)
        val Core = Dependency(group, "koin-core", version)
    }

    val Napier = Dependency("io.github.aakira", "napier", "2.6.1")

    val JUnit = Dependency("junit", "junit", "4.13.2")

    object Mockito {
        private const val group = "org.mockito"
        private const val version = "5.3.1"

        val Android = Dependency(group, "mockito-android", version)
        val Core = Dependency(group, "mockito-core", version)
    }

    object ByteBuddy {
        private const val group = "net.bytebuddy"
        private const val version = "1.14.4"

        val Android = Dependency(group, "byte-buddy-android", version)
        val Agent = Dependency(group, "byte-buddy-agent", version)
    }
}
