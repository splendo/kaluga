plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetimepicker"
    dependencies {
        android {
            device {
                implementation(libs.androidx.activity.ktx)
                implementation(project(":architecture:architecture", ""))
            }
        }
        common {
            main {
                api(project(":lifecycle:lifecycle", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-date-time-picker", ""))
            }
        }
    }
}
