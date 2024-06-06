import java.io.File
import java.io.FileInputStream
import java.util.Properties

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    includeBuild("../kaluga-library-components")
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
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
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

rootProject.name = "Kaluga Example"
//include(":android")
include(":shared")

if (isCompositeBuild) {
    includeBuild("../")
}
