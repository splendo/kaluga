plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "bluetooth.client"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
                api(project(":location:location", ""))
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
                api(project(":date-time:date-time"))
                api(project(":permissions:bluetooth", ""))
            }
            test {
                implementation(project(":bluetooth:test-client", ""))
                implementation(project(":permissions:test", ""))
            }
        }
    }
}
