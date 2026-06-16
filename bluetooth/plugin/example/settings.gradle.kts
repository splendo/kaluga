/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 * Also any new modules should be added to the build matrix in [.git/workflows]
 *
 ***********************************************/

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
    versionCatalogs {
        create("libs") {
            from(files("../../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Kaluga Bluetooth Plugin"

// The demo/showcase application.
include(":app:shared")
include(":app:android")

// Codegen validation fixtures, one per plugin capability (see validation/spec for the shared definitions).
include(":validation:full")
include(":validation:contract")
include(":validation:client")
include(":validation:server")
include(":validation:simulator")
include(":validation:external-api-client")
include(":validation:external-api-server")
include(":validation:external-api-simulator")

includeBuild("../../../")
