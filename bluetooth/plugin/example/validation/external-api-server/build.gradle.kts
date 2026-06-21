import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.externalApiServer"
    dependencies {
        common {
            main {
                implementation(project(":validation:contract"))
            }
            test {
                // Provides the iOS test entry point (mainBackground) + kotlin.test; resolved from the kaluga root.
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    useExternalApi()
    generatedPackage = "com.splendo.kaluga.bluetooth.sharedserver"
    apiPackage = "com.splendo.kaluga.bluetooth.sharedcontract"
    annotationSource("../spec")
}
