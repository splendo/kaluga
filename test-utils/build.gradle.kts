plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test"
    dependencies {
        common {
            main {
                api(project(":test-utils-alerts"))
                api(project(":architecture:test"))
                api(project(":test-utils-base"))
                api(project(":test-utils-bluetooth-base"))
                api(project(":test-utils-bluetooth-client"))
                api(project(":test-utils-bluetooth-server"))
                api(project(":test-utils-date-time-picker"))
                api(project(":test-utils-hud"))
                api(project(":keyboard:test"))
                api(project(":test-utils-koin"))
                api(project(":lifecycle:test"))
                api(project(":test-utils-location"))
                api(project(":media:test"))
                api(project(":test-utils-permissions"))
                api(project(":resources:test"))
                api(project(":test-utils-service"))
                api(project(":test-utils-system"))
            }
        }
    }
}
