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

package com.splendo.kaluga.example.arch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.splendo.kaluga.example.arch.info.InfoScreen
import org.koin.mp.KoinPlatformTools

object Routes {
    const val ROOT = "root"
}

/**
 * Root composable hosted by every platform (Android `ComponentActivity`, iOS `MainViewController`,
 * macOS `Window { … }`). Pulls every [FeatureContribution] from Koin, builds a nav graph from the
 * contributions that ship compose destinations, and routes non-compose contributions through
 * [onNativeLaunch] so host platforms can launch their native UIs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen(onNativeLaunch: (id: String) -> Unit = {}, modifier: Modifier = Modifier) {
    val contributions = remember {
        KoinPlatformTools.defaultContext().get().getAll<FeatureContribution>().sortedBy { it.label }
    }
    val navController = rememberNavController()
    val deepLink by DeepLinkBus.state.collectAsState()
    LaunchedEffect(deepLink) {
        deepLink?.let { link ->
            navController.navigate(link.targetId) {
                launchSingleTop = true
                popUpTo(Routes.ROOT)
            }
            val entry = navController.getBackStackEntry(link.targetId)
            link.payload.forEach { (k, v) -> entry.savedStateHandle[k] = v }
            DeepLinkBus.consume()
        }
    }
    NavHost(
        navController = navController,
        startDestination = Routes.ROOT,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(Routes.ROOT) {
            RootScaffold(
                contributions = contributions,
                onSelected = { contribution ->
                    if (contribution.isCompose) {
                        navController.navigate(contribution.id)
                    } else {
                        onNativeLaunch(contribution.id)
                    }
                },
            )
        }
        contributions.filter { it.isCompose }.forEach { contribution ->
            contribution.register(this, navController)
        }
    }
}

private enum class RootTab(val label: String) { Features("Features"), Info("Info") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RootScaffold(contributions: List<FeatureContribution>, onSelected: (FeatureContribution) -> Unit) {
    var selectedTab by remember { mutableStateOf(RootTab.Features) }
    Scaffold(topBar = { TopAppBar(title = { Text("Kaluga Example") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
                RootTab.entries.forEach { tab ->
                    Tab(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        text = { Text(tab.label) },
                    )
                }
            }
            when (selectedTab) {
                RootTab.Features -> FeatureList(contributions = contributions, onSelected = onSelected)
                RootTab.Info -> InfoScreen()
            }
        }
    }
}

@Composable
private fun FeatureList(contributions: List<FeatureContribution>, onSelected: (FeatureContribution) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(contributions, key = { it.id }) { contribution ->
            Button(
                onClick = { onSelected(contribution) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(contribution.label)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScaffold(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("<") }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            content()
        }
    }
}
