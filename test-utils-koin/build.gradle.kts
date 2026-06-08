plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test.koin"

    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":architecture:test"))
                api(libs.koin.core)
            }
        }
    }
}
