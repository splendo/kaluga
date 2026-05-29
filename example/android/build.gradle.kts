plugins {
    id("com.android.application")
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    id("com.android.legacy-kapt")
}

group = "com.splendo.kaluga"
version = libs.versions.kaluga.get()

android {
    namespace = "com.splendo.kaluga.example"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
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
                "META-INF/kotlinx-serialization-runtime.kotlin_module",
            ),
        )
    }

    buildFeatures {
        dataBinding {
            enable = true
        }
    }
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
    compilerOptions {
        optIn.addAll(
            "kotlin.ExperimentalStdlibApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.ui.ExperimentalComposeUiApi",
        )
    }
}

dependencies {
    implementation("com.splendo.kaluga:architecture-compose:${project.rootProject.version}")
    implementation("com.splendo.kaluga:keyboard-compose:${project.rootProject.version}")
    implementation("com.splendo.kaluga:resources-compose:${project.rootProject.version}")
    implementation("com.splendo.kaluga:resources-databinding:${project.rootProject.version}")
    implementation(project(":shared"))
    implementation(project(":mobileshared"))

    implementation(libs.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.navigation)

    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.android.play.services.location)
    implementation(libs.android.material)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.compose)
}
