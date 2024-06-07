import com.splendo.kaluga.example.plugin.EmbeddingMode
import java.io.File
import java.io.FileInputStream
import java.util.Properties

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
            from(files("../libs.versions.toml"))
        }
    }
}

rootProject.name = "Kaluga Example"
include(":android")
include(":shared")

if (embedding.embeddingMode is EmbeddingMode.Composite) {
    includeBuild("../")
}
