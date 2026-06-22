import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.client"
    dependencies {
        common {
            test {
                // Provides the iOS test entry point (mainBackground) + kotlin.test; resolved from the kaluga root.
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-client:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    annotationSource("../spec")
}
