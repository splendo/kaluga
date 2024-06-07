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
            instrumented {
                implementation(libs.androidx.activity.ktx)
            }
        }
        common {
            main {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                implementation(project(":resources", ""))
            }
            test {
                implementation(project(":test-utils-alerts", ""))
            }
        }
    }
}
