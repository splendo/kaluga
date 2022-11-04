import java.io.File
import java.io.FileInputStream
import java.util.Properties

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    kotlin("android").apply(false)
    kotlin("multiplatform").apply(false)
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
