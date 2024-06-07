plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.contacts"
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
