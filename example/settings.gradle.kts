import com.splendo.kaluga.example.plugin.EmbeddingMode

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    includeBuild("../kaluga-library-components")
    includeBuild("./embedding")
}

plugins {
    id("com.splendo.kaluga.example.settings.plugin")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Kaluga Example"
include(":android")
include(":core-arch")
include(":core-koin")
include(":core-stylable")
include(":feature-alerts")
include(":feature-architecture")
include(":feature-beacons")
include(":feature-bluetooth")
include(":feature-datetime")
include(":feature-datetimepicker")
include(":feature-hud")
include(":feature-info")
include(":feature-keyboard")
include(":feature-links")
include(":feature-location")
include(":feature-media")
include(":feature-permissions")
include(":feature-resources")
include(":feature-review")
include(":feature-scientific")
include(":feature-system")
include(":shared")

if (embedding.embeddingMode is EmbeddingMode.Composite) {
    includeBuild("../")
}
