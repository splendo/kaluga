import com.splendo.kaluga.example.plugin.EmbeddingMode
import org.gradle.internal.management.VersionCatalogBuilderInternal

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    includeBuild("../kaluga-library-components")
    includeBuild("../bluetooth/plugin")
    includeBuild("./embedding")
}

plugins {
    id("com.splendo.kaluga.example.settings.plugin")
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    // For the androidx catalog artifact; project repositories are unaffected
    // (default PREFER_PROJECT mode).
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        val libs = create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
        // androidx's monthly release catalog, month pinned in the main build's toml
        create("androidxLib") {
            val month = (libs as VersionCatalogBuilderInternal).build().getVersion("androidx-version-catalog").version
            from("androidx.gradle:gradle-version-catalog:$month")
        }
    }
}

// Lowercase, no spaces: the name is reused as the root npm package name by the Kotlin/Wasm
// tooling (`:web`), and npm package names may not contain spaces or uppercase characters.
rootProject.name = "kaluga-example"
include(":android")
include(":core-arch")
include(":core-koin")
include(":core-stylable")
include(":feature-alerts")
include(":feature-architecture")
include(":feature-beacons")
include(":feature-bluetooth-base")
include(":feature-bluetooth-client")
include(":feature-bluetooth-generation")
include(":feature-bluetooth-server")
include(":feature-datetime")
include(":feature-datetimepicker")
include(":feature-hud")
include(":feature-keyboard")
include(":feature-links")
include(":feature-localization")
include(":feature-location")
include(":feature-media")
include(":feature-permissions")
include(":feature-resources")
include(":feature-review")
include(":feature-scientific")
include(":feature-system")
include(":shared")
include(":web")
include(":desktop")

if (embedding.embeddingMode is EmbeddingMode.Composite) {
    includeBuild("../")
}
