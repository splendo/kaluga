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

fun org.gradle.api.Project.commonAndroidComponent(type: ComponentType = ComponentType.Default) {
    androidLibrary {
        androidCommon(this@commonAndroidComponent, type)
    }

    dependencies {
        implement(Dependencies.KotlinX.Coroutines.Android)
        implement(Dependencies.AndroidX.AppCompat)

        implementForTest(Dependencies.JUnit)
        implementForTest(Dependencies.Mockito.Core)
        implementForTest(Dependencies.Kotlin.Test)
        implementForTest(Dependencies.Kotlin.JUnit)

        implementForAndroidTest(Dependencies.Mockito.Android)
        implementForAndroidTest(Dependencies.ByteBuddy.Android)
        implementForAndroidTest(Dependencies.ByteBuddy.Agent)

        implementForAndroidTest(Dependencies.AndroidX.Test.Core)
        implementForAndroidTest(Dependencies.AndroidX.Test.CoreKtx)
        implementForAndroidTest(Dependencies.AndroidX.Test.UIAutomator)
        implementForAndroidTest(Dependencies.AndroidX.Test.Rules)
        implementForAndroidTest(Dependencies.AndroidX.Test.JUnit)
        implementForAndroidTest(Dependencies.AndroidX.Test.Runner)
        implementForAndroidTest(Dependencies.AndroidX.Test.Espresso)
        implementForAndroidTest(Dependencies.Kotlin.Test)
        implementForAndroidTest(Dependencies.Kotlin.JUnit)
    }
}

fun LibraryExtension.androidCommon(project: org.gradle.api.Project, componentType: ComponentType = ComponentType.Default) {
    compileSdk = LibraryImpl.Android.compileSdk
    buildToolsVersion = LibraryImpl.Android.buildTools

    defaultConfig {
        minSdk = LibraryImpl.Android.minSdk
        targetSdk = LibraryImpl.Android.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    project.logger.lifecycle("Android sourcesets for this project module are configured as a library")
    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidLibMain/AndroidManifest.xml")
            res.srcDir("src/androidLibMain/res")
            if (componentType is ComponentType.Compose) {
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-XXLanguage:+InlineClasses", "-Xjvm-default=all")
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
        is ComponentType.Default-> {}
    }
}
