package com.splendo.kaluga.example.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.create
import java.io.File
import java.io.FileInputStream
import java.util.Properties

abstract class BaseEmbeddingPlugin {
    fun apply(container: ExtensionContainer, rootDir: File, logger: Logger) {
        val props = Properties()
        val file = File(rootDir, "/../local.properties")
        if (file.exists()) {
            props.load(FileInputStream(file))
        }
        container.create("embedding", EmbeddingModeExtension::class, props, logger)
    }
}

class EmbeddingPlugin : BaseEmbeddingPlugin(), Plugin<Project> {

    override fun apply(target: Project) = target.run {
        apply(extensions, target.rootDir, target.logger)
    }
}

class EmbeddingSettingsPlugin : BaseEmbeddingPlugin(), Plugin<Settings> {

    override fun apply(target: Settings) = target.run {
        apply(extensions, target.rootDir, Logging.getLogger(Settings::class.java))
    }
}
