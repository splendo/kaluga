/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 * CI compiles this whole composite (the `bluetooth-plugin` jobs in [.github/workflows]), so new validation
 * modules added below are picked up automatically.
 *
 ***********************************************/

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

rootProject.name = "Kaluga Bluetooth Plugin"

// Codegen validation fixtures, one per plugin capability (see validation/spec for the shared definitions).
include(":validation:full")
include(":validation:xml")
include(":validation:contract")
include(":validation:client")
include(":validation:server")
include(":validation:simulator")
include(":validation:mock")
include(":validation:external-api-client")
include(":validation:external-api-server")
include(":validation:external-api-simulator")
include(":validation:external-api-mock")

includeBuild("../../../")
