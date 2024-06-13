plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetime.timer"

    supportJVM = true
    supportJS = true

    dependencies {
        common {
            main {
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
