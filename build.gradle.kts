buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // mostly migrated to new style plugin declarations, but some cross plugin interaction still requires this
        classpath(libs.android.gradle)
        classpath(libs.kotlin.gradle)
        classpath(libs.kotlinx.atomicfu.gradle)
    }
}

plugins {
    id("kaluga-library-components")
    // id("convention.publication")
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.dokka)
    // id("rs.houtbecke.gradle.recorder.plugin")
}
//
// allprojects {
//     repositories {
//         mavenCentral()
//         google()
//         // only enable temporarily if needed:
//         /* mavenLocal() */
//     }
//
//     tasks.withType<Test> {
//         testLogging {
//             events = setOf(TestLogEvent.STANDARD_OUT, TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
//         }
//         testLogging.exceptionFormat = TestExceptionFormat.FULL
//     }
// }
//


apiValidation {

    subprojects.forEach {
        val name = it.name

        ignoredClasses.add("com.splendo.kaluga.$name.BuildConfig")
        ignoredClasses.add("com.splendo.kaluga.${name.replace("-", ".")}.BuildConfig")
        ignoredClasses.add("com.splendo.kaluga.${name.replace("-", "")}.BuildConfig")
        ignoredClasses.add("com.splendo.kaluga.permissions.${name.replace("-permissions", "")}.BuildConfig")
    }

    ignoredClasses.add("com.splendo.kaluga.permissions.BuildConfig")
    ignoredClasses.add("com.splendo.kaluga.test.BuildConfig")
    ignoredClasses.add("com.splendo.kaluga.datetime.timer.BuildConfig")
}

apply(from = "gradle/newModule.gradle.kts")
apply(from = "gradle/copyReports.gradle.kts")

// group = Library.group
// version = Library.version
