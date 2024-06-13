plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "test-utils-%PACKAGE%"
%TARGET_CONFIG%
    dependencies {
        common {
            main {
                implementation(project(":base"))
            }
            test {
                implementation(project(":test-utils-base"))
            }
        }
    }
}
