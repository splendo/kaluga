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
        }
        common {
            main {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
            test {
                api(project(":test-utils-hud", ""))
            }
        }
    }
}
