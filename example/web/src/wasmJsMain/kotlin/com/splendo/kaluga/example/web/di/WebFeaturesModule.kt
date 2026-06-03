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

package com.splendo.kaluga.example.web.di

import com.splendo.kaluga.example.feature.datetime.datetimeFeatureModule
import com.splendo.kaluga.example.feature.localization.localizationFeatureModule
import com.splendo.kaluga.example.feature.scientific.scientificFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Aggregator for every feature available on the web (`wasmJs`) target — the counterpart to
 * `:shared`'s `sharedFeaturesModule`. Each feature module exposes its own Koin module containing
 * its `FeatureContribution`; this includes them all so the web bootstrap just loads one thing.
 *
 * The web target only includes the features whose underlying Kaluga modules support `wasmJs`
 * (localization, date/time and scientific units). It cannot reuse `:shared` directly: that module
 * aggregates the macOS/iOS/Android feature set, much of which (Bluetooth, location, media, …) has
 * no `wasmJs` target.
 */
val webFeaturesModule: Module = module {
    includes(
        localizationFeatureModule,
        datetimeFeatureModule,
        scientificFeatureModule,
    )
}
