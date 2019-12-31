import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetPreset
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess

val ext =  (gradle as ExtensionAware).extra

apply(from = "../gradle/publishable_component.gradle")

group = "com.splendo.kaluga"
version = ext["library_version"]!!
val carthageBuildDir = "$projectDir/src/iosMain/native/Carthage/Build/iOS"

plugins {
    id("jacoco")
    id("maven-publish")
    id("com.android.library")
    kotlin("multiplatform")
}

dependencies {
    implementation("org.altbeacon:android-beacon-library:2.16.3")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
}

kotlin {
    for (target in targets.filterIsInstance<KotlinNativeTarget>()) {
        target.compilations {
                    val main by getting {
                        cinterops(Action {
                            val eddystone by creating {
                                defFile("src/iosMain/native/eddystone_generated.def")
                                includeDirs("$carthageBuildDir/Eddystone.framework/Headers")
                            }
                        })
                    }

            }
        target.mavenPublication {
            artifactId = "${project.name}-${target.name}"
        }
    }
}

// Create Carthage tasks
listOf("bootstrap", "update").forEach { type ->
    task<Exec>("carthage${type.capitalize()}") {
        group = "carthage"
        executable = "carthage"
        args(
            type,
            "--project-directory", "src/iosMain/native",
            "--platform", "iOS",
            "--no-use-binaries" // Provided binaries are sometimes problematic, remove this to speedup process
            //"--cache-builds"
        )
    }
}

tasks.create("generateDefFile") {
    val templateFile = File("$projectDir/src/iosMain/native/eddystone.def")
    val generatedFile = File("$projectDir/src/iosMain/native/eddystone_generated.def")
    generatedFile.writeText(templateFile.readText().replace("\$CARTHAGE_BUILD_PATH", carthageBuildDir))
}

// Make CInterop tasks depend on Carthage
tasks.withType(CInteropProcess::class) {
    dependsOn("carthageBootstrap")
    dependsOn("generateDefFile")
}

tasks.create("clearCarthageFiles", Delete::class.java) {
    delete(
        "$projectDir/src/iosMain/native/eddystone_generated.def",
        "$projectDir/src/iosMain/native/Carthage",
        "$projectDir/src/iosMain/native/Cartfile.resolved"
    )
}
tasks.named<Delete>("clean").dependsOn(tasks.named("clearCarthageFiles"))