import com.splendo.kaluga.plugin.helpers.gitBranch

plugins {
    id("com.android.application")
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.splendo.kaluga"
version = "${libs.versions.kaluga.get()}${gitBranch.kalugaBranchPostfix}"

android {
    namespace = "com.splendo.kaluga.example"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    buildToolsVersion = libs.versions.androidBuildTools.get()
    defaultConfig {
        applicationId = "com.splendo.kaluga.example"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidCompileSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
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
        jvmTarget = libs.versions.java.get()
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
    }
}

dependencies {
    implementation("com.splendo.kaluga:architecture-compose:$version")
    implementation("com.splendo.kaluga:keyboard-compose:$version")
    implementation("com.splendo.kaluga:resources-compose:$version")
    implementation("com.splendo.kaluga:resources-databinding:$version")
    implementation(project(":shared"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.android.play.services.location)
    implementation(libs.android.material)
    implementation(libs.accompanist.themeadaptermaterial)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.compose)
}

