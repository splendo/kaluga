plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.datetimepicker"
    dependencies {
        common {
            main {
                api(project(":date-time-picker"))
                api(project(":test-utils-base"))
            }
        }
    }
}
