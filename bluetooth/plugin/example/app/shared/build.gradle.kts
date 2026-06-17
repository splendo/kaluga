import com.splendo.kaluga.bluetooth.plugin.BluetoothTarget
import com.splendo.kaluga.bluetooth.plugin.ImplementFor

plugins {
    id("com.splendo.kaluga.plugin")
    id("com.splendo.kaluga.bluetooth.plugin")
    id(libs.plugins.compose.get().pluginId)
    alias(libs.plugins.jetbrains.compose)
}

kaluga {
    moduleName = "bluetooth.demo"
    supportMacOS = true
    appleFramework {
        baseName = "Shared"
        isStatic = true
    }
    dependencies {
        common {
            main {
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.lifecycle.viewmodel)
                implementation(libs.koin.core)
                implementation(libs.koin.core.viewmodel)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
            }
        }
    }
}

bluetooth {
    target.set(setOf(BluetoothTarget.CLIENT, BluetoothTarget.SERVER))
    implementFor.set(setOf(ImplementFor.BLUETOOTH, ImplementFor.SIMULATOR))
}
