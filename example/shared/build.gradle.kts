plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("jacoco")
    id("org.jmailen.kotlinter")
}

val libraryVersion = Library.version
val modules = listOf(
    "alerts" to true,
    "architecture" to true,
    "base" to false,
    "bluetooth" to false,
    "beacons" to false,
    "date-time" to false,
    "date-time-picker" to true,
    "hud" to true,
    "keyboard" to true,
    "links" to true,
    "location" to false,
    "logging" to false,
    "media" to true,
    "resources" to true,
    "review" to true,
    "scientific" to false,
    "system" to true,
    "permissions" to true
)

commonComponent("example.shared") {
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

// Use Gradle 8 before Kotlin 1.9 https://youtrack.jetbrains.com/issue/KT-55751
listOf(
    "debugFrameworkIosFat",
    "debugFrameworkIosX64",
    "releaseFrameworkIosX64",
    "releaseFrameworkIosFat",
).forEach {
    if (configurations.names.contains(it)) {
        configurations.named(it).configure {
            attributes {
                attributes {
                    attribute(Attribute.of("KT-55751", String::class.java), it)
                }
            }
        }
    }
}
