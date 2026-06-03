plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.lifecycle"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    supportJVM = true
    supportJS = true
    dependencies {
        common {
            main {
                api(project(":lifecycle"))
                api(project(":test-utils-base"))
            }
        }
    }
}
