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
apply("../../../gradle/ext.gradle.kts")

val ext = (gradle as ExtensionAware).extra

when (ext["example_embedding_method"] ) {
    "composite" -> {
        includeBuild("../../..")
    }
}

include(":android")
project(":android").projectDir = file("../../android")

include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

rootProject.name = file("../..").name
