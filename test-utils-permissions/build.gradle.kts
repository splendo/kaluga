plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.permissions"
    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":permissions"))
            }
        }
    }
}
