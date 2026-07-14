plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "alerts"
    dependencies {
        android {
            main {
                implementation(androidxLib.fragment.fragment)
            }
            test {
                implementation(androidxLib.fragment.fragmentKtx)
            }
            device {
                implementation(androidxLib.activity.activityKtx)
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
