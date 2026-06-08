plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.datetimepicker"
    dependencies {
        common {
            main {
                api(project(":date-time-picker:date-time-picker"))
                api(project(":base:test"))
                api(project(":lifecycle:test"))
            }
        }
    }
}
