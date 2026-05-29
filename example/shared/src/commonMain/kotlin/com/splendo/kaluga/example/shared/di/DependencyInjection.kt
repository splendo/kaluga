/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import com.splendo.kaluga.permissions.registerAllPermissions
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

typealias LocationStateRepoBuilderBuilder = (suspend (CoroutineContext) -> Permissions) -> LocationStateRepoBuilder
typealias BluetoothBuilderBuilder = (suspend (CoroutineContext) -> Permissions) -> BluetoothBuilder

/*
    Switch this value to use the location permission on Android when using bluetooth.
 */
const val USE_LOCATION_FOR_BLUETOOTH = false

private fun sharedModule(locationStateRepoBuilderBuilder: LocationStateRepoBuilderBuilder, bluetoothBuilderBuilder: BluetoothBuilderBuilder) = module {
    single<Logger> { RestrictedLogger(RestrictedLogLevel.None) }
    // `registerAllPermissions` is non-suspend and idempotent enough that re-running for already
    // registered permissions is a no-op. Doing it here means every consumer of `PermissionsBuilder`
    // — including the CMP `PermissionScreen` that pulls the builder via `koinInject` — sees the
    // factories ready without having to register them inside its own coroutine first.
    single {
        PermissionsBuilder().apply {
            registerAllPermissions(settings = BasePermissionManager.Settings(logger = get()))
        }
    }
    single {
        locationStateRepoBuilderBuilder {
            val builder = get<PermissionsBuilder>()
            builder.registerLocationPermissionIfNotRegistered(
                settings = BasePermissionManager.Settings(logger = get()),
            )
            Permissions(builder, it)
        }
    }
    single {
        bluetoothBuilderBuilder {
            val builder = get<PermissionsBuilder>()
            val settings = BasePermissionManager.Settings(logger = get())
            builder.registerBluetoothPermissionIfNotRegistered(settings = settings)
            builder.registerLocationPermissionIfNotRegistered(settings = settings)
            Permissions(builder, it)
        }
    }
    single {
        get<BluetoothBuilder>().createClient(
            { BaseScanner.Settings(permissions = it, useLocation = USE_LOCATION_FOR_BLUETOOTH, logger = get()) },
        )
    }
}

internal fun initKoin(
    platformModule: Module,
    locationStateRepoBuilderBuilder: LocationStateRepoBuilderBuilder,
    bluetoothBuilderBuilder: BluetoothBuilderBuilder,
    customModules: List<Module> = emptyList(),
) = startKoin {
    appDeclaration()
    modules(platformModule, sharedModule(locationStateRepoBuilderBuilder, bluetoothBuilderBuilder), *customModules.toTypedArray())
}

internal expect val appDeclaration: KoinAppDeclaration
