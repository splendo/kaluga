plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    `version-catalog`
    `maven-publish`
    alias(libs.plugins.kotlinter)
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

gradlePlugin {
    plugins.register("com.splendo.kaluga.example.plugin") {
        id = "com.splendo.kaluga.example.plugin"
        implementationClass = "com.splendo.kaluga.example.plugin.EmbeddingPlugin"
    }
    plugins.register("com.splendo.kaluga.example.setting.plugin") {
        id = "com.splendo.kaluga.example.settings.plugin"
        implementationClass = "com.splendo.kaluga.example.plugin.EmbeddingSettingsPlugin"
    }
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}
