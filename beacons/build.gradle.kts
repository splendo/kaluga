plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "beacons"
    dependencies {
        common {
            main {
                implementation(project(":base"))
                api(project(":bluetooth"))
                api(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-bluetooth"))
            }
        }
    }
}
