plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "location"
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
                implementation(project(":service"))
                api(project(":location-permissions", ""))
            }
            test {
                implementation(project(":test-utils-location", ""))
            }
        }
    }
}
