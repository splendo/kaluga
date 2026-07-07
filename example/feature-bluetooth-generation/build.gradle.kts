import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
    id(libs.plugins.compose.get().pluginId)
}

kaluga {
    moduleName = "example.feature.bluetooth.generation"
    supportMacOS = true
    dependencies {
        common {
            main {
                api(project(":core-arch"))
                api(project(":core-koin"))
            }
        }
    }
}

// The device covering the full annotation feature set; the plugin generates the client + server
// (Bluetooth and simulated) implementations consumed by the screens in this module.
bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH, ImplementFor.SIMULATOR))
}

// Generated Bluetooth code is not subject to ktlint (it is also a KSP output, so depend on it explicitly).
tasks.withType<org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask>().configureEach {
    dependsOn(tasks.matching { it.name.startsWith("ksp") })
    exclude { it.file.path.contains("/generated/") }
}
