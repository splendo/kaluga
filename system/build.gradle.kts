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
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-system", ""))
            }
        }
    }
}
