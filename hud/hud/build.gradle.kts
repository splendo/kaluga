plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "hud"
    dependencies {
        android {
            main {
                implementation(androidxLibs.fragment.fragment)
            }
            test {
                implementation(androidxLibs.fragment.fragmentKtx)
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
