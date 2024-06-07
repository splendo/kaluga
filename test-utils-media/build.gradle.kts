plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.media"
    dependencies {
        common {
            main {
                api(project(":media"))
                api(project(":test-utils-architecture"))
            }
        }
    }
}
