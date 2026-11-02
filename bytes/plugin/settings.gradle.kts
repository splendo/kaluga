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

rootProject.name = "Kaluga Bytes Plugin"

include(":plugin")
