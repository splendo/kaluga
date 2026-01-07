plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "keyboard"
    dependencies {
        common {
            main {
                implementation(project(":architecture", ""))
                implementation(project(":base", ""))
            }
            test {
                implementation(project(":test-utils-keyboard", ""))
            }
        }
    }
}
