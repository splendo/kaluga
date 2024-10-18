import com.splendo.kaluga.plugin.helpers.gitBranch

plugins {
    id("com.splendo.kaluga.plugin")
    id("rs.houtbecke.gradle.recorder.plugin")
    `version-catalog`
    `maven-publish`
}

apply(from = "gradle/newModule.gradle.kts")
apply(from = "gradle/copyReports.gradle.kts")

catalog {
    versionCatalog {
        val catalogVersion = libs.versions.kaluga.get()
        val publishVersion = gitBranch.toVersion(catalogVersion)

        library("catalog", "com.splendo.kaluga:catalog:$publishVersion")
        from(files("gradle/libs.versions.toml"))
        // override the version in the catalog to match the published version
        version("kaluga", publishVersion)
    }
}

publishing {
    publications {
        create<MavenPublication>("catalog") {
            from(components["versionCatalog"])
            artifactId = "catalog"
        }
    }
}