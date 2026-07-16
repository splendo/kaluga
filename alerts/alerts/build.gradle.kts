plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "alerts"
    dependencies {
        android {
            main {
                implementation(androidxLibs.fragment.fragment)
            }
            test {
                implementation(androidxLibs.fragment.fragmentKtx)
            }
            device {
                implementation(androidxLibs.activity.activityKtx)
                implementation(project(":architecture:architecture", ""))
            }
        }
        common {
            main {
                api(project(":lifecycle:lifecycle", ""))
                implementation(project(":base:core", ""))
                implementation(project(":logging", ""))
                implementation(project(":resources:resources", ""))
            }
            test {
                implementation(project(":alerts:test", ""))
            }
        }
    }
}
