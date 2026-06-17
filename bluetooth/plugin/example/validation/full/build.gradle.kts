import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.full"
}

bluetooth {
    target.add(BluetoothTarget.SERVER)
    implementFor.add(ImplementFor.SIMULATOR)
}
