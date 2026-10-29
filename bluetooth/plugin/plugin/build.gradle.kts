import com.splendo.kaluga.plugin.helpers.kalugaVersion
import com.vanniktech.maven.publish.GradlePlugin
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    alias(libs.plugins.plugin.publish)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.kotlinter)
    id("com.palantir.git-version")
}

repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
    google()
    mavenCentral()
}

group = "com.splendo.kaluga.bluetooth"
version = kalugaVersion

gradlePlugin {
    website = "https://github.com/splendo/kaluga"
    vcsUrl = "https://github.com/splendo/kaluga.git"
    plugins.register("com.splendo.kaluga.bluetooth.plugin") {
        id = "com.splendo.kaluga.bluetooth.plugin"
        implementationClass = "com.splendo.kaluga.bluetooth.plugin.BluetoothPlugin"
        displayName = "Kaluga Bluetooth code generation"
        description = "Generates typed Bluetooth clients and servers from annotated device definitions."
        tags = listOf("kotlin", "kotlin-multiplatform", "bluetooth", "ble", "ksp", "codegen")
    }
}

mavenPublishing {
    // `plugin-publish` already adds the javadoc and sources jars to the java component.
    configure(GradlePlugin(javadocJar = JavadocJar.None(), sourcesJar = SourcesJar.None()))
    coordinates(artifactId = "gradle-plugin")

    pom {
        name.set("Kaluga Bluetooth code generation")
        description.set("Generates typed Bluetooth clients and servers from annotated device definitions.")
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

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.google.devtools.ksp.gradle)
    implementation(project(":support"))
    implementation(libs.kotlinpoet)
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_4)
    }
}

val generatePluginVersion by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/resources/main")
    val outputFile = outputDir.map { it.file("bluetooth.properties") }
    val version = kalugaVersion
    val coroutinesVersion = libs.versions.kotlinx.coroutines.get()

    inputs.property("kalugaVersion", version)
    inputs.property("coroutinesVersion", coroutinesVersion)
    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()

        file.writeText("kalugaVersion=$version\ncoroutinesVersion=$coroutinesVersion\n")
    }
}

tasks.named<Jar>("jar") {
    dependsOn(generatePluginVersion)
    from(layout.buildDirectory.dir("generated/resources/main"))
}
