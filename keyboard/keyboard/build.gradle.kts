plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "keyboard"
    dependencies {
        android {
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
                implementation(project(":keyboard:test", ""))
            }
        }
    }
}
