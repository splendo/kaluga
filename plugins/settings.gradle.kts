pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "kaluga-plugins"

include(":support")
project(":support").projectDir = file("../ksp/plugin-support")
include(":bluetooth")
project(":bluetooth").projectDir = file("../bluetooth/plugin/plugin")
include(":bytes")
project(":bytes").projectDir = file("../bytes/plugin/plugin")
