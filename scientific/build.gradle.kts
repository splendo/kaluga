plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "scientific"

    supportJVM = true
    supportJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true

    dependencies {
        common {
            main {
                implementation(project(":base"))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":test-utils-base"))
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}
