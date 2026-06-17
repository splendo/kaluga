import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.contract"
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    apiOnly()
    generatedPackage = "com.splendo.kaluga.bluetooth.sharedcontract"
    annotationSource("../spec")
}
