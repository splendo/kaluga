plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.keyboard"
    dependencies {
        common {
            main {
                api(project(":test-utils-architecture"))
                api(project(":keyboard"))
            }
        }
    }
}
