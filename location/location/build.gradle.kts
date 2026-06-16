plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "location"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.android.play.services.location)
                implementation(libs.kotlinx.coroutines.playservices)
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(project(":service:service"))
                api(project(":date-time:date-time"))
                api(project(":permissions:location", ""))
            }
            test {
                implementation(project(":location:test", ""))
            }
        }
    }
}
