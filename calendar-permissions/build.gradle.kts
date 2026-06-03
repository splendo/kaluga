plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.calendar"
    supportMacOS = true
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
