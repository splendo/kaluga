plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("org.jmailen.kotlinter")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    signingConfigs {
        get("debug").apply {
            keyAlias = "key0"
            keyPassword = "nckI1UYofHIMkOnXpmZJVA"
            storeFile = file("../keystore/debug.keystore")
            storePassword = "nckI1UYofHIMkOnXpmZJVA"
        }
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
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
            optIn("androidx.compose.ui.ExperimentalComposeUiApi")
        }
    }
}

dependencies {
    val libraryVersion = Library.version
    implementation("com.splendo.kaluga:architecture-compose:$libraryVersion")
    implementation("com.splendo.kaluga:keyboard-compose:$libraryVersion")
    implementation("com.splendo.kaluga:resources-compose:$libraryVersion")
    implementation("com.splendo.kaluga:resources-databinding:$libraryVersion")
    implementation(project(":shared"))

    implementationDependency(Dependencies.AndroidX.Compose.UI)
    implementationDependency(Dependencies.AndroidX.Compose.UITooling)
    implementationDependency(Dependencies.AndroidX.Compose.UIToolingPreview)
    implementationDependency(Dependencies.AndroidX.Compose.Foundation)
    implementationDependency(Dependencies.AndroidX.Compose.Material)
    implementationDependency(libs.androidx.activity.compose)
    implementationDependency(Dependencies.AndroidX.Navigation.Compose)

    implementationDependency(libs.androidx.fragment)
    implementationDependency(libs.androidx.fragment.ktx)
    implementationDependency(Dependencies.AndroidX.ConstraintLayout)
    implementationDependency(Dependencies.AndroidX.Lifecycle.Service)

    implementationDependency(Dependencies.Android.PlayServices.Location)
    implementationDependency(Dependencies.Android.Material)
    implementationDependency(Dependencies.Accompanist.MaterialThemeAdapter)

    implementationDependency(libs.kotlinx.serialization.core)
    implementationDependency(Dependencies.KotlinX.Serialization.Json)

    implementationDependency(Dependencies.Koin.AndroidXCompose)
}

// Workaround for Kapt not setting the proper JVM target
// See https://youtrack.jetbrains.com/issue/KT-55947/Unable-to-set-kapt-jvm-target-version
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}
