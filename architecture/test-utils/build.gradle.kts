plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "test.architecture"

    dependencies {
        common {
            main {
                api(project(":base:test"))
                api(project(":architecture:architecture"))
                implementation(libs.kotlinx.atomicfu)
            }
        }
        js {
            main {
                api(libs.kotlinx.atomicfu)
            }
        }
    }
}
