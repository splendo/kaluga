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

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.example.feature.alerts.alertsFeatureModule
import com.splendo.kaluga.example.feature.architecture.architectureFeatureModule
import com.splendo.kaluga.example.feature.datetimepicker.datetimepickerFeatureModule
import com.splendo.kaluga.example.feature.hud.hudFeatureModule
import com.splendo.kaluga.example.feature.keyboard.keyboardFeatureModule
import com.splendo.kaluga.example.feature.resources.resourcesFeatureModule
import com.splendo.kaluga.example.koin.initKoin as initCoreKoin
import org.koin.core.module.Module

/**
 * iOS bootstrap. Loads [sharedFeaturesModule] (macOS-capable feature contributions) plus the
 * common Koin module of every mobile-only feature (which exposes the FeatureContribution and any
 * iOS-relevant singletons). iOS ViewModels are constructed by Swift directly, so no per-feature
 * `viewModel { … }` registries are loaded here.
 */
fun initKoin(customModules: List<Module> = emptyList()) = initCoreKoin(
    customModules = listOf(
        sharedFeaturesModule,
        alertsFeatureModule,
        architectureFeatureModule,
        datetimepickerFeatureModule,
        hudFeatureModule,
        keyboardFeatureModule,
        resourcesFeatureModule,
    ) + customModules,
)
