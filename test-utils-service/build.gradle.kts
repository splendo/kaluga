plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.service"

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
