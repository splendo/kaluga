/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

import java.nio.file.Files
import java.util.Calendar

abstract class NewModule : DefaultTask() {

    private companion object {
        const val VALID_NAME_REGEX = "^[^\\dA-Z-][a-z]+[a-z-]*\\Z"
        const val TEMPLATE_PATH = "adding-a-new-module/template"
        const val BUILD_GRADLE_KTS = "build.gradle.kts"
        val CREATE_DIRS = listOf(
            "androidLibAndroidTest" to listOf("kotlin/TestActivity.kt", "AndroidManifest.xml"),
            "androidLibMain" to listOf("AndroidManifest.xml"),
            "androidLibTest" to emptyList(),
            "commonMain" to emptyList(),
            "commonTest" to emptyList(),
            "iosMain" to emptyList(),
            "iosTest" to emptyList(),
            "jsMain" to emptyList(),
            "jvmMain" to emptyList()
        )
    }

    @get:Internal
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun create() {
        if (!outputDir.isPresent) {
            throw GradleException("No module name provided! Use -P module_name=my-awesome-module")
        }
        val outputDir = outputDir.get()
        val file = outputDir.asFile
        val module = file.name
        if (file.exists()) {
            throw GradleException("Module `$module` already exists!")
        }
        if (module.matches(Regex(VALID_NAME_REGEX))) {
            CREATE_DIRS.forEach { pair ->
                val dir = outputDir.dir("src/${pair.first}")
                val kotlinDir = dir.dir("kotlin")
                Files.createDirectories(kotlinDir.asFile.toPath())
                pair.second.forEach {
                    val from = outputDir.file("../$TEMPLATE_PATH/src/${pair.first}/$it").asFile
                    val to = dir.file(it).asFile
                    from.copyRecursively(to)
                    replaceVariable(to, module)
                }
            }
            val buildGradleFile = outputDir.file("../$TEMPLATE_PATH/$BUILD_GRADLE_KTS").asFile
            buildGradleFile.copyTo(outputDir.file(BUILD_GRADLE_KTS).asFile)
        } else {
            throw GradleException("`$module` is not valid module name!")
        }
        println("New module `$module` has been created:")
        outputDir.asFileTree.visit {
            println(this.file)
        }
    }

    private fun replaceVariable(template: File, value: String) {
        val content = template
            .readText()
            .replace("%MODULE%", value)
            .replace("%YEAR%", "${Calendar.getInstance().get(Calendar.YEAR)}")
        template.writeText(content)
    }
}

tasks.register<NewModule>("createNewModule") {
    group = "utils"
    if (project.hasProperty("module_name")) {
        outputDir.set(file(project.property("module_name").toString()))
    }
}
