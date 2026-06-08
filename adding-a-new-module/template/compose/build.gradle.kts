plugins {
    id("com.splendo.kaluga.plugin.android.compose")
}

kaluga {
    moduleName = "%BASEMODULE%"
    dependencies {
        common {
            main {
                implementation(project(":base:base"))
                implementation(project("%BASEMODULE%"))
            }
            test {
                implementation(project(":base:test"))
            }
        }
    }
}
