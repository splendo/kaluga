plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test-utils-%PACKAGE%"
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
