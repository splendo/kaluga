plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "hud"
    dependencies {
        android {
            main {
                implementation(libs.androidx.fragment)
            }
            test {
                implementation(libs.androidx.fragment.ktx)
            }
            device {
                implementation(project(":architecture:architecture", ""))
            }
        }
        common {
            main {
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-hud", ""))
            }
        }
    }
}
