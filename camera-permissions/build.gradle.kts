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
                api(project(":base-permissions", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
