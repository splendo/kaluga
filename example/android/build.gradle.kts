plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
}


group = Library.group
version = Library.version

androidApp {
    namespace = "com.splendo.kaluga.example"
    compileSdk = LibraryImpl.Android.compileSdk
    buildToolsVersion = LibraryImpl.Android.buildTools
    defaultConfig {
        applicationId = "com.splendo.kaluga.example"
        minSdk = LibraryImpl.Android.minSdk
        targetSdk = LibraryImpl.Android.targetSdk
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        get("debug").apply {
            keyAlias = "key0"
            keyPassword = "nckI1UYofHIMkOnXpmZJVA"
            storeFile = file("../keystore/debug.keystore")
            storePassword = "nckI1UYofHIMkOnXpmZJVA"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.get("debug")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/kotlinx-coroutines-core.kotlin_module",
                "META-INF/shared_debug.kotlin_module",
                "META-INF/kotlinx-serialization-runtime.kotlin_module"
            )
        )
    }

    buildFeatures {
        dataBinding {
            enable = true
        }
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = LibraryImpl.Android.composeCompiler
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("kotlin.ExperimentalStdlibApi")
            optIn("androidx.compose.material.ExperimentalMaterialApi")
        }
    }
}

dependencies {
    val libraryVersion = Library.version
    implementation("com.splendo.kaluga:architecture-compose:$libraryVersion")
    implementation("com.splendo.kaluga:resources-compose:$libraryVersion")
    implementation(project(":shared"))

    implement(Dependencies.AndroidX.Compose.UI)
    implement(Dependencies.AndroidX.Compose.UITooling)
    implement(Dependencies.AndroidX.Compose.UIToolingPreview)
    implement(Dependencies.AndroidX.Compose.Foundation)
    implement(Dependencies.AndroidX.Compose.Material)
    implement(Dependencies.AndroidX.Activity.Compose)
    implement(Dependencies.AndroidX.Navigation.Compose)

    implement(Dependencies.AndroidX.FragmentKtx)
    implement(Dependencies.AndroidX.ConstraintLayout)
    implement(Dependencies.AndroidX.Lifecycle.Service)

    implement(Dependencies.Android.PlayServices.Location)
    implement(Dependencies.Android.Material)
    implement(Dependencies.Android.MaterialComposeThemeAdapter)

    implement(Dependencies.KotlinX.Serialization.Core)
    implement(Dependencies.KotlinX.Serialization.Json)
}