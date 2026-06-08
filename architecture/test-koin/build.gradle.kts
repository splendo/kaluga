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
                api(libs.koin.core)
            }
        }
    }
}
