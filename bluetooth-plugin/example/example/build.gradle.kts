import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.splendo.kaluga.bluetooth.plugin")
    id("com.android.library")
}

android {
    namespace = "com.splendo.kaluga.bluetooth.example"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
    androidTarget()
    iosSimulatorArm64()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get())) // your JVM target
    }
}

