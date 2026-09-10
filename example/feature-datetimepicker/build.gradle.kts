plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.feature.datetimepicker"
    appleFramework {
        export("com.splendo.kaluga.date-time-picker:date-time-picker:${project.rootProject.version}")
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
                api("com.splendo.kaluga.date-time-picker:date-time-picker:${project.rootProject.version}")
            }
        }
    }
}
