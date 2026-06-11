plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "example.core.koin"
    supportJVM = true
    supportMacOS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api("com.splendo.kaluga:logging:${project.rootProject.version}")
                api(libs.koin.core)
            }
        }
    }
}
