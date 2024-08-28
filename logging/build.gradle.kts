plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
}

kaluga {
    moduleName = "logging"

    supportJVM = true
    supportJS = true

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
        js {
            main {
                api(libs.kotlinx.atomicfu)
            }
        }
    }
}
