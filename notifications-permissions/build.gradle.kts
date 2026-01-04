plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.notifications"
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
