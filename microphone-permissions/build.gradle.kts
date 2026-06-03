plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.microphone"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
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
