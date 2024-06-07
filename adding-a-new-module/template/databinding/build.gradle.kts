plugins {
    id("com.splendo.kaluga.plugin.android.databinding")
}

kaluga {
    moduleName = "%BASEMODULE%"
    dependencies {
        common {
            main {
                implementation(project(":base"))
                implementation(project("%BASEMODULE%"))
            }
            test {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
