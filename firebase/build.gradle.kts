plugins {
    kotlin("multiplatform")
    id("jacoco")
    id("com.android.library")
    id("maven-publish")
    id("org.jlleitschuh.gradle.ktlint")
}

val ext = (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!

android {
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation("com.google.firebase:firebase-firestore:19.0.2")
                implementation("com.google.firebase:firebase-auth:17.0.0")
            }
        }

        val firebaseModules = listOf(
            "FirebaseCore",
            "FirebaseAuth",
            "FirebaseFirestore"
        )

        targets.filterIsInstance<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().forEach {
            it.compilations.getByName("main") {
                firebaseModules.forEach {
                    cinterops.create(it) {
                        packageName("Firebase.$it")
                        defFile(project.file("$projectDir/src/iosMain/c_interop/$it-generated.def"))
                        includeDirs.apply {
                            allHeaders("$projectDir/src/iosMain/c_interop/Carthage/Build/iOS/$it.framework/Headers/")
                        }
                        compilerOpts("-F$projectDir/src/iosMain/c_interop/Carthage/Build/iOS/")
                    }
                }
            }
        }

        // linkerOpts not supported by cinterops
        tasks.create("generateDefs") {
            group = "carthage"
            firebaseModules.forEach {
                val templateFile = File("$projectDir/src/iosMain/c_interop/Firebase.def")
                val generatedFile = File("$projectDir/src/iosMain/c_interop/$it-generated.def")
                val content = templateFile.readText()
                    .replace(
                    "\$CARTHAGE_BUILD_PATH",
                    "$projectDir/src/iosMain/c_interop/Carthage/Build/iOS/"
                    ).replace(
                        "\$FRAMEWORK",
                        it
                    )
                delete(generatedFile)
                generatedFile.createNewFile()
                generatedFile.writeText(content)
            }
        }

        tasks.create("cleanDefs", Delete::class.java) {
            group = "carthage"
            firebaseModules.forEach {
                delete(File("$projectDir/src/iosMain/c_interop/$it-generated.def"))
            }
        }

        tasks.named<Delete>("clean") {
            dependsOn("cleanDefs")
        }

        listOf("bootstrap", "update").forEach {
            task<Exec>("carthage${it.capitalize()}") {
                group = "carthage"
                executable = "carthage"
                args(
                    it,
                    "--project-directory", "src/iosMain/c_interop",
                    "--platform", "iOS",
                    "--cache-builds"
                )
            }
        }

        tasks.withType(org.jetbrains.kotlin.gradle.tasks.CInteropProcess::class) {
            dependsOn("carthageBootstrap")
            dependsOn("generateDefs")
        }
    }
}
