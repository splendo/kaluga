plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.microphone"
    dependencies {
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
