import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.mock"
    dependencies {
        common {
            test {
                // base test-utils provides testRunBlocking and the mock library; resolved from the kaluga root via the composite includeBuild.
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.MOCK))
    annotationSource("../spec")
}
