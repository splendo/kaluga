plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.camera"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":permissions:core", ""))
            }
            test {
                implementation(project(":base:test", ""))
            }
        }
    }
}
