plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.media"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":media"))
                api(project(":test-utils-base"))
            }
        }
    }
}
