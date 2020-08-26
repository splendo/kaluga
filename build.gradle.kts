buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()

// uncomment for early access / dev releases
//        maven { url 'https://dl.bintray.com/kotlin/kotlin-dev' }
//        maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }
    }
    dependencies {

        val android_gradle_plugin_version: String by project
        val kotlin_version: String by project

        classpath("com.android.tools.build:gradle:$android_gradle_plugin_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("com.google.gms:google-services:4.3.3")
    }
}

plugins {
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "9.2.1"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenLocal()

// uncomment for early access / dev releases
//        maven { url 'https://dl.bintray.com/kotlin/kotlin-dev' }
//        maven { url 'https://dl.bintray.com/kotlin/kotlin-eap' }
    }

    tasks.withType(Test::class.java) {
        testLogging {
            events = mutableSetOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                org.gradle.api.tasks.testing.logging.TestLogEvent.STARTED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
            )
        }
        testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

val ext = (gradle as ExtensionAware).extra

group = "com.splendo.kaluga"
version = ext["library_version"]!!
