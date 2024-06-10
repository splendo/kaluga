plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "test.architecture"

    supportJVM = true
    supportJS = true

    dependencies {
        common {
            main {
                api(project(":test-utils-base"))
                api(project(":architecture"))
            }
        }
    }
}
