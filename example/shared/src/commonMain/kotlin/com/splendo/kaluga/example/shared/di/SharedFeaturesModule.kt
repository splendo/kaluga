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

import com.splendo.kaluga.example.feature.bluetooth.client.bluetoothClientFeatureModule
import com.splendo.kaluga.example.feature.datetime.datetimeFeatureModule
import com.splendo.kaluga.example.feature.links.linksFeatureModule
import com.splendo.kaluga.example.feature.localization.localizationFeatureModule
import com.splendo.kaluga.example.feature.location.locationFeatureModule
import com.splendo.kaluga.example.feature.media.mediaFeatureModule
import com.splendo.kaluga.example.feature.permissions.permissionsFeatureModule
import com.splendo.kaluga.example.feature.scientific.scientificFeatureModule
import com.splendo.kaluga.example.feature.system.systemFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * The feature modules with no `wasmJs` target (bluetooth-server, beacons, review). Provided per target:
 * non-empty on macOS/iOS/Android, empty on the web so [sharedFeaturesModule] stays wasmJs-compatible.
 */
expect val platformSharedFeaturesModule: Module

/**
 * Aggregator for every feature module the platform supports. Each feature module exposes its own Koin
 * module containing its `FeatureContribution`; this includes them all so the host bootstrap just loads
 * one thing. The web (`wasmJs`) host reuses this directly; macOS/iOS/Android additionally pick up the
 * non-web features through [platformSharedFeaturesModule], and the Android + iOS hosts add the
 * mobile-only features on top.
 */
val sharedFeaturesModule: Module = module {
    includes(
        linksFeatureModule,
        datetimeFeatureModule,
        systemFeatureModule,
        permissionsFeatureModule,
        locationFeatureModule,
        bluetoothClientFeatureModule,
        localizationFeatureModule,
        mediaFeatureModule,
        scientificFeatureModule,
        platformSharedFeaturesModule,
    )
}
