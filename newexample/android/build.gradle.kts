plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.splendo.kaluga.example.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.splendo.kaluga.example.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implement(Dependencies.AndroidX.Compose.UI)
    implement(Dependencies.AndroidX.Compose.UITooling)
    implement(Dependencies.AndroidX.Compose.UIToolingPreview)
    implement(Dependencies.AndroidX.Compose.Foundation)
    implement(Dependencies.AndroidX.Compose.Material)
    implement(Dependencies.AndroidX.Activity.Compose)
}