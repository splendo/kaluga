plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.feature.beacons"
    appleFramework {
        export("com.splendo.kaluga:beacons:${project.rootProject.version}")
        export("com.splendo.kaluga:architecture:${project.rootProject.version}")
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
                api(project(":feature-bluetooth"))
                api("com.splendo.kaluga:architecture:${project.rootProject.version}")
                api("com.splendo.kaluga:beacons:${project.rootProject.version}")
                api("com.splendo.kaluga:resources:${project.rootProject.version}")
            }
        }
    }
}
