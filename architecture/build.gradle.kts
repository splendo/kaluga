plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("jacoco")
    id("com.android.library")
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

dependencies {
    val ext = (gradle as ExtensionAware).extra
    val kotlin_version: String by ext
    val androidx_lifecycle_version: String by ext
    val androidx_browser_version: String by ext

    api("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle_version")
    api("androidx.lifecycle:lifecycle-livedata-ktx:$androidx_lifecycle_version")
    implementation("androidx.browser:browser:$androidx_browser_version")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js {
        nodejs()
    }

    sourceSets {
        val ext = (gradle as ExtensionAware).extra
        val serialization_version: String by ext

        val commonMain by getting {
            dependencies {
                implementation(project(":base", ""))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialization_version")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
            }
        }

        val commonTest by getting {
            dependencies {
                api(project(":test-utils", ""))
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
