import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id(libs.plugins.kotlin.multiplatform.get().pluginId)
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.jetbrains.compose)
}

group = "com.splendo.kaluga"
version = libs.versions.kaluga.get()

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            // The JVM-capable feature modules (their Kaluga libraries — date-time, scientific,
            // base — all support JVM). The desktop host loads their Koin feature modules directly
            // rather than reusing `:shared` (which aggregates wasmJs/macOS-only features that have
            // no JVM target).
            implementation(project(":core-arch"))
            implementation(project(":core-koin"))
            implementation(project(":feature-datetime"))
            implementation(project(":feature-localization"))
            implementation(project(":feature-scientific"))
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.splendo.kaluga.example.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KalugaExampleDesktop"
            packageVersion = "1.0.0"
        }
    }
}
