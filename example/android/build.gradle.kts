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

    implementationDependency(Dependencies.AndroidX.Compose.UI)
    implementationDependency(Dependencies.AndroidX.Compose.UITooling)
    implementationDependency(Dependencies.AndroidX.Compose.UIToolingPreview)
    implementationDependency(Dependencies.AndroidX.Compose.Foundation)
    implementationDependency(Dependencies.AndroidX.Compose.Material)
    implementationDependency(Dependencies.AndroidX.Activity.Compose)
    implementationDependency(Dependencies.AndroidX.Navigation.Compose)

    implementationDependency(Dependencies.AndroidX.FragmentKtx)
    implementationDependency(Dependencies.AndroidX.ConstraintLayout)
    implementationDependency(Dependencies.AndroidX.Lifecycle.Service)

    implementationDependency(Dependencies.Android.PlayServices.Location)
    implementationDependency(Dependencies.Android.Material)
    implementationDependency(Dependencies.Android.MaterialComposeThemeAdapter)

    implementationDependency(Dependencies.KotlinX.Serialization.Core)
    implementationDependency(Dependencies.KotlinX.Serialization.Json)
}
