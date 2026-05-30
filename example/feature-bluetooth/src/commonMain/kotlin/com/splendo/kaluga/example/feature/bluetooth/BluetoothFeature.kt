/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.example.feature.bluetooth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import com.splendo.kaluga.example.feature.permissions.USE_LOCATION_FOR_BLUETOOTH
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

class BluetoothContribution : FeatureContribution {
    override val id = "bluetooth"
    override val label = "Bluetooth"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                BluetoothListScreen(
                    onClient = { navController.navigate("bluetooth/client") },
                    onServer = { navController.navigate("bluetooth/server") },
                )
            }
        }
        builder.composable("bluetooth/client") {
            DetailScaffold(title = "Bluetooth Client", onBack = { navController.popBackStack() }) {
                BluetoothDeviceListScreen()
            }
        }
        builder.composable("bluetooth/server") {
            DetailScaffold(title = "Bluetooth Server", onBack = { navController.popBackStack() }) {
                BluetoothServerScreen()
            }
        }
    }
}

/** [BluetoothBuilder] is an `expect class` — its constructor differs per platform, so the
 *  factory must live in each platform's source set. The Android variant pulls in
 *  `applicationContext` defaults while the Apple variants do not. */
internal expect fun newBluetoothBuilder(
    permissionsBuilder: suspend (CoroutineContext) -> Permissions,
): BluetoothBuilder

/**
 * Owns the singletons for the Bluetooth feature: the [BluetoothBuilder] (which wires the
 * [PermissionsBuilder] from `:feature-permissions` after registering bluetooth + location
 * permission factories on demand) and a ready-to-use [Bluetooth] client. The beacons feature
 * reuses the same `Bluetooth` instance by depending on `:feature-bluetooth` and pulling it from
 * Koin — that is the rationale for owning the singleton here rather than further down.
 */
val bluetoothFeatureModule: Module = module {
    single {
        newBluetoothBuilder { context ->
            val builder = get<PermissionsBuilder>()
            val settings = BasePermissionManager.Settings(logger = get())
            builder.registerBluetoothPermissionIfNotRegistered(settings = settings)
            builder.registerLocationPermissionIfNotRegistered(settings = settings)
            Permissions(builder, context)
        }
    }
    single<Bluetooth> {
        get<BluetoothBuilder>().createClient(
            { BaseScanner.Settings(permissions = it, useLocation = USE_LOCATION_FOR_BLUETOOTH, logger = get()) },
        )
    }
    single { BluetoothContribution() } bind FeatureContribution::class
}
