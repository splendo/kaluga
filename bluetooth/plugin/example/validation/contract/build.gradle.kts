import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.contract"
    dependencies {
        common {
            test {
                // Provides the iOS test entry point (mainBackground) + kotlin.test; resolved from the kaluga root.
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    apiOnly()
    generatedPackage = "com.splendo.kaluga.bluetooth.sharedcontract"
    annotationSource("../spec")
}
