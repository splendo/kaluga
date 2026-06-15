plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "system.test"
    supportMacOS = true
    supportJS = true
    supportWasmJS = true
    dependencies {
        common {
            main {
                api(project(":system:system"))
                api(project(":base:test"))
            }
        }
    }
}
