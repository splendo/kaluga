import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
        classpath("com.android.tools.build:gradle:${project.extra["kaluga.androidGradlePluginVersion"]}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kaluga.kotlinVersion"]}")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:${project.extra["kaluga.atomicFuGradlePluginVersion"]}")
    }

    configurations.classpath {
        resolutionStrategy {
            // Temporarily override ktlint version until kotlinter is updated
            // Required because Composable methods require an override for function naming rules
            force(
                "com.pinterest.ktlint:ktlint-rule-engine:1.0.1",
                "com.pinterest.ktlint:ktlint-rule-engine-core:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-core:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-checkstyle:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-json:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-html:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-plain:1.0.1",
                "com.pinterest.ktlint:ktlint-cli-reporter-sarif:1.0.1",
                "com.pinterest.ktlint:ktlint-ruleset-standard:1.0.1"
            )
        }
    }
}

plugins {
    id("kaluga-library-components")
    id("convention.publication")
    id("org.jmailen.kotlinter")
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlin.multiplatform") apply false
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("rs.houtbecke.gradle.recorder.plugin")
}

allprojects {
    repositories {
        mavenCentral()
        google()
        // only enable temporarily if needed:
        /* mavenLocal() */
    }

    tasks.withType<Test> {
        testLogging {
            events = setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
        testLogging.exceptionFormat = TestExceptionFormat.FULL
    }
}

task("generateNonDependentProjectsFile") {
    outputs.upToDateWhen { false }

    val file = project.file("non_dependent_projects.properties")
    file.delete()
    file.appendText("projects=[")
    var firstProject = true

    val blacklist = properties["generateNonDependentProjectsFile.blacklist"]?.toString()?.split(',')?.map { it.trim() } ?: listOf()

    subprojects.forEach { thisProject ->

        val dependsOnOtherProject = subprojects.any { otherProject -> thisProject != otherProject && (thisProject.name.startsWith(otherProject.name) || thisProject.name.endsWith(otherProject.name)) }
        val otherProjectsDependOn = subprojects.any { otherProject -> thisProject != otherProject && (otherProject.name.startsWith(thisProject.name) || otherProject.name.endsWith(thisProject.name)) }

        if (!blacklist.contains(thisProject.name) && (!dependsOnOtherProject || otherProjectsDependOn)) {
            logger.debug("main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")

            if (firstProject)
                firstProject = false
            else
                file.appendText(",")
            file.appendText('"' + thisProject.name + '"')
        } else {
            logger.debug("not a main module: ${thisProject.name} dependsOnOtherProject:$dependsOnOtherProject otherProjectsDependOn:$otherProjectsDependOn")
        }
    }

    file.appendText("]")
}

apiValidation {

    subprojects.forEach {
        val name = it.name

        ignoredClasses.add("com.splendo.kaluga.$name.BuildConfig".toString())
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

group = Library.group
version = Library.version
