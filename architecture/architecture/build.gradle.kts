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
                api(androidxLibs.lifecycle.lifecycleRuntimeKtx)
                api(androidxLibs.lifecycle.lifecycleViewmodelKtx)
                api(androidxLibs.lifecycle.lifecycleLivedataKtx)
                api(libs.android.material)
                implementation(androidxLibs.browser.browser)
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
