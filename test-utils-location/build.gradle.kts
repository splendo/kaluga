plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.location"
    dependencies {
        common {
            main {
                api(project(":location"))
                api(project(":test-utils-permissions"))
                api(project(":test-utils-service"))
            }
        }
    }
}
