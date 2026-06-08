plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.location"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    dependencies {
        android {
            main {
                implementation(libs.android.play.services.location)
            }
        }
        common {
            main {
                api(project(":permissions:core", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
