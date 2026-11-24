import com.splendo.kaluga.bluetooth.plugin.BluetoothPluginVersion
import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
}

kaluga {
    moduleName = "bluetooth.validation.externalSourceConsumer"
    dependencies {
        common {
            main {
                // Provides the generated Remote*/Local* symbols for the external ExternalService.
                implementation(project(":validation:external-source-provider"))
            }
            test {
                // Provides the iOS test entry point (mainBackground) + kotlin.test; resolved from the kaluga root.
                implementation("com.splendo.kaluga.base:test:${BluetoothPluginVersion.kalugaVersion}")
                implementation("com.splendo.kaluga.bluetooth:test-client:${BluetoothPluginVersion.kalugaVersion}")
            }
        }
    }
}

// Generates its own ComposedDevice/OwnService symbols, but references ExternalService (owned by the provider module)
// through externalAnnotationSource: the type resolves for cross-reference, yet no duplicate Remote*/Local* symbols are
// generated for it.
bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT))
    implementFor.set(setOf(ImplementFor.BLUETOOTH))
    annotationSource("../consumer-spec")
    externalAnnotationSource("../external-spec")
}
