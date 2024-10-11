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
        library("catalog", "com.splendo.kaluga:catalog:"+libs.versions.kaluga.get())
        from(files("gradle/libs.versions.toml"))
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