plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.camera"
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
