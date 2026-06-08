plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.resources"
    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":resources:resources"))
            }
        }
    }
}
