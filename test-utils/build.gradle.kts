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
                api(project(":test-utils-keyboard"))
                api(project(":test-utils-koin"))
                api(project(":test-utils-lifecycle"))
                api(project(":test-utils-location"))
                api(project(":test-utils-media"))
                api(project(":test-utils-permissions"))
                api(project(":test-utils-resources"))
                api(project(":test-utils-service"))
                api(project(":test-utils-system"))
            }
        }
    }
}
