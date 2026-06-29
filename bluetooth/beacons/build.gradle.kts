plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "beacons"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        common {
            main {
                implementation(project(":base:core"))
                api(project(":date-time:date-time"))
                api(project(":bluetooth:client"))
                api(project(":logging", ""))
            }
            test {
                implementation(project(":bluetooth:test-client"))
            }
        }
    }
}
