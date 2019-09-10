pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                val default_version = "1.3.50"
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version ?: default_version}")
            }
            if (requested.id.id == "com.android.library") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}



enableFeaturePreview("GRADLE_METADATA")

if (gradle is ExtensionAware) {
    val extension = gradle as ExtensionAware
    extension.extra["kotlin_version"] = "1.3.50" // when changing this also change the build.gradle multiplatform plugin version,
    extension.extra["kotlinx_corountines_version"] = "1.3.0"
    extension.extra["library_version"] = "0.0.2"
    extension.extra["android_version"] = 28
    extension.extra["one_ios_sourceset"] = true
}



include(":KotlinNativeFramework")
project(":KotlinNativeFramework").projectDir = file("../KotlinNativeFramework")

include(":shared")
project(":shared").projectDir = file("../../shared")

/* uncomment this to also include the main project in the build. This way a direct dependency can be made

include(":root")
project(":root").projectDir = file("../../..")

include(":Components")
project(":Components").projectDir = file("../../../Components")

/**/



rootProject.name = file("..").name
