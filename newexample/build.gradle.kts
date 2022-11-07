import java.io.File
import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("multiplatform").apply(false)
    id("kaluga-library-components")
}

allprojects {
    repositories {
        val exampleMavenRepo = if (System.getenv().containsKey("EXAMPLE_MAVEN_REPO")) {
            System.getenv()["EXAMPLE_MAVEN_REPO"].also {
                logger.lifecycle("System env EXAMPLE_MAVEN_REPO set to ${System.getenv()["EXAMPLE_MAVEN_REPO"]}, using $it")
            }!!
        } else {
            // load some more from local.properties or set defaults.
            val props = Properties()
            props.load(FileInputStream(File("$rootDir/local.properties")))
            val exampleMavenRepoLocalProperties: String? =
                props["kaluga.exampleMavenRepo"] as? String
            exampleMavenRepoLocalProperties?.also {
                logger.lifecycle("local.properties read (kaluga.exampleMavenRepo=$exampleMavenRepoLocalProperties, using $it)")
            }
                ?: "local".also {
                    logger.lifecycle("local.properties not found, using default value ($it)")
                }
        }
        logger.lifecycle("Using repo: $exampleMavenRepo for resolving dependencies")

        when(exampleMavenRepo) {
            null, "", "local" -> mavenLocal()
            "none" -> {/* noop */}
            else ->
                maven(exampleMavenRepo)
        }
        mavenCentral()
        google()
    }
}
