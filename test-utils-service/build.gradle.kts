plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.service"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true

    dependencies {
        common {
            main {
                api(project(":service"))
                api(project(":test-utils-base"))
            }
        }
    }
}
