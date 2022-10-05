/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 * Also any new modules should be added to the build matrix in [.git/workflows]
 *
 ***********************************************/

pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {

            val kalugaAndroidGradlePluginVersion =
                settings.extra["kaluga.androidGradlePluginVersion"]
            val kalugaKotlinVersion = settings.extra["kaluga.kotlinVersion"]
            val kalugaKtLintGradlePluginVersion = settings.extra["kaluga.ktLintGradlePluginVersion"]
            val kalugaGoogleServicesGradlePluginVersion = settings.extra["kaluga.googleServicesGradlePluginVersion"]
            val kalugaBinaryCompatibilityValidatorVersion = settings.extra["kaluga.binaryCompatibilityValidatorVersion"]

            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform",
                "org.jetbrains.kotlin.plugin.serialization",
                "org.jetbrains.kotlin.android",
                "org.jetbrains.kotlin.kapt",
                -> useVersion("$kalugaKotlinVersion")
                "com.android.library",
                "com.android.application",
                -> useVersion("$kalugaAndroidGradlePluginVersion")
                "org.jlleitschuh.gradle.ktlint",
                "org.jlleitschuh.gradle.ktlint-idea",
                -> useVersion("$kalugaKtLintGradlePluginVersion")
                "com.google.gms:google-services"
                -> useVersion("com.google.gms:google-services:$kalugaGoogleServicesGradlePluginVersion")
                "org.jetbrains.kotlinx.binary-compatibility-validator"
                -> useVersion("$kalugaBinaryCompatibilityValidatorVersion")
            }
        }
    }
}

apply("gradle/ext.gradle")
includeBuild("convention-plugins")

rootProject.name = "Kaluga"

/* REMINDER (see header), files should be kept up to date with Gradle and the [.git/workflows] */

include(":base")
include(":beacons")
include(":bluetooth")
include(":architecture")
include(":architecture-compose")
include(":alerts")
include(":date-time")
include(":date-time-picker")
include(":logging")
include(":hud")
include(":base-permissions")
include(":bluetooth-permissions")
include(":calendar-permissions")
include(":location-permissions")
include(":storage-permissions")
include(":notifications-permissions")
include(":contacts-permissions")
include(":microphone-permissions")
include(":camera-permissions")
include(":permissions")
include(":location")
include(":keyboard")
include(":keyboard-compose")
include(":links")
include(":resources")
include(":resources-compose")
include(":review")
include(":scientific")
include(":system")
// Test Utils
include(":test-utils")
include(":test-utils-base")
include(":test-utils-alerts")
include(":test-utils-architecture")
include(":test-utils-bluetooth")
include(":test-utils-hud")
include(":test-utils-keyboard")
include(":test-utils-koin")
include(":test-utils-location")
include(":test-utils-permissions")
include(":test-utils-resources")
