plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.core.stylable"
    dependencies {
        common {
            main {
                api("com.splendo.kaluga:resources:${project.rootProject.version}")
            }
        }
    }
}
