plugins {
    id(libs.plugins.kotlin.jvm.get().pluginId)
}

group = "com.splendo.kaluga"
version = libs.versions.kaluga.get()

dependencies {
    implementation(project(":bluetooth-annotations"))
    implementation(libs.google.devtools.ksp.symbolProcessingAPI)
    implementation(libs.kotlinpoet.ksp)
}
