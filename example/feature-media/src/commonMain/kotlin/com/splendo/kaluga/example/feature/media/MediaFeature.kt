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

package com.splendo.kaluga.example.feature.media

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import com.splendo.kaluga.media.BaseMediaManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

/** [BaseMediaManager.Builder] (i.e. `DefaultMediaManager.Builder`) is defined per platform, so
 *  the factory lives in each platform source set. */
internal expect fun newMediaManagerBuilder(): BaseMediaManager.Builder

private const val MEDIA_FEATURE_ID = "media"

class MediaContribution : FeatureContribution.Compose {
    override val id = MEDIA_FEATURE_ID
    override val label = "Media"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                MediaListScreen(
                    onMedia = { navController.navigate("$id/player") },
                    onSound = { navController.navigate("$id/sound") },
                )
            }
        }
        builder.composable("$id/player") {
            DetailScaffold(title = "Media Player", onBack = { navController.popBackStack() }) {
                MediaScreen()
            }
        }
        builder.composable("$id/sound") {
            DetailScaffold(title = "Sound", onBack = { navController.popBackStack() }) {
                MediaSoundScreen()
            }
        }
    }
}

val mediaFeatureModule: Module = module {
    single<BaseMediaManager.Builder> { newMediaManagerBuilder() }
    viewModel { MediaViewModel(get()) }
    single { MediaContribution() } bind FeatureContribution::class
}
