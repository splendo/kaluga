plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "architecture"
    dependencies {
        android {
            main {
                api(libs.androidx.lifecycle.runtime)
                api(libs.androidx.lifecycle.viewmodel)
                api(libs.androidx.lifecycle.livedata)
                api(libs.android.material)
                implementation(libs.androidx.browser)
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(project(":base:core", ""))
                api(project(":date-time:date-time", ""))
                api(project(":lifecycle:lifecycle", ""))
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.serialization.json)
            }
            test {
                implementation(project(":architecture:test", ""))
                implementation(project(":lifecycle:test", ""))
            }
        }
    }
}
