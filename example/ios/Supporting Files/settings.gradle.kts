val android_gradle_plugin_version:String by settings
val kotlin_version:String by settings

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
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
            }
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:${android_gradle_plugin_version}")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${android_gradle_plugin_version}")
            }
        }
    }
}

/***********************************************
 *
 * Changes made to this file should also be reflected in the `settings.gradle` in the root of the project
 *
 ***********************************************/


apply("../../../gradle/ext.gradle")

val ext =  (gradle as ExtensionAware).extra

if (!(ext["exampleAsRoot"] as Boolean)) {
    include(":test-utils")
    project(":test-utils").projectDir = file("../../../test-utils")

    include(":location")
    project(":location").projectDir = file("../../../location")

    include(":base")
    project(":base").projectDir = file("../../../base")

    include(":logging")
    project(":logging").projectDir = file("../../../logging")

    include(":permissions")
    project(":permissions").projectDir = file("../../../permissions")

    include(":alerts")
    project(":alerts").projectDir = file("../../../alerts")

    include(":keyboardManager")
    project(":keyboardManager").projectDir = file("../../../keyboardManager")

    include(":loadingIndicator")
    project(":loadingIndicator").projectDir = file("../../../loadingIndicator")
}

include(":android")
project(":android").projectDir = file("../../android")

include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

rootProject.name = file("..").name
