import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
        classpath("com.android.tools.build:gradle:${project.extra["kaluga.androidGradlePluginVersion"]}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kaluga.kotlinVersion"]}")
    }
}

plugins {
    id("convention.publication")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jlleitschuh.gradle.ktlint-idea")
    id("org.jetbrains.kotlin.multiplatform") apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

// TODO: To be removed once we will migrate to kotlin version 1.6.20
// https://youtrack.jetbrains.com/issue/KT-49109#focus=Comments-27-5667134.0-0
rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin>() {
    rootProject.extensions.getByType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension::class.java).nodeVersion = "16.13.2"
}

allprojects {
    repositories {
        mavenCentral()
        google()
        // only enable temporarily if needed:
        /* mavenLocal() */
    }

    tasks.withType<Test>() {
        testLogging {
            events = setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }

    tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask>().configureEach {
        // it.workerMaxHeapSize.set("512m")
    }
}

apiValidation {

    subprojects.forEach {
        val name = it.name

        ignoredClasses.add("com.splendo.kaluga.${name}.BuildConfig".toString())
        ignoredClasses.add("com.splendo.kaluga.${name.replace("-", ".")}.BuildConfig".toString())
        ignoredClasses.add("com.splendo.kaluga.${name.replace("-", "")}.BuildConfig".toString())
        ignoredClasses.add("com.splendo.kaluga.permissions.${name.replace("-permissions", "")}.BuildConfig".toString())
    }

    ignoredClasses.add("com.splendo.kaluga.permissions.BuildConfig")
    ignoredClasses.add("com.splendo.kaluga.test.BuildConfig")
    ignoredClasses.add("com.splendo.kaluga.datetime.timer.BuildConfig")
}

apply(from = "gradle/newModule.gradle.kts")
apply(from = "gradle/copyReports.gradle.kts")

group = "com.splendo.kaluga"
version = Library.version
