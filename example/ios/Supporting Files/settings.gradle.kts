/***********************************************
 *
 * Changes made to this file should also be reflected in the [settings.gradle.kts] in the root of the project
 *
 ***********************************************/

pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {

            val kalugaAndroidGradlePluginVersion = settings.extra["kaluga.androidGradlePluginVersion"]
            val kalugaKotlinVersion = settings.extra["kaluga.kotlinVersion"]
            val kalugaKtLintGradlePluginVersion = settings.extra["kaluga.ktLintGradlePluginVersion"]
            val kalugaGoogleServicesGradlePluginVersion = settings.extra["kaluga.googleServicesGradlePluginVersion"]

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

includeBuild("../../../convention-plugins")
apply("../../../gradle/ext.gradle")

val ext = (gradle as ExtensionAware).extra

if (!(ext["example_as_root"] as Boolean)) {

    include(":alerts")
    project(":alerts").projectDir = file("../../../alerts")

    include(":architecture")
    project(":architecture").projectDir = file("../../../architecture")

    include(":architecture-compose")
    project(":architecture-compose").projectDir = file("../../../architecture-compose")

    include(":base")
    project(":base").projectDir = file("../../../base")

    include(":bluetooth")
    project(":bluetooth").projectDir = file("../../../bluetooth")

    include(":beacons")
    project(":beacons").projectDir = file("../../../beacons")

    include(":date-time-picker")
    project(":date-time-picker").projectDir = file("../../../date-time-picker")

    include(":hud")
    project(":hud").projectDir = file("../../../hud")

    include(":keyboard")
    project(":keyboard").projectDir = file("../../../keyboard")

    include(":links")
    project(":links").projectDir = file("../../../links")

    include(":location")
    project(":location").projectDir = file("../../../location")

    include(":logging")
    project(":logging").projectDir = file("../../../logging")

    include(":base-permissions")
    project(":base-permissions").projectDir = file("../../../base-permissions")

    include(":location-permissions")
    project(":location-permissions").projectDir = file("../../../location-permissions")

    include(":bluetooth-permissions")
    project(":bluetooth-permissions").projectDir = file("../../../bluetooth-permissions")

    include(":camera-permissions")
    project(":camera-permissions").projectDir = file("../../../camera-permissions")

    include(":contacts-permissions")
    project(":contacts-permissions").projectDir = file("../../../contacts-permissions")

    include(":microphone-permissions")
    project(":microphone-permissions").projectDir = file("../../../microphone-permissions")

    include(":storage-permissions")
    project(":storage-permissions").projectDir = file("../../../storage-permissions")

    include(":notifications-permissions")
    project(":notifications-permissions").projectDir = file("../../../notifications-permissions")

    include(":calendar-permissions")
    project(":calendar-permissions").projectDir = file("../../../calendar-permissions")

    include(":resources")
    project(":resources").projectDir = file("../../../resources")

    include(":review")
    project(":review").projectDir = file("../../../review")

    include(":system")
    project(":system").projectDir = file("../../../system")

    include(":test-utils")
    project(":test-utils").projectDir = file("../../../test-utils")

    include(":test-utils-system")
    project(":test-utils-system").projectDir = file("../../../test-utils-system")
}

include(":android")
project(":android").projectDir = file("../../android")

include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

rootProject.name = file("../..").name
