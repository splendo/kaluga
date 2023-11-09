/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.beacons.DefaultBeacons
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
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.minutes

typealias LocationStateRepoBuilderBuilder = (suspend (CoroutineContext) -> Permissions) -> LocationStateRepoBuilder
typealias BluetoothBuilderBuilder = (suspend (CoroutineContext) -> Permissions) -> BluetoothBuilder

private fun sharedModule(locationStateRepoBuilderBuilder: LocationStateRepoBuilderBuilder, bluetoothBuilderBuilder: BluetoothBuilderBuilder) = module {
    single<Logger> { RestrictedLogger(RestrictedLogLevel.None) }
    single { PermissionsBuilder() }
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
        }.create(
            scannerSettingsBuilder = { BaseScanner.Settings(it, logger = get()) },
        )
    }
    single { DefaultBeacons(get<Bluetooth>(), beaconLifetime = 1.minutes, logger = get()) }
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
