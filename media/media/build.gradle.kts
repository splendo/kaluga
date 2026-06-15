plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "media"
    supportMacOS = true
    supportTvOS = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                implementation(project(":base:base"))
                implementation(project(":logging"))
            }
            test {
                implementation(project(":media:test"))
            }
        }
    }
}
