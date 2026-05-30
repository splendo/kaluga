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

@file:JvmName("AndroidSharedDependencyInjection")

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.example.feature.alerts.alertsFeatureAndroidModule
import com.splendo.kaluga.example.feature.alerts.alertsFeatureModule
import com.splendo.kaluga.example.feature.architecture.architectureFeatureAndroidModule
import com.splendo.kaluga.example.feature.architecture.architectureFeatureModule
import com.splendo.kaluga.example.feature.datetimepicker.datetimepickerFeatureAndroidModule
import com.splendo.kaluga.example.feature.datetimepicker.datetimepickerFeatureModule
import com.splendo.kaluga.example.feature.hud.hudFeatureAndroidModule
import com.splendo.kaluga.example.feature.hud.hudFeatureModule
import com.splendo.kaluga.example.feature.keyboard.keyboardFeatureModule
import com.splendo.kaluga.example.feature.media.mediaFeatureAndroidModule
import com.splendo.kaluga.example.feature.media.mediaFeatureModule
import com.splendo.kaluga.example.feature.permissions.permissionsFeatureAndroidModule
import com.splendo.kaluga.example.feature.resources.resourcesFeatureAndroidModule
import com.splendo.kaluga.example.feature.resources.resourcesFeatureModule
import com.splendo.kaluga.example.koin.initKoin as initCoreKoin
import org.koin.core.module.Module

/**
 * Android bootstrap. Loads every macOS-capable feature's Koin module via [sharedFeaturesModule]
 * plus each mobile-only feature's common Koin module (FeatureContribution + cross-platform
 * singletons) plus its Android-only `viewModel { … }` registry. Each `:feature-<mobile>` module
 * ships its own `xxxFeatureAndroidModule` so the `viewModel` factories live next to the
 * ViewModels they construct.
 */
fun initKoin(customModules: List<Module> = emptyList()) = initCoreKoin(
    customModules = listOf(
        sharedFeaturesModule,
        // Mobile-only feature contributions + common Koin singletons:
        alertsFeatureModule,
        architectureFeatureModule,
        datetimepickerFeatureModule,
        hudFeatureModule,
        keyboardFeatureModule,
        mediaFeatureModule,
        resourcesFeatureModule,
        // Android-only ViewModel registries:
        alertsFeatureAndroidModule,
        architectureFeatureAndroidModule,
        datetimepickerFeatureAndroidModule,
        hudFeatureAndroidModule,
        mediaFeatureAndroidModule,
        permissionsFeatureAndroidModule,
        resourcesFeatureAndroidModule,
    ) + customModules,
)
