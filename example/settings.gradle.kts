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
            val kalugaKotlinterGradlePluginVersion = settings.extra["kaluga.kotlinterGradlePluginVersion"]
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
                "org.jmailen.kotlinter",
                -> useVersion("$kalugaKotlinterGradlePluginVersion")
                "com.google.gms:google-services"
                -> useVersion("com.google.gms:google-services:$kalugaGoogleServicesGradlePluginVersion")
            }
        }
    }
}

val props = Properties()
val file = File("$rootDir/local.properties")
if (file.exists()) {
    props.load(FileInputStream(file))
}

val exampleEmbeddingMethod = if (System.getenv().containsKey("EXAMPLE_EMBEDDING_METHOD")) {
    System.getenv()["EXAMPLE_EMBEDDING_METHOD"].also {
        logger.lifecycle("System env EXAMPLE_EMBEDDING_METHOD set to ${System.getenv()["EXAMPLE_EMBEDDING_METHOD"]}, using $it")
    }!!
} else {
    val exampleEmbeddingMethodLocalProperties = props["kaluga.exampleEmbeddingMethod"] as? String
    (exampleEmbeddingMethodLocalProperties ?: "composite").also {
        logger.lifecycle("local.properties read (kaluga.exampleEmbeddingMethod=$exampleEmbeddingMethodLocalProperties, using $it)")
    }
}

val isCompositeBuild = when (exampleEmbeddingMethod) {
    "composite" -> true
    else -> false
}

dependencyResolutionManagement {
    repositories {
        if (!isCompositeBuild) {
            val exampleMavenRepo = if (System.getenv().containsKey("EXAMPLE_MAVEN_REPO")) {
                System.getenv()["EXAMPLE_MAVEN_REPO"].also {
                    logger.lifecycle("System env EXAMPLE_MAVEN_REPO set to ${System.getenv()["EXAMPLE_MAVEN_REPO"]}, using $it")
                }!!
            } else {
                val exampleMavenRepoLocalProperties: String? =
                    props["kaluga.exampleMavenRepo"] as? String
                exampleMavenRepoLocalProperties?.also {
                    logger.lifecycle("local.properties read (kaluga.exampleMavenRepo=$exampleMavenRepoLocalProperties, using $it)")
                }
                    ?: "local".also {
                        logger.info("local.properties not found, using default value ($it)")
                    }
            }
            logger.lifecycle("Using repo: $exampleMavenRepo for resolving dependencies")

            when (exampleMavenRepo) {
                null, "", "local" -> mavenLocal()
                "none" -> {/* noop */
                }
                else ->
                    maven(exampleMavenRepo)
            }
        }
        mavenCentral()
        google()
    }
}

includeBuild("../kaluga-library-components")
includeBuild("../convention-plugins")

rootProject.name = "Kaluga Example"
include(":android")
include(":shared")

if (isCompositeBuild) {
    includeBuild("../")
}
