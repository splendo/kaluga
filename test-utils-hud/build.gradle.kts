plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.hud"
    dependencies {
        common {
            main {
                api(project(":test-utils-architecture"))
                api(project(":hud"))
            }
        }
    }
}
