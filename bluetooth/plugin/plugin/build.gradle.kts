import com.palantir.gradle.gitversion.VersionDetails
import org.gradle.internal.extensions.core.extra
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `version-catalog`
    `maven-publish`
    alias(libs.plugins.plugin.publish)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.palantir.git.version)
}

repositories {
    gradlePluginPortal() // To use 'maven-publish' and 'signing' plugins in our own plugin
    google()
    mavenCentral()
}

group = "com.splendo.kaluga.bluetooth"
version = kalugaVersion

gradlePlugin {
    website = "https://github.com/splendo/kaluga"
    vcsUrl = "https://github.com/splendo/kaluga.git"
    plugins.register("com.splendo.kaluga.bluetooth.plugin") {
        id = "com.splendo.kaluga.bluetooth.plugin"
        implementationClass = "com.splendo.kaluga.bluetooth.plugin.BluetoothPlugin"
        displayName = "Kaluga Bluetooth code generation"
        description = "Generates typed Bluetooth clients and servers from annotated device definitions."
        tags = listOf("kotlin", "kotlin-multiplatform", "bluetooth", "ble", "ksp", "codegen")
    }
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.google.devtools.ksp.gradle)
    implementation(libs.kotlinpoet)
    implementation(libs.snakeyaml)
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_2_4)
    }
}

val generatePluginVersion by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/resources/main")
    val outputFile = outputDir.map { it.file("bluetooth.properties") }

    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()

        Properties().apply {
            setProperty("kalugaVersion", kalugaVersion)
            file.outputStream().use { store(it, null) }
        }
    }
}

tasks.named<Jar>("jar") {
    dependsOn(generatePluginVersion)
    from(layout.buildDirectory.dir("generated/resources/main"))
}

internal fun calculateVersion(
    version: String,
    releaseType: String,
    branchName: String?,
    gitHash: String?,
    buildNumber: String?,
): String {
    var appendix = when (releaseType) {
        "release" -> ""
        "-branch-alpha" -> branchName?.let { branch ->
            val sanitizedBranch = branch.substringAfterLast('/')
                .replace(' ', '-')
                .replace('_', '-')
                .filter { it.isLetterOrDigit() || it == '-' }
                .split('-')
                .filterNot { it.all(Char::isDigit) } // filter out purely numerics such as issue numbers
                .joinToString("-")
            "-$sanitizedBranch-alpha"
        } ?: error("Trying to append a branch name to the version, but no branch is present.")
        "-commit-alpha" -> "-$gitHash-alpha"
        else -> "-alpha"
    }

    if (appendix.isNotEmpty() && buildNumber != null) {
        appendix += ".$buildNumber"
    }

    return "$version$appendix"
}

val kalugaVersion: String
    get() {
        val releaseTypeProvider = providers.gradleProperty("releaseType")
        val releaseType = releaseTypeProvider.getOrElse("alpha")

        val versionDetails: groovy.lang.Closure<VersionDetails> by project.extra
        val details = versionDetails()
        val buildNumberProvider = providers.environmentVariable("GITHUB_RUN_NUMBER")

        val version = libs.versions.kaluga.get()

        return calculateVersion(
            version = version,
            releaseType = releaseType,
            branchName = details.branchName,
            gitHash = details.gitHash,
            buildNumber = buildNumberProvider.orNull,
        )
    }
