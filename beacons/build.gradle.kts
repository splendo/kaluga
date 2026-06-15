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
                implementation(project(":base"))
                api(project(":bluetooth-client"))
                api(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-bluetooth-client"))
            }
        }
    }
}
