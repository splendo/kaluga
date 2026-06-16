plugins {
    id("com.android.application")
    id(libs.plugins.compose.get().pluginId)
}

android {
    namespace = "com.splendo.kaluga.bluetooth.demo.android"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.splendo.kaluga.bluetooth.demo"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidCompileSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":app:shared"))
    implementation("com.splendo.kaluga.base:base:${libs.versions.kaluga.get()}")
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
}
