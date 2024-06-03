plugins {
    id("kaluga-library-components")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "scientific"
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
