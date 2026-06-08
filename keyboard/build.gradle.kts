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
                api(project(":lifecycle", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-keyboard", ""))
            }
        }
    }
}
