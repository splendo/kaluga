plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "permissions.location"
    dependencies {
        android {
            main {
                implementation(libs.android.play.services.location)
            }
        }
        common {
            main {
                api(project(":base-permissions", ""))
            }
            test {
                implementation(project(":test-utils-base", ""))
            }
        }
    }
}
