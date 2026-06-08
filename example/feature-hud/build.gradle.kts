plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.feature.hud"
    appleFramework {
        export("com.splendo.kaluga:hud:${project.rootProject.version}")
        export("com.splendo.kaluga.architecture:architecture:${project.rootProject.version}")
    }
    dependencies {
        android {
            main {
                api(libs.koin.compose.viewmodel)
            }
        }
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-stylable"))
                api("com.splendo.kaluga.architecture:architecture:${project.rootProject.version}")
                api("com.splendo.kaluga:hud:${project.rootProject.version}")
            }
        }
    }
}
