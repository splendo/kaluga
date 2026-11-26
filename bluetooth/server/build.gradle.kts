plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.server"
    supportMacOS = true
    dependencies {
        android {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        ios {
            main {
                implementation(libs.kotlinx.atomicfu)
            }
        }
        common {
            main {
                api(project(":bluetooth:core"))
                api(project(":permissions:bluetooth", ""))
                api(project(":base:core"))
            }
            test {
                implementation(project(":base:test", ""))
                implementation(project(":bluetooth:test-server", ""))
                implementation(project(":permissions:test", ""))
            }
        }
    }
}

// Scenarios against a simulated peer: only the test source sets that can run them include them, not commonTest. The
// protocols of the peers sit in core, as the client and the server scenarios both play the other's peer.
kotlin.sourceSets.matching { it.name in setOf("androidDeviceTest", "appleTest") }.configureEach {
    kotlin.srcDir("src/deviceScenarios/kotlin")
    kotlin.srcDir("../core/src/deviceScenarios/kotlin")
}
