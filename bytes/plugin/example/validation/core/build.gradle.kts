import com.splendo.kaluga.bytes.plugin.BytesPluginVersion

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bytes.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bytes.validation.core"

    // Generated views run on every platform the library supports, so the suites run on the web targets too.
    supportJS = true
    supportWasmJS = true

    dependencies {
        common {
            test {
                implementation("com.splendo.kaluga.base:test:${BytesPluginVersion.kalugaVersion}")
            }
        }
    }
}
