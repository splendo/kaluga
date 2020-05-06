val android_gradle_plugin_version: String by settings
val kotlin_version: String by settings

pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
        jcenter()
    }

    resolutionStrategy {
        eachPlugin {

            if (requested.id.id == "kotlin-multiplatform") {
                // The version here must be kept in sync with gradle/ext.gradle and settings.gradle in the root
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
            }
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:$android_gradle_plugin_version")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:$android_gradle_plugin_version")
            }
        }
    }
}

/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` under [example/ios/Supporting Files]
 *
 ***********************************************/

apply("gradle/ext.gradle")

rootProject.name = "Kaluga"

include(":base")
include(":test-utils")
include(":alerts")
include(":logging")
include(":hud")
include(":permissions")
include(":location")
include(":androidtesthelper")
include(":template")
