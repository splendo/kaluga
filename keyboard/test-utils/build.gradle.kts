plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.keyboard"
    dependencies {
        common {
            main {
                api(project(":keyboard:keyboard"))
                api(project(":base:test"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
