plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "bluetooth.server"
    supportMacOS = true
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        ios {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                api(project(":bluetooth:core"))
                api(project(":permissions:bluetooth", ""))
                api(project(":base"))
            }
            test {
                implementation(project(":test-utils-base", ""))
                implementation(project(":bluetooth:test:server", ""))
                implementation(project(":permissions:test", ""))
            }
        }
    }
}
