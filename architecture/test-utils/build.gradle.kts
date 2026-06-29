plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "architecture.test"

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
