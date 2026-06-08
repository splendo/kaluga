plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.hud"
    dependencies {
        common {
            main {
                api(project(":hud"))
                api(project(":test-utils-base"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
