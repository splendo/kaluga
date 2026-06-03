plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                api(project(":base-permissions"))
            }
            test {
                implementation(project(":test-utils-base"))
            }
        }
        android {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":camera-permissions"))
                api(project(":contacts-permissions"))
                api(project(":location-permissions"))
                api(project(":microphone-permissions"))
                api(project(":notifications-permissions"))
                api(project(":storage-permissions"))
            }
        }
        ios {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":camera-permissions"))
                api(project(":contacts-permissions"))
                api(project(":location-permissions"))
                api(project(":microphone-permissions"))
                api(project(":notifications-permissions"))
                api(project(":storage-permissions"))
            }
        }
        macos {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":camera-permissions"))
                api(project(":contacts-permissions"))
                api(project(":location-permissions"))
                api(project(":microphone-permissions"))
                api(project(":notifications-permissions"))
                api(project(":storage-permissions"))
            }
        }
        tvos {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":location-permissions"))
                api(project(":microphone-permissions"))
                api(project(":notifications-permissions"))
                api(project(":storage-permissions"))
            }
        }
        watchos {
            main {
                api(project(":bluetooth-permissions"))
                api(project(":calendar-permissions"))
                api(project(":contacts-permissions"))
                api(project(":location-permissions"))
                api(project(":microphone-permissions"))
                api(project(":notifications-permissions"))
            }
        }
    }
}
