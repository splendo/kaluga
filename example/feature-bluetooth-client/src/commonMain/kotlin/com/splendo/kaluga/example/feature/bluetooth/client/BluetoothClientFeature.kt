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

package com.splendo.kaluga.example.feature.bluetooth.client

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothClientBuilder
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

class BluetoothClientContribution : FeatureContribution {
    override val id = "bluetooth-client"
    override val label = "Bluetooth Client"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                BluetoothDeviceListScreen()
            }
        }
    }
}

/** [BluetoothClientBuilder] is an `expect class` — its constructor differs per platform. */
internal expect fun newBluetoothClientBuilder(permissionsBuilder: suspend (CoroutineContext) -> Permissions): BluetoothClientBuilder

/**
 * Owns the singletons for the Bluetooth client feature: the [BluetoothClientBuilder] (which wires
 * the [PermissionsBuilder] from `:feature-permissions` after registering bluetooth + location
 * permission factories on demand) and a ready-to-use [Bluetooth] client. The beacons feature
 * reuses the same `Bluetooth` instance by depending on this module.
 */
val bluetoothClientFeatureModule: Module = module {
    single {
        newBluetoothClientBuilder { context ->
            val builder = get<PermissionsBuilder>()
            val settings = BasePermissionManager.Settings(logger = get())
            builder.registerBluetoothPermissionIfNotRegistered(settings = settings)
            builder.registerLocationPermissionIfNotRegistered(settings = settings)
            Permissions(builder, context)
        }
    }
    single<Bluetooth> {
        get<BluetoothClientBuilder>().createClient(
            { BaseScanner.Settings(permissions = it, useLocation = USE_LOCATION_FOR_BLUETOOTH, logger = get()) },
        )
    }
    single { BluetoothClientContribution() } bind FeatureContribution::class
}
