plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.alerts"
    dependencies {
        common {
            main {
                api(project(":test-utils-architecture"))
                implementation(project(":alerts"))
            }
        }
    }
}
