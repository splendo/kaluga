plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "resources"
    dependencies {
        common {
            main {
                implementation(project(":base", ""))
                implementation(project(":logging", ""))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":test-utils-base", ""))
                implementation(project(":test-utils-resources", ""))
            }
        }
    }
}
