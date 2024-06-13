plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "service"

    supportJVM = true
    supportJS = true

    dependencies {
        common {
            main {
                implementation(project(":logging"))
                implementation(project(":base"))
            }
        }
    }
}
