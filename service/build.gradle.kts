plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "service"
    dependencies {
        common {
            main {
                implementation(project(":logging"))
                implementation(project(":base"))
            }
        }
    }
}
