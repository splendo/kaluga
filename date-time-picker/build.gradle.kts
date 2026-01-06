plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "datetimepicker"
    dependencies {
        android {
            device {
                implementation(libs.androidx.activity.ktx)
            }
        }
        common {
            main {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-date-time-picker", ""))
            }
        }
    }
}
