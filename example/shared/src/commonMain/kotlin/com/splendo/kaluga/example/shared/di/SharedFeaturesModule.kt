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

import com.splendo.kaluga.example.feature.bluetooth.bluetoothFeatureModule
import com.splendo.kaluga.example.feature.datetime.datetimeFeatureModule
import com.splendo.kaluga.example.feature.info.infoFeatureModule
import com.splendo.kaluga.example.feature.links.linksFeatureModule
import com.splendo.kaluga.example.feature.location.locationFeatureModule
import com.splendo.kaluga.example.feature.permissions.permissionsFeatureModule
import com.splendo.kaluga.example.feature.scientific.scientificFeatureModule
import com.splendo.kaluga.example.feature.system.systemFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Aggregator for every macOS-capable feature module. Each feature module exposes its own Koin
 * module containing its `FeatureContribution`; this includes them all so the host bootstrap just
 * loads one thing. The macOS Demo host loads only this; the Android + iOS hosts additionally load
 * `mobileSharedContributionsModule` (native-launch contributions for `:mobileshared` features).
 */
val sharedFeaturesModule: Module = module {
    includes(
        infoFeatureModule,
        linksFeatureModule,
        datetimeFeatureModule,
        systemFeatureModule,
        permissionsFeatureModule,
        locationFeatureModule,
        bluetoothFeatureModule,
        scientificFeatureModule,
    )
}
