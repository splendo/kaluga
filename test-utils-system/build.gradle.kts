plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.system"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":system"))
                api(project(":test-utils-base"))
            }
        }
    }
}
