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
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
            }
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
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
    include(":Components")
    project(":Components").projectDir = file("../../../Components")

    include(":logging")
    project(":logging").projectDir = file("../../../logging")

    include(":permissions")
    project(":permissions").projectDir = file("../../../permissions")

    include(":alerts")
    project(":alerts").projectDir = file("../../../alerts")
}

include(":android")
project(":android").projectDir = file("../../android")

include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

rootProject.name = file("..").name
