import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `version-catalog`
    `maven-publish`
    alias(libs.plugins.kotlinter)
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
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.compose.gradle)
    implementation(libs.dependencycheck.gradle)
    implementation(libs.dokka.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlinter.gradle)
    implementation(libs.kotlinx.atomicfu.gradle)
    implementation(libs.kotlinx.binarycompatibilityvalidator.gradle)
}
val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    languageVersion = "2.0"
}