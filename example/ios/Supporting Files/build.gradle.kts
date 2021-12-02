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
