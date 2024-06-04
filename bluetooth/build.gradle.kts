plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth"
    dependencies {
        android {
            main {
                implementation(libs.nordic.support.scanner)
                implementation(project(":location", ""))
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(project(":service"))
                api(project(":bluetooth-permissions", ""))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":test-utils-bluetooth", ""))
            }
        }
    }
}
