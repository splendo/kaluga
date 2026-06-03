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

package com.splendo.kaluga.example.feature.links

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.example.arch.DeepLink
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

class LinksContribution : FeatureContribution {
    override val id = "links"
    override val label = "Links"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) { entry ->
            val incomingUrl = entry.savedStateHandle.get<String>("url")
            entry.savedStateHandle["url"] = null
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                LinksScreen(incomingUrl = incomingUrl)
            }
        }
    }

    override fun parseDeepLink(url: String): DeepLink? = if (url.startsWith("https://kaluga-links.web.app")) {
        DeepLink(targetId = id, payload = mapOf("url" to url))
    } else {
        null
    }
}

val linksFeatureModule: Module = module {
    single { LinksContribution() } bind FeatureContribution::class
}
