import java.io.File
import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
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

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("../kaluga-library-components")
includeBuild("../convention-plugins")

rootProject.name = "Kaluga Example"
include(":android")
include(":shared")

val exampleEmbeddingMethod = if (System.getenv().containsKey("EXAMPLE_EMBEDDING_METHOD")) {
    System.getenv()["EXAMPLE_EMBEDDING_METHOD"].also {
        logger.lifecycle("System env EXAMPLE_EMBEDDING_METHOD set to ${System.getenv()["EXAMPLE_EMBEDDING_METHOD"]}, using $it")
    }!!
} else {
    val props = Properties()
    props.load(FileInputStream(File("$rootDir/local.properties")))
    val exampleEmbeddingMethodLocalProperties = props["kaluga.exampleEmbeddingMethod"] as? String
    (exampleEmbeddingMethodLocalProperties ?: "composite").also {
        logger.lifecycle("local.properties read (kaluga.exampleEmbeddingMethod=$exampleEmbeddingMethodLocalProperties, using $it)")
    }
}

when (exampleEmbeddingMethod) {
    "composite" -> includeBuild("../")
    else -> {}
}
