plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.resources"
    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":resources:resources"))
            }
        }
    }
}
