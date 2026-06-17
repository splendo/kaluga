import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.externalApiSimulator"
    dependencies {
        common {
            main {
                implementation(project(":validation:contract"))
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.SIMULATOR))
    useExternalApi()
    generatedPackage = "com.splendo.kaluga.bluetooth.sharedsimulator"
    apiPackage = "com.splendo.kaluga.bluetooth.sharedcontract"
    annotationSource("../spec")
}
