plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.microphone"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":permissions:core", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
