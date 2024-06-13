plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions"
    dependencies {
        common {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":location-permissions"))
                api(project(":storage-permissions"))
                api(project(":notifications-permissions"))
                api(project(":contacts-permissions"))
                api(project(":microphone-permissions"))
                api(project(":camera-permissions"))
            }
            test {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
