plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "hud.test"
    dependencies {
        common {
            main {
                api(project(":hud:hud"))
                api(project(":base:test"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
