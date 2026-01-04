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

    includeBuild("../plugin")
    includeBuild("../../kaluga-library-components")
    includeBuild("../../gradle-test-recorder/plugin-build/")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Kaluga Bluetooth Generation Example"

include(":example")

includeBuild("../../")
