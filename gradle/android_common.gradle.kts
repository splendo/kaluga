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

repositories {
    mavenCentral()
}

android {

    val android_compile_sdk_version: Int by extra
    val android_min_sdk_version: Int by extra
    val android_target_sdk_version: Int by extra
    val android_build_tools_version: String by extra

    compileSdk = android_compile_sdk_version
    buildToolsVersion = android_build_tools_version

    defaultConfig {
        minSdk = android_min_sdk_version
        targetSdk = android_target_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled = false
        }
    }

    val component_type: String by extra
    if (!component_type.toLowerCase().contains("app")) {
        logger.lifecycle("Android sourcesets for this project module are configured as a library")
        sourceSets {
            main {
                manifest.srcFile "src/androidLibMain/AndroidManifest.xml"
                res.srcDir "src/androidLibMain/res"
                if (component_type.contains("compose")) {
                    java.srcDir = "src/androidLibMain/kotlin"
                }
            }
            androidTest {
                manifest.srcFile = "src/androidLibAndroidTest/AndroidManifest.xml"
                java.srcDir = "src/androidLibAndroidTest/kotlin"
                res.srcDir = "src/androidLibAndroidTest/res"
            }


            test {
                java.srcDir = "src/androidLibUnitTest/kotlin"
            }

        }
    } else {
        logger.lifecycle("Android sourcesets for this project module are configured using defaults (for an app)")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += ['-XXLanguage:+InlineClasses', '-Xjvm-default=all']
    }

    if (component_type.contains("compose")) {
        logger.lifecycle("This project module is a Compose only module")
        buildFeatures {
            compose = true
        }
        composeOptions {
            val androidx_compose_compiler_version: String by extra
            kotlinCompilerExtensionVersion = androidx_compose_compiler_version
        }
    }

}

dependencies {
    val kotlinx_coroutines_version: String by extra
    val androidx_appcompat_version: String by extra

    val junit_version: String by extra
    val mockito_version: String by extra
    val bytebuddy_version: String by extra
    val androidx_test_version: String by extra
    val androidx_test_uiautomator_version: String by extra
    val androidx_test_junit_version: String by extra
    val androidx_test_espresso_version: String by extra

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinx_coroutines_version")
    implementation("androidx.appcompat:appcompat:$androidx_appcompat_version")

    testImplementation("junit:junit:$junit_version")
    testImplementation("org.mockito:mockito-core:$mockito_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test"
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    androidTestImplementation("org.mockito:mockito-android:$mockito_version")
    androidTestImplementation("net.bytebuddy:byte-buddy-android:$bytebuddy_version!!")
    androidTestImplementation("net.bytebuddy:byte-buddy-agent:$bytebuddy_version!!")

    androidTestImplementation("androidx.test:core:$androidx_test_version")
    androidTestImplementation("androidx.test:core-ktx:$androidx_test_version")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:$androidx_test_uiautomator_version")
    androidTestImplementation("androidx.test:rules:$androidx_test_version")
    androidTestImplementation("androidx.test.ext:junit:$androidx_test_junit_version")
    androidTestImplementation("androidx.test:runner:$androidx_test_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$androidx_test_espresso_version")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
