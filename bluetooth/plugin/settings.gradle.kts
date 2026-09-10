/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 * Also any new modules should be added to the build matrix in [.git/workflows]
 *
 ***********************************************/

import org.gradle.internal.management.VersionCatalogBuilderInternal

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
        val libs = create("libs") {
            from(files("../../gradle/libs.versions.toml"))
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

include(":plugin")
