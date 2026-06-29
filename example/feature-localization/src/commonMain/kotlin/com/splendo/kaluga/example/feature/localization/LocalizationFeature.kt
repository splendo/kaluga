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

package com.splendo.kaluga.example.feature.localization

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

class LocalizationContribution : FeatureContribution.Compose {
    override val id = "localization"
    override val label = "Localization"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                LocalizationScreen()
            }
        }
    }
}

val localizationFeatureModule: Module = module {
    viewModel { LocalizationViewModel() }
    single { LocalizationContribution() } bind FeatureContribution::class
}
