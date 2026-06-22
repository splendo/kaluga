plugins {
    id("com.splendo.kaluga.plugin.android.databinding")
}

kaluga {
    moduleName = "%PACKAGE%"
    dependencies {
        common {
            main {
                implementation(project(":base:core"))
                api(project(":%BASEMODULE%:%BASEMODULE%"))
            }
            test {
                implementation(project(":base:test"))
            }
        }
    }
}
