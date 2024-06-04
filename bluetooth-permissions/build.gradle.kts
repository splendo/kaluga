plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.bluetooth"
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
                api(project(":test-utils-base", ""))
            }
        }
    }
}
