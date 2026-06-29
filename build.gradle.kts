import com.splendo.kaluga.plugin.helpers.kalugaVersion

plugins {
    id("com.splendo.kaluga.plugin")
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
     // Gradle just does not do this for version catalogs and it breaks without these.
     // The sign* tasks only exist when signing is configured (see the signAllPublications guard in
     // the Kaluga plugin), so only wire them when a signing key is present — otherwise
     // publishToMavenLocal works locally without signing credentials.
     if (providers.gradleProperty("signingInMemoryKey").getOrNull() != null) {
         tasks.named("publishMavenPublicationToMavenCentralRepository") {
             dependsOn(tasks.named("signMavenPublication"))
             dependsOn(tasks.named("signCatalogPublication"))
         }
         tasks.named("publishCatalogPublicationToMavenCentralRepository") {
             dependsOn(tasks.named("signCatalogPublication"))
             dependsOn(tasks.named("signMavenPublication"))
         }
     }
 }

// `:scientific:converters` generates an enormous API surface (~4.4k files of generated unit-conversion
// functions) that exhausts Dokka's heap, so it stays out of the aggregated documentation. The pure
// `:scientific:scientific` module is an order of magnitude smaller and documents fine, so it is included.
val dokkaExcludedProjects = setOf(":scientific:converters")

dependencies {
    // Group container projects (e.g. `:bluetooth`, `:permissions`) have no build script and apply
    // neither the Kover nor Dokka plugins, so they expose no consumable variants — skip them.
    subprojects.filter { it.buildFile.exists() }.forEach { project ->
        kover(project)
        // Aggregate every module into a single multi-module Dokka site (dokkaGeneratePublicationHtml).
        if (project.path !in dokkaExcludedProjects) {
            dokka(project)
        }
    }
}
