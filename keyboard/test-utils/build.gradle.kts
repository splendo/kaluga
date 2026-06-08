plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.keyboard"
    dependencies {
        common {
            main {
                api(project(":keyboard:keyboard"))
                api(project(":test-utils-base"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
