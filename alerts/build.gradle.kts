plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "alerts"
    dependencies {
        android {
            main {
                implementation(libs.androidx.fragment)
            }
            test {
                implementation(libs.androidx.fragment.ktx)
            }
            device {
                implementation(libs.androidx.activity.ktx)
                implementation(project(":architecture:architecture", ""))
            }
        }
        common {
            main {
                api(project(":lifecycle:lifecycle", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                implementation(project(":resources:resources", ""))
            }
            test {
                implementation(project(":test-utils-alerts", ""))
            }
        }
    }
}
