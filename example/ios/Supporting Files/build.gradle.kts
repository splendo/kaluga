// TODO: To be removed once we will migrate to kotlin version 1.6.20
// https://youtrack.jetbrains.com/issue/KT-49109#focus=Comments-27-5667134.0-0
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.13.2"
}

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        val kalugaAndroidGradlePluginVersion = project.extra["kaluga.androidGradlePluginVersion"]
        // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
        classpath("com.android.tools.build:gradle:${kalugaAndroidGradlePluginVersion}")
    }
}

plugins {
    kotlin("multiplatform") apply false
}

val ext = (gradle as ExtensionAware).extra
val repo = ext["example_maven_repo"]
logger.lifecycle("Using repo: $repo for resolving dependencies")

allprojects {

    repositories {
        when(repo) {
            null, "", "local" -> mavenLocal()
            "none" -> {/* noop */}
            else ->
                maven(repo)
        }
        mavenCentral()
        google()
    }
}
