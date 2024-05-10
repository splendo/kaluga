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
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies

fun org.gradle.api.Project.commonAndroidComponent(type: ComponentType = ComponentType.Default, packageName: String) {
    androidLibrary {
        androidCommon(this@commonAndroidComponent, type, packageName)
    }

    dependencies {
        implementationDependency(Dependencies.KotlinX.Coroutines.Android)
        implementationDependency(Dependencies.AndroidX.AppCompat)
        implementationDependency(Dependencies.AndroidX.Activity.Ktx)
        implementationDependency(Dependencies.AndroidX.Lifecycle.ViewModel)
        implementationDependency(Dependencies.AndroidX.Navigation.FragmentKtx)

        testImplementationDependency(Dependencies.JUnit)
        testImplementationDependency(Dependencies.Mockito.Core)
        testImplementationDependency(Dependencies.ByteBuddy.Agent)
        testImplementationDependency(Dependencies.Kotlin.Test)
        testImplementationDependency(Dependencies.Kotlin.JUnit)

        androidTestImplementationDependency(Dependencies.Mockito.Core)
        androidTestImplementationDependency(Dependencies.Mockito.Android)
        androidTestImplementationDependency(Dependencies.ByteBuddy.Android)
        androidTestImplementationDependency(Dependencies.ByteBuddy.Agent)

        androidTestImplementationDependency(Dependencies.AndroidX.Test.Core)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.CoreKtx)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.UIAutomator)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.Rules)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.JUnit)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.Runner)
        androidTestImplementationDependency(Dependencies.AndroidX.Test.Espresso)
        androidTestImplementationDependency(Dependencies.Kotlin.Test)
        androidTestImplementationDependency(Dependencies.Kotlin.JUnit)
    }
}

fun LibraryExtension.androidCommon(project: org.gradle.api.Project, componentType: ComponentType = ComponentType.Default, packageName: String) {
    compileSdk = LibraryImpl.Android.compileSdk
    buildToolsVersion = LibraryImpl.Android.buildTools
    namespace = "${project.Library.group}.$packageName"

    defaultConfig {
        minSdk = LibraryImpl.Android.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("stableDebug") {
            storeFile = project.rootProject.file("keystore/stableDebug.keystore")
            storePassword = "stableDebug"
            keyAlias = "stableDebug"
            keyPassword = "stableDebug"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            signingConfig = signingConfigs.getByName("stableDebug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    when (componentType) {
        is ComponentType.Compose -> {
            project.logger.lifecycle("This project module is a Compose only module")
            buildFeatures {
                compose = true
            }
            composeOptions {
                kotlinCompilerExtensionVersion = LibraryImpl.Android.composeCompiler
            }
        }
        is ComponentType.DataBinding -> {
            project.logger.lifecycle("This project module is a Databinding only module")
            buildFeatures {
                dataBinding {
                    enable = true
                }
            }
        }
        is ComponentType.Default-> {}
    }
}
