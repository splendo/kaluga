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

    sealed class Templates {
        abstract val subpath: String
        abstract fun createDirs(includeJVM: Boolean, includeJS: Boolean): Map<String, List<String>>

        object Common : Templates() {
            override val subpath: String = "common"
            override fun createDirs(includeJVM: Boolean, includeJS: Boolean) = listOfNotNull(
                "androidLibInstrumentedTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                "androidLibMain" to emptyList(),
                "androidLibUnitTest" to emptyList(),
                "commonMain" to emptyList(),
                "commonTest" to emptyList(),
                "iosMain" to emptyList(),
                "iosTest" to emptyList(),
                if (includeJS) { "jsMain" to emptyList() } else { null },
                if (includeJVM) { "jvmMain" to emptyList() } else { null },
            ).toMap()
        }

        object Test : Templates() {
            override val subpath: String = "test"
            override fun createDirs(includeJVM: Boolean, includeJS: Boolean) = listOfNotNull<Pair<String, List<String>>>(
                "androidLibMain" to emptyList(),
                "commonMain" to emptyList(),
                "iosMain" to emptyList(),
                if (includeJS) { "jsMain" to emptyList() } else { null },
                if (includeJVM) { "jvmMain" to emptyList() } else { null },
            ).toMap()
        }

        object Compose : Templates() {
            override val subpath: String = "compose"
            override fun createDirs(includeJVM: Boolean, includeJS: Boolean) = mapOf(
                "androidTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                "main" to emptyList(),
                "test" to emptyList()
            )
        }

        object Databinding : Templates() {
            override val subpath: String = "databinding"
            override fun createDirs(includeJVM: Boolean, includeJS: Boolean) = mapOf(
                "androidTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
                "main" to emptyList(),
                "test" to emptyList()
            )
        }
    }

    data class Configuration(
        val baseModuleName: String,
        val basePackageName: String,
        val includeJVM: Boolean,
        val includeJS: Boolean,
        val template: Templates,
    ) {
        val moduleName: String get() = when (template) {
            is Templates.Common -> baseModuleName
            is Templates.Test -> "test-utils-$baseModuleName"
            is Templates.Compose -> "$baseModuleName-compose"
            is Templates.Databinding -> "$baseModuleName-databinding"
        }

        val packageName: String get() = when (template) {
            is Templates.Common -> basePackageName
            is Templates.Test -> "test.$basePackageName"
            is Templates.Compose -> "$basePackageName.compose"
            is Templates.Databinding -> "$basePackageName.databinding"
        }

        val targetConfig: String get() = buildString {
            if (includeJS || includeJVM) {
                appendLine()
            }
            if (includeJVM) {
                appendLine("\tsupportJVM = true")
            }
            if (includeJS) {
                appendLine("\tsupportJS = true")
            }
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
        val outputDir = rootDir.get().dir(moduleName)
        val file = outputDir.asFile
        val module = file.name
        if (file.exists()) {
            throw GradleException("Module `$module` already exists!")
        }

        when {
            !module.matches(Regex(VALID_MODULE_NAME_REGEX)) -> throw GradleException("`$module` is not valid module name!")
            !packageName.matches(Regex(VALID_PACKAGE_NAME_REGEX)) -> throw GradleException("`$packageName` is not a valid package name!")
            else -> {
                val basePath = "../$TEMPLATE_PATH/${template.subpath}"
                template.createDirs(false, false).entries.forEach { (path, files) ->
                    val dir = outputDir.dir("src/${path}")
                    val kotlinDir = dir.dir("kotlin")
                    Files.createDirectories(kotlinDir.asFile.toPath())
                    files.forEach {
                        val from = outputDir.file("$basePath/src/${path}/$it").asFile
                        val to = dir.file(it).asFile
                        from.copyRecursively(to)
                        replaceVariable(to, this)
                    }
                }
                val buildGradleFile = outputDir.file("$basePath/$BUILD_GRADLE_KTS").asFile
                val to = outputDir.file(BUILD_GRADLE_KTS).asFile
                buildGradleFile.copyTo(to)
                replaceVariable(to, this)
            }
        }
        logger.lifecycle("New module `$module` has been created:")
        outputDir.asFileTree.visit {
            logger.lifecycle(this.file.canonicalPath)
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

tasks.register("createNewModule", NewModule::class) {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        val moduleName = project.property("module_name").toString()
        val packageName = project.property(if (project.hasProperty("package_name")) "package_name" else "module_name").toString()
        val includeJVM = project.hasProperty("include-jvm")
        val includeJS = project.hasProperty("include-js")
        configurations.set(
            listOfNotNull(
                NewModule.Configuration(moduleName, packageName, includeJVM, includeJS, NewModule.Templates.Common),
                if (project.hasProperty("create-test-utils")) {
                    NewModule.Configuration(moduleName, packageName, includeJVM, includeJS, NewModule.Templates.Test)
                } else null,
                if (project.hasProperty("create-compose")) {
                    NewModule.Configuration(moduleName, packageName, includeJVM = false, includeJS = false, template = NewModule.Templates.Compose)
                } else null,
                if (project.hasProperty("create-databinding")) {
                    NewModule.Configuration(moduleName, packageName, includeJVM = false, includeJS = false, template = NewModule.Templates.Databinding)
                } else null
            )
        )
    }
}

tasks.register<NewModule>("createNewTestModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        val moduleName = project.property("module_name").toString()
        val packageName = project.property(if (project.hasProperty("package_name")) "package_name" else "module_name").toString()
        val includeJVM = project.hasProperty("include-jvm")
        val includeJS = project.hasProperty("include-js")
        configurations.set(
            listOf(NewModule.Configuration(moduleName, packageName, includeJVM, includeJS, NewModule.Templates.Test))
        )
    }
}

tasks.register<NewModule>("createNewComposeModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        val moduleName = project.property("module_name").toString()
        val packageName = project.property(if (project.hasProperty("package_name")) "package_name" else "module_name").toString()
        configurations.set(
            listOf(NewModule.Configuration(moduleName, packageName, includeJVM = false, includeJS = false, template = NewModule.Templates.Compose))
        )
    }
}

tasks.register<NewModule>("createNewDataBindingModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        rootDir.set(file("./"))
        val moduleName = project.property("module_name").toString()
        val packageName = project.property(if (project.hasProperty("package_name")) "package_name" else "module_name").toString()
        configurations.set(
            listOf(NewModule.Configuration(moduleName, packageName, includeJVM = false, includeJS = false, template = NewModule.Templates.Databinding))
        )
    }
}
