import com.splendo.kaluga.plugin.helpers.kalugaVersion

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

        library("catalog", "com.splendo.kaluga:catalog:$catalogVersion")
        from(files("gradle/libs.versions.toml"))
        // override the version in the catalog to match the published version
        version("kaluga", project.kalugaVersion)
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

 afterEvaluate {
     // Gradle just does not do this for version catalogs and it breaks without these

     tasks.named("publishMavenPublicationToMavenCentralRepository") {
         dependsOn(tasks.named("signMavenPublication"))
         dependsOn(tasks.named("signCatalogPublication"))
     }
     tasks.named("publishCatalogPublicationToMavenCentralRepository") {
         dependsOn(tasks.named("signCatalogPublication"))
         dependsOn(tasks.named("signMavenPublication"))
     }
 }

dependencies {
    subprojects.forEach { project ->
        // kover(project)
    }
}
