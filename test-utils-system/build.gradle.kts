plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.system"
    dependencies {
        common {
            main {
                api(project(":system"))
                api(project(":test-utils-base"))
            }
        }
    }
}
