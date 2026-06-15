plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.alerts"
    dependencies {
        common {
            main {
                implementation(project(":alerts"))
                api(project(":test-utils-base"))
                api(project(":test-utils-lifecycle"))
            }
        }
    }
}
