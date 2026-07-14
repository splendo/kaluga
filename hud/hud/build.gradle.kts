plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "hud"
    dependencies {
        android {
            main {
                implementation(androidxLib.fragment.fragment)
            }
            test {
                implementation(androidxLib.fragment.fragmentKtx)
            }
            device {
                implementation(project(":architecture:architecture", ""))
            }
        }
        common {
            main {
                api(project(":lifecycle:lifecycle", ""))
                implementation(project(":base:core", ""))
            }
            test {
                implementation(project(":hud:test", ""))
            }
        }
    }
}
