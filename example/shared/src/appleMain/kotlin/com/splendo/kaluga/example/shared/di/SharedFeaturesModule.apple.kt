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

import com.splendo.kaluga.example.feature.beacons.beaconsFeatureModule
import com.splendo.kaluga.example.feature.bluetooth.generation.bluetoothGenerationFeatureModule
import com.splendo.kaluga.example.feature.bluetooth.server.bluetoothServerFeatureModule
import com.splendo.kaluga.example.feature.review.reviewFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformSharedFeaturesModule: Module = module {
    includes(bluetoothServerFeatureModule, bluetoothGenerationFeatureModule, beaconsFeatureModule, reviewFeatureModule)
}
