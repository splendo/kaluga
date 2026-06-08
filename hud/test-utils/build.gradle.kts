plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.hud"
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
