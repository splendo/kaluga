plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "permissions.base"
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                api(project(":logging", ""))
                api(project(":base", ""))
            }
            test {
                api(project(":test-utils-permissions", ""))
            }
        }
    }
}
