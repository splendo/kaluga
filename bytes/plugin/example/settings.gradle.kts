// CI compiles this whole composite (the `bytes-plugin` jobs in [.github/workflows]), so new validation
// modules added below are picked up automatically.

import org.gradle.internal.management.VersionCatalogBuilderInternal

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    includeBuild("..")
    includeBuild("../../../kaluga-library-components")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        val libs = create("libs") {
            from(files("../../../gradle/libs.versions.toml"))
        }
        // androidx's monthly catalog, pinned by androidx-version-catalog in the
        // same file — the Kaluga plugin resolves androidx artifacts from here.
        create("androidxLibs") {
            val month = (libs as VersionCatalogBuilderInternal).build().getVersion("androidx-version-catalog").version
            from("androidx.gradle:gradle-version-catalog:$month")
        }
    }
}

// Also the name of the npm package of the web targets, which cannot contain spaces.
rootProject.name = "kaluga-bytes-plugin-example"

// Codegen validation fixtures, one per plugin capability.
include(":validation:core")
include(":validation:format")

// Named explicitly: named after its directory, the checkout would clash with the plugin build when both share a name.
includeBuild("../../../") {
    name = "kaluga"
}
