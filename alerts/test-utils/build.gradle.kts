plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.alerts"
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
