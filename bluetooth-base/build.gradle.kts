plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.base"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
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
                api(project(":base"))
                api(project(":service"))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
