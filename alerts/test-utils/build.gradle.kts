plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "alerts.test"
    dependencies {
        common {
            main {
                implementation(project(":alerts:alerts"))
                api(project(":base:test"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
