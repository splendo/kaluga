plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "bluetooth.client"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
                api(project(":location", ""))
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
                api(project(":bluetooth-base"))
                api(project(":bluetooth-permissions", ""))
            }
            test {
                implementation(project(":test-utils-bluetooth-client", ""))
                implementation(project(":test-utils-permissions", ""))
            }
        }
    }
}
