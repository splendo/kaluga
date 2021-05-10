buildscript {
    val ext = (gradle as ExtensionAware).extra
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
        classpath("com.google.gms:google-services:4.3.3")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.0.0")
        classpath("com.adarshr:gradle-test-logger-plugin:2.1.0")
    }
}
