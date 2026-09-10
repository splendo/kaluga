plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "koin.test"

    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":architecture:test"))
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
            }
        }
    }
}
