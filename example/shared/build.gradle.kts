plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint")
}

val libraryVersion = Library.version
val modules = listOf(
    "alerts" to true,
    "architecture" to true,
    "base" to false,
    "bluetooth" to false,
    "beacons" to false,
    "date-time-picker" to true,
    "hud" to true,
    "keyboard" to true,
    "links" to false,
    "location" to false,
    "logging" to false,
    "resources" to true,
    "review" to true,
    "system" to true,
    "permissions" to true
)

commonComponent {
    logger.lifecycle("Configure framework")
    baseName = "KalugaExampleShared"
    isStatic = false
    transitiveExport = true
    modules.forEach { (module, isExportable) ->
        if (isExportable) {
            export("com.splendo.kaluga:$module:$libraryVersion")
        }
    }
}

kotlin {
    sourceSets {
        getByName("commonMain") {
            dependencies {
                modules.forEach { (module, _) ->
                    api("com.splendo.kaluga:$module:$libraryVersion")
                }
                apiDependency(Dependencies.Koin.Core)
            }
        }
    }
}

android {
    dependencies {
        apiDependency(Dependencies.Koin.Android)
    }
}
