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

@file:JvmName("AndroidDependencyInjection")

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.location.DefaultLocationManager
import com.splendo.kaluga.location.GoogleLocationProvider
import com.splendo.kaluga.location.LocationStateRepoBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal val androidModule = module {}

fun initKoin(customModules: List<Module> = emptyList()) = initKoin(
    androidModule,
    {
        LocationStateRepoBuilder(
            locationManagerBuilder = DefaultLocationManager.Builder(
                googleLocationProviderSettings = GoogleLocationProvider.Settings(),
            ),
            permissionsBuilder = it,
        )
    },
    { BluetoothBuilder(permissionsBuilder = it) },
    customModules,
)

internal actual val appDeclaration: KoinAppDeclaration = {
    androidContext(ApplicationHolder.applicationContext)
}
