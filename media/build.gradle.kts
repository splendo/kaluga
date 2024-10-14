plugins {
    id("com.splendo.kaluga.plugin")
}

kaluga {
    moduleName = "media"
    dependencies {
        common {
            main {
                implementation(project(":base"))
                implementation(project(":architecture"))
                implementation(project(":logging"))
            }
            test {
                implementation(project(":test-utils-media"))
            }
        }
    }
}
