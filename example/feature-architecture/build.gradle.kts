plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "example.feature.architecture"
    appleFramework {
        export("com.splendo.kaluga.architecture:architecture:${project.rootProject.version}")
    }
    dependencies {
        android {
            main {
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-stylable"))
                api("com.splendo.kaluga.architecture:architecture:${project.rootProject.version}")
            }
        }
    }
}
