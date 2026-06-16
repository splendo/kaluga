plugins {
    id("com.splendo.kaluga.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "resources"
    dependencies {
        common {
            main {
                implementation(project(":base:base", ""))
                implementation(project(":logging", ""))
                api(libs.kotlinx.serialization.core)
            }
            test {
                implementation(project(":base:test", ""))
                implementation(project(":resources:test", ""))
            }
        }
    }
}
