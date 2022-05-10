/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
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
            val kalugaGoogleServicesGradlePluginVersion =
                settings.extra["kaluga.googleServicesGradlePluginVersion"]

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
            }
        }
    }
}

apply("gradle/ext.gradle")
includeBuild("convention-plugins")

rootProject.name = "Kaluga"

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
include(":links")
include(":resources")
include(":resources-compose")
include(":review")
include(":scientific")
include(":system")
// Test Utils
include(":test-utils-base")
include(":test-utils-alerts")
include(":test-utils-architecture")
include(":test-utils-bluetooth")
include(":test-utils-hud")
include(":test-utils-keyboard")
include(":test-utils-koin")
include(":test-utils-permissions")
include(":test-utils-resources")
include(":androidtesthelper")
