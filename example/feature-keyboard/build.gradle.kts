plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.feature.keyboard"
    appleFramework {
        export("com.splendo.kaluga:keyboard:${project.rootProject.version}")
        export("com.splendo.kaluga:architecture:${project.rootProject.version}")
    }
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-stylable"))
                api("com.splendo.kaluga:architecture:${project.rootProject.version}")
                api("com.splendo.kaluga:keyboard:${project.rootProject.version}")
            }
        }
    }
}
