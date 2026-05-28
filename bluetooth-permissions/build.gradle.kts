plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.bluetooth"
    supportMacOS = true
    dependencies {
        android {
            main {
                api(project(":location-permissions", ""))
            }
        }
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
