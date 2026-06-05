plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "service"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    supportJVM = true
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            main {
                implementation(project(":logging"))
                implementation(project(":base"))
            }
        }
    }
}
