plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "keyboard.test"
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
