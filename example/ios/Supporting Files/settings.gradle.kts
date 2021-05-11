pluginManagement {

    repositories {
        gradlePluginPortal()
        google()
    }

    resolutionStrategy {
        eachPlugin {

            val android_gradle_plugin_version:String by settings
            val kotlin_version:String by settings

            when (requested.id.id) {
                "kotlin-multiplatform" ->
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
 * Changes made to this file should also be reflected in the `settings.gradle` in the root of the project
 *
 ***********************************************/

includeBuild("../../../convention-plugins")
apply("../../../gradle/ext.gradle")

val ext = (gradle as ExtensionAware).extra

if (!(ext["exampleAsRoot"] as Boolean)) {

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


    include(":date-time-picker")
    project(":date-time-picker").projectDir = file("../../../date-time-picker")

    include(":hud")
    project(":hud").projectDir = file("../../../hud")

    include(":keyboard")
    project(":keyboard").projectDir = file("../../../keyboard")

    include(":location")
    project(":location").projectDir = file("../../../location")

    include(":logging")
    project(":logging").projectDir = file("../../../logging")

    include(":permissions")
    project(":permissions").projectDir = file("../../../permissions")

    include(":resources")
    project(":resources").projectDir = file("../../../resources")

    include(":review")
    project(":review").projectDir = file("../../../review")

    include(":system")
    project(":system").projectDir = file("../../../system")

    include(":test-utils")
    project(":test-utils").projectDir = file("../../../test-utils")
}

include(":android")
project(":android").projectDir = file("../../android")

include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

rootProject.name = file("..").name
