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
include(":shared")

if (embedding.embeddingMode is EmbeddingMode.Composite) {
    includeBuild("../")
}
