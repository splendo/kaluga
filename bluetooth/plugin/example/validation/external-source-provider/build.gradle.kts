import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.externalSourceProvider"
}

// Owns the external definitions: generates the Remote*/Local* symbols for ExternalService that the consumer module
// reuses (rather than regenerating) via externalAnnotationSource().
bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    annotationSource("../external-spec")
}
