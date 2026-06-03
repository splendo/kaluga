plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "lifecycle"
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
    supportJVM = true
    supportJS = true
    dependencies {
        android {
            main {
                api(libs.androidx.lifecycle.runtime)
                api(libs.androidx.fragment)
            }
        }
        common {
            test {
                implementation(project(":test-utils-lifecycle", ""))
            }
        }
    }
}
