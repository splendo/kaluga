plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth"
    supportMacOS = true
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
                implementation(project(":location", ""))
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
                api(project(":bluetooth-client"))
                api(project(":bluetooth-server"))
                implementation(project(":service"))
                api(project(":bluetooth-permissions", ""))
                api(libs.kotlinx.serialization.core)
            }
        }
    }
}
