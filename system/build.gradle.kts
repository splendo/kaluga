plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "system"
    dependencies {
        common {
            main {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
            }
            test {
                implementation(project(":test-utils-system", ""))
            }
        }
    }
}
