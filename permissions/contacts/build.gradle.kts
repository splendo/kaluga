plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.contacts"
    supportMacOS = true
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
