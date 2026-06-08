plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "%PACKAGE%"
%TARGET_CONFIG%
    dependencies {
        common {
            main {
                implementation(project(":base:base"))
            }
            test {
                implementation(project(":base:test"))
            }
        }
    }
}
