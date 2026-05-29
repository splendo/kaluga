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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

private enum class RootTab(val title: String) {
    Features("Features"),
    Info("About"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen(features: List<Feature>, onFeatureSelected: (Feature) -> Unit, modifier: Modifier = Modifier) {
    var selected by rememberSaveable { mutableStateOf(RootTab.Features) }
    val tabs = remember { RootTab.entries }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Kaluga Example") })
        },
    ) { padding ->
        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selected.ordinal) {
                tabs.forEach { tab ->
                    Tab(
                        selected = selected == tab,
                        onClick = { selected = tab },
                        text = { Text(tab.title) },
                    )
                }
            }
            when (selected) {
                RootTab.Features -> FeatureListScreen(features, onFeatureSelected)
                RootTab.Info -> InfoScreen()
            }
        }
    }
}
