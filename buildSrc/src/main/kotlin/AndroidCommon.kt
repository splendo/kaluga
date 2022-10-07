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

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies

enum class ComponentType {
    DEFAULT,
    COMPOSE,
    APP,
    COMPOSE_APP
}

fun org.gradle.api.Project.commonAndroidComponent(type: ComponentType = ComponentType.DEFAULT) {
    val action = object : Action<LibraryExtension> {
        override fun execute(t: LibraryExtension) {
            t.androidCommon(type)
        }
    }
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("android", action)
    dependencies {

    }
}

fun LibraryExtension.androidCommon(componentType: ComponentType = ComponentType.DEFAULT) {
    compileSdk = Library.Android.compileSdk
    buildToolsVersion = Library.Android.buildTools

    defaultConfig {
        minSdk = Library.Android.minSdk
        targetSdk = Library.Android.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    when (componentType) {
        ComponentType.APP,
        ComponentType.COMPOSE_APP -> {
            println("Android sourcesets for this project module are configured using defaults (for an app)")
        }
        ComponentType.COMPOSE,
        ComponentType.DEFAULT -> {
            println("Android sourcesets for this project module are configured as a library")
            sourceSets {
                getByName("main") {
                    manifest.srcFile("src/androidLibMain/AndroidManifest.xml")
                    res.srcDir("src/androidLibMain/res")
                    if (componentType == ComponentType.COMPOSE) {
                        java.srcDir("src/androidLibMain/kotlin")
                    }
                }
                getByName("androidTest") {
                    manifest.srcFile("src/androidLibAndroidTest/AndroidManifest.xml")
                    java.srcDir("src/androidLibAndroidTest/kotlin")
                    res.srcDir("src/androidLibAndroidTest/res")
                }

                getByName("test") {
                    java.srcDir("src/androidLibUnitTest/kotlin")
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    when (componentType) {
        ComponentType.COMPOSE,
        ComponentType.COMPOSE_APP -> {
            println("This project module is a Compose only module")
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = Library.Android.composeCompiler
            }
        }
        ComponentType.DEFAULT,
        ComponentType.APP -> {}
    }
}
