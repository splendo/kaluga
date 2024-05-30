import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `maven-publish`
}

repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
    google()
    mavenCentral()
}

gradlePlugin {
    plugins.register("kaluga-library-components") {
        id = "kaluga-library-components"
        implementationClass = "LibraryComponentsPlugin"
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {

    val properties = File("${rootDir.absolutePath}/../gradle.properties").loadProperties()
    val kotlinVersion = properties["kaluga.kotlinVersion"] as String
    val androidGradleVersion = properties["kaluga.androidGradlePluginVersion"] as String
    val kotlinterVersion = properties["kaluga.kotlinterGradlePluginVersion"] as String
    logger.lifecycle("Kotlin version $kotlinVersion")

    // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.android.tools.build:gradle:$androidGradleVersion")
    implementation("org.jmailen.gradle:kotlinter-gradle:$kotlinterVersion")
}
