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

package com.splendo.kaluga.example.feature.beacons

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.beacons.DefaultBeacons
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

class BeaconsContribution : FeatureContribution {
    override val id = "beacons"
    override val label = "Beacons"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                BeaconsScreen()
            }
        }
    }
}

/** Owns the [Beacons] singleton, which sits on top of the [com.splendo.kaluga.bluetooth.Bluetooth]
 *  client registered by `:feature-bluetooth` — beacons reuses the same scanner pipeline rather
 *  than spinning up a parallel one. */
val beaconsFeatureModule: Module = module {
    single<Beacons> { DefaultBeacons(bluetooth = get<Bluetooth>(), logger = get()) }
    single { BeaconsContribution() } bind FeatureContribution::class
}
