import com.splendo.kaluga.plugin.helpers.kalugaVersion
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.kotlinter)
    id("com.palantir.git-version")
}

group = "com.splendo.kaluga.ksp"
version = kalugaVersion

mavenPublishing {
    configure(KotlinJvm(javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationHtml"), sourcesJar = SourcesJar.Sources()))
    coordinates(artifactId = "plugin-support")

    pom {
        name.set("Kaluga KSP plugin support")
        description.set("KSP wiring shared by the Kaluga code generation Gradle plugins.")
        url.set("https://github.com/splendo/kaluga")

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("splendo")
                name.set("Splendo Consulting BV")
            }
        }
        scm {
            url.set("https://github.com/splendo/kaluga")
        }
    }

    publishToMavenCentral()
    // Only sign when a signing key is configured, so `publishToMavenLocal` keeps working locally.
    if (providers.gradleProperty("signingInMemoryKey").getOrNull() != null) {
        signAllPublications()
    }
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

// The plugins using this bring Gradle, the Kotlin Gradle plugin and KSP themselves, at the versions they are built with.
dependencies {
    compileOnly(gradleApi())
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.google.devtools.ksp.gradle)
    testImplementation(gradleApi())
    testImplementation(libs.kotlin.gradle)
    testImplementation(libs.google.devtools.ksp.gradle)
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // ProjectBuilder defines classes in java.lang; java-gradle-plugin opens it for plugin tests, a plain JVM module must.
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}
