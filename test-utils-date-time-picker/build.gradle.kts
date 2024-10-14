plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.datetimepicker"
    dependencies {
        common {
            main {
                api(project(":test-utils-architecture"))
                api(project(":date-time-picker"))
            }
        }
    }
}
