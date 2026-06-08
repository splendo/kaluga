plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "resources.test"
    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":resources:resources"))
            }
        }
    }
}
