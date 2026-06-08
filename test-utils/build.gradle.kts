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
                api(project(":bluetooth:test:core"))
                api(project(":bluetooth:test:client"))
                api(project(":bluetooth:test:server"))
                api(project(":test-utils-date-time-picker"))
                api(project(":test-utils-hud"))
                api(project(":keyboard:test"))
                api(project(":test-utils-koin"))
                api(project(":lifecycle:test"))
                api(project(":test-utils-location"))
                api(project(":media:test"))
                api(project(":permissions:test"))
                api(project(":resources:test"))
                api(project(":test-utils-service"))
                api(project(":test-utils-system"))
            }
        }
    }
}
