plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bytes.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bytes.validation.parts"

    // Its targets are those of the modules holding its layouts.
    supportJS = true
    supportWasmJS = true
}
