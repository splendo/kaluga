/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.util.Calendar

abstract class NewModule : DefaultTask() {

    private companion object {
        const val VALID_MODULE_NAME_REGEX = "^[^\\dA-Z-][a-z]+[a-z-]*\\Z"
        const val VALID_PACKAGE_NAME_REGEX = "^[a-z]+(\\.[a-z]+)*\\Z"
        const val TEMPLATE_PATH = "adding-a-new-module/template"
        const val BUILD_GRADLE_KTS = "build.gradle.kts"
    }

    // Optional Kotlin targets a module can opt into. Android and iOS are always present.
    // Shared Apple code (macOS / tvOS / watchOS) lives in `appleMain`; iOS-only code in `iosMain`.
    data class Targets(
        val jvm: Boolean,
        val js: Boolean,
        val wasmJs: Boolean,
        val macos: Boolean,
        val tvos: Boolean,
        val watchos: Boolean,
    ) {
        val anyExtraApple: Boolean get() = macos || tvos || watchos
        val any: Boolean get() = jvm || js || wasmJs || anyExtraApple

        companion object {
            val NONE = Targets(jvm = false, js = false, wasmJs = false, macos = false, tvos = false, watchos = false)
        }
    }

    sealed class Templates {
        abstract val subpath: String
        abstract fun createDirs(targets: Targets): Map<String, List<String>>

        protected fun Targets.optionalSourceSets(): List<Pair<String, List<String>>> = listOfNotNull(
            if (anyExtraApple) { "appleMain" to emptyList() } else { null },
            if (macos) { "macosMain" to emptyList() } else { null },
            if (tvos) { "tvosMain" to emptyList() } else { null },
            if (watchos) { "watchosMain" to emptyList() } else { null },
            if (js) { "jsMain" to emptyList() } else { null },
            if (wasmJs) { "wasmJsMain" to emptyList() } else { null },
            if (jvm) { "jvmMain" to emptyList() } else { null },
        )

        object Common : Templates() {
            override val subpath: String = "common"
            override fun createDirs(targets: Targets) = (
                listOf(
                    "androidInstrumentedTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                    "androidMain" to emptyList(),
                    "androidUnitTest" to emptyList(),
                    "commonMain" to emptyList(),
                    "commonTest" to emptyList(),
                    "iosMain" to emptyList(),
                    "iosTest" to emptyList(),
                ) + targets.optionalSourceSets()
                ).toMap()
        }

        object Test : Templates() {
            override val subpath: String = "test"
            override fun createDirs(targets: Targets) = (
                listOf<Pair<String, List<String>>>(
                    "androidMain" to emptyList(),
                    "commonMain" to emptyList(),
                    "iosMain" to emptyList(),
                ) + targets.optionalSourceSets()
                ).toMap()
        }

        object Compose : Templates() {
            override val subpath: String = "compose"
            override fun createDirs(targets: Targets) = mapOf(
                "androidTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                "main" to emptyList(),
                "test" to emptyList()
            )
        }

        object Databinding : Templates() {
            override val subpath: String = "databinding"
            override fun createDirs(targets: Targets) = mapOf(
                "androidTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                "main" to emptyList(),
                "test" to emptyList()
            )
        }
    }

    data class Configuration(
        val baseModuleName: String,
        val basePackageName: String,
        val targets: Targets,
        val template: Templates,
    ) {
        // Directory of the new module, nested under its feature group `baseModuleName`
        // (AndroidX-style). The test module lives in `test-utils` but publishes as `test`.
        val relativePath: String get() = when (template) {
            is Templates.Common -> "$baseModuleName/$baseModuleName"
            is Templates.Test -> "$baseModuleName/test-utils"
            is Templates.Compose -> "$baseModuleName/compose"
            is Templates.Databinding -> "$baseModuleName/databinding"
        }

        // Gradle path of the new module (group `baseModuleName` + artifact).
        val gradlePath: String get() = when (template) {
            is Templates.Common -> ":$baseModuleName:$baseModuleName"
            is Templates.Test -> ":$baseModuleName:test"
            is Templates.Compose -> ":$baseModuleName:compose"
            is Templates.Databinding -> ":$baseModuleName:databinding"
        }

        // The `test` artifact keeps living in the `test-utils` directory, so its include
        // needs a matching `projectDir` override in settings.gradle.kts.
        val projectDirOverride: String? get() = (template as? Templates.Test)?.let { relativePath }

        val packageName: String get() = when (template) {
            is Templates.Common -> basePackageName
            is Templates.Test -> "$basePackageName.test"
            is Templates.Compose -> "$basePackageName.compose"
            is Templates.Databinding -> "$basePackageName.databinding"
        }

        val targetConfig: String get() = buildString {
            if (targets.any) {
                appendLine()
            }
            if (targets.jvm) appendLine("\tsupportJVM = true")
            if (targets.js) appendLine("\tsupportJS = true")
            if (targets.wasmJs) appendLine("\tsupportWasmJS = true")
            if (targets.macos) appendLine("\tsupportMacOS = true")
            if (targets.tvos) appendLine("\tsupportTvOS = true")
            if (targets.watchos) appendLine("\tsupportWatchOS = true")
        }
    }

    @get:Internal
    abstract val rootDir: DirectoryProperty
    @get:Internal
    abstract val configurations: ListProperty<Configuration>

    @TaskAction
    fun create() {
        if (!configurations.isPresent || configurations.get().isEmpty()) {
            throw GradleException("No module name provided! Use -P module_name=my-awesome-module")
        }

        configurations.get().forEach { it.createModule() }
    }

    private fun Configuration.createModule() {
        val outputDir = rootDir.get().dir(relativePath)
        val file = outputDir.asFile
        if (file.exists()) {
            throw GradleException("Module `$relativePath` already exists!")
        }

        when {
            !baseModuleName.matches(Regex(VALID_MODULE_NAME_REGEX)) -> throw GradleException("`$baseModuleName` is not a valid module name!")
            !packageName.matches(Regex(VALID_PACKAGE_NAME_REGEX)) -> throw GradleException("`$packageName` is not a valid package name!")
            else -> {
                val templateDir = rootDir.get().dir("$TEMPLATE_PATH/${template.subpath}")
                template.createDirs(targets).entries.forEach { (path, files) ->
                    val dir = outputDir.dir("src/$path")
                    val kotlinDir = dir.dir("kotlin")
                    Files.createDirectories(kotlinDir.asFile.toPath())
                    files.forEach {
                        val from = templateDir.file("src/$path/$it").asFile
                        val to = dir.file(it).asFile
                        from.copyRecursively(to)
                        replaceVariable(to, this)
                    }
                }
                val buildGradleFile = templateDir.file(BUILD_GRADLE_KTS).asFile
                val to = outputDir.file(BUILD_GRADLE_KTS).asFile
                buildGradleFile.copyTo(to)
                replaceVariable(to, this)
            }
        }
        logger.lifecycle("New module `$gradlePath` has been created at `$relativePath`:")
        outputDir.asFileTree.visit {
            logger.lifecycle(this.file.canonicalPath)
        }
        logger.lifecycle("Add the following to settings.gradle.kts:")
        logger.lifecycle("    include(\"$gradlePath\")")
        projectDirOverride?.let {
            logger.lifecycle("    project(\"$gradlePath\").projectDir = file(\"$it\")")
        }
    }

    private fun replaceVariable(template: File, configuration: Configuration) {
        val content = template
            .readText()
            .replace("%PACKAGE%", configuration.packageName)
            .replace("%BASEMODULE%", configuration.baseModuleName)
            .replace("%TARGET_CONFIG%", configuration.targetConfig)
            .replace("%YEAR%", "${Calendar.getInstance().get(Calendar.YEAR)}")
        template.writeText(content)
    }
}

fun Project.moduleTargets() = NewModule.Targets(
    jvm = hasProperty("include-jvm"),
    js = hasProperty("include-js"),
    wasmJs = hasProperty("include-wasm-js"),
    macos = hasProperty("include-macos"),
    tvos = hasProperty("include-tvos"),
    watchos = hasProperty("include-watchos"),
)

fun Project.newModuleName() = property("module_name").toString()

fun Project.newPackageName() = property(if (hasProperty("package_name")) "package_name" else "module_name").toString()

tasks.register("createNewModule", NewModule::class) {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        val moduleName = project.newModuleName()
        val packageName = project.newPackageName()
        val targets = project.moduleTargets()
        configurations.set(
            listOfNotNull(
                NewModule.Configuration(moduleName, packageName, targets, NewModule.Templates.Common),
                if (project.hasProperty("create-test-utils")) {
                    NewModule.Configuration(moduleName, packageName, targets, NewModule.Templates.Test)
                } else null,
                if (project.hasProperty("create-compose")) {
                    NewModule.Configuration(moduleName, packageName, NewModule.Targets.NONE, NewModule.Templates.Compose)
                } else null,
                if (project.hasProperty("create-databinding")) {
                    NewModule.Configuration(moduleName, packageName, NewModule.Targets.NONE, NewModule.Templates.Databinding)
                } else null
            )
        )
    }
}

tasks.register<NewModule>("createNewTestModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        configurations.set(
            listOf(NewModule.Configuration(project.newModuleName(), project.newPackageName(), project.moduleTargets(), NewModule.Templates.Test))
        )
    }
}

tasks.register<NewModule>("createNewComposeModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        configurations.set(
            listOf(NewModule.Configuration(project.newModuleName(), project.newPackageName(), NewModule.Targets.NONE, NewModule.Templates.Compose))
        )
    }
}

tasks.register<NewModule>("createNewDataBindingModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        configurations.set(
            listOf(NewModule.Configuration(project.newModuleName(), project.newPackageName(), NewModule.Targets.NONE, NewModule.Templates.Databinding))
        )
    }
}
