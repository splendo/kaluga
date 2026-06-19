plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
    alias(libs.plugins.kotlin.serialization)
}

kaluga {
    moduleName = "bluetooth.validation.xml"
}

// Validates the GATT XML front-end end to end: definitions (including ScientificValue value classes for unit fields)
// are generated, processed by KSP and compiled.
bluetooth {
    apiOnly()
    useScientificUnits = true
    generateFromXml("Thermometer", "src/gatt")
}
