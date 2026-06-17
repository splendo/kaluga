import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.server"
}

bluetooth {
    target.set(setOf(BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    annotationSource("../spec")
}
