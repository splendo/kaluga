plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.resources"
    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":resources"))
            }
            test {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
