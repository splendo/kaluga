plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.koin"

    supportJVM = true
    supportJS = true

    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":test-utils-architecture"))
                api(libs.koin.core)
            }
        }
    }
}
