plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "system"
    supportMacOS = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                implementation(project(":base:core", ""))
                api(project(":base:state", ""))
                implementation(project(":logging", ""))
            }
            test {
                implementation(project(":system:test", ""))
            }
        }
    }
}
