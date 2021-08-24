buildscript {
    val android_gradle_plugin_version: String by project
    val kotlin_version: String by project

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:$android_gradle_plugin_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")
        classpath("com.google.gms:google-services:4.3.10")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.1.0")
        classpath("com.adarshr:gradle-test-logger-plugin:2.1.0")
    }
}

val ext = (gradle as ExtensionAware).extra
val repo = ext["exampleMavenRepo"]
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
