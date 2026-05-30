plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.feature.media"
    appleFramework {
        export("com.splendo.kaluga:media:${project.rootProject.version}")
        export("com.splendo.kaluga:architecture:${project.rootProject.version}")
        export("com.splendo.kaluga:alerts:${project.rootProject.version}")
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
                api("com.splendo.kaluga:architecture:${project.rootProject.version}")
                api("com.splendo.kaluga:alerts:${project.rootProject.version}")
                api("com.splendo.kaluga:media:${project.rootProject.version}")
            }
        }
    }
}
