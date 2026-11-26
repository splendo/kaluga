plugins {
    id("com.splendo.kaluga.plugin")
    id(libs.plugins.kotlinx.atomicfu.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.core"
    supportJS = true
    supportWasmJS = true
    supportMacOS = true
    supportTvOS = true
    supportWatchOS = true
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
                api(project(":base:core"))
                api(project(":bytes:core"))
                api(project(":bytes:serialization"))
                implementation(project(":bytes:hash"))
                implementation(project(":base:formatting"))
                api(project(":service:service"))
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.coroutines.core)
            }
            test {
                implementation(project(":bluetooth:test-core", ""))
            }
        }
    }
}

// The protocols of the device scenarios' peers live here for the client and server scenarios; the host tests check
// them against the byte vectors the peer scripts are checked against too.
kotlin.sourceSets.matching { it.name == "androidHostTest" }.configureEach {
    kotlin.srcDir("src/deviceScenarios/kotlin")
    dependencies {
        implementation(libs.kotlinx.serialization.json)
    }
}
