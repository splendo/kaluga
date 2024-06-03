plugins {
    id("kaluga-library-components")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "logging"
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                implementation(libs.napier)
            }
            test {
                api(project(":test-utils-base", ""))
            }
        }

    }
}
