import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bytes.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.views"
    dependencies {
        common {
            test {
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-client:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-server:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH, ImplementFor.SIMULATOR, ImplementFor.MOCK))
}
