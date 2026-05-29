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

package com.splendo.kaluga.example.shared.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.links.DefaultLinksManager
import com.splendo.kaluga.links.LinksManager

private const val DEMO_URL = "https://kaluga-links.web.app"

/**
 * Validates the demo URL through [LinksManager] (proving the macOS-capable kaluga.links module is
 * wired through CMP) and hands the resulting URL to [PlatformActions.openUrl]. Skips the deep-link
 * intake demo from the Kaluga-MVVM version — that one is intrinsically platform-routing and stays
 * with the host activity.
 */
@Composable
fun LinksScreen(modifier: Modifier = Modifier) {
    val linksManager: LinksManager = remember { DefaultLinksManager.Builder().create() }
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Open a Kaluga demo URL in the default browser.")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val validated = linksManager.validateLink(DEMO_URL)
                if (validated != null) {
                    PlatformActions.openUrl(validated)
                }
            },
        ) {
            Text("Open Browser")
        }
    }
}
