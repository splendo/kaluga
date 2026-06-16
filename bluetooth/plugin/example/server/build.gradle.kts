import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.splendo.kaluga.bluetooth.plugin")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())

    android {
        namespace = "com.splendo.kaluga.bluetooth.sharedserver"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    iosSimulatorArm64()

    dependencies {
        implementation(libs.kotlinx.coroutines.core)
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    annotationSource("../shared-spec")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_3)
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
    }
}
