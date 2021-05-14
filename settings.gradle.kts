pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {

            val android_gradle_plugin_version: String by settings
            val kotlin_version: String by settings

            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform" ->
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
                "org.jetbrains.kotlin.plugin.serialization" ->
                    useModule("org.jetbrains.kotlin:kotlin-serialization-plugin:$kotlin_version")
                "com.android.library" ->
                    useModule("com.android.tools.build:gradle:$android_gradle_plugin_version")
                "com.android.application" ->
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
includeBuild("convention-plugins")

include(":base")
include(":architecture")
include(":architecture-compose")
include(":test-utils")
include(":alerts")
include(":date-time-picker")
include(":logging")
include(":hud")
include(":permissions")
include(":location")
include(":androidtesthelper")
include(":keyboard")
include(":resources")
include(":review")
include(":system")

