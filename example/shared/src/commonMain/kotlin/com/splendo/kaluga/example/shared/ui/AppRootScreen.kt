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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read

private enum class RootTab(val title: String) {
    Features("Features"),
    Info("About"),
}

/**
 * Maps each [Feature] to a CMP nav-graph route, or `null` if the feature is not yet migrated and
 * has to be dispatched to a platform-specific screen by the host (via the
 * `onUnmigratedFeatureSelected` callback passed to [AppRootScreen]).
 */
private fun cmpRouteFor(feature: Feature): String? = when (feature) {
    Feature.Links -> Routes.LINKS
    Feature.System -> Routes.SYSTEM
    Feature.DateTime -> Routes.TIMER
    Feature.Permissions -> Routes.PERMISSIONS
    Feature.Location -> Routes.LOCATION
    Feature.Bluetooth -> Routes.BLUETOOTH
    Feature.Scientific -> Routes.SCIENTIFIC
    else -> null
}

internal object Routes {
    const val ROOT = "root"
    const val LINKS = "links"
    const val SYSTEM = "system"
    const val NETWORK = "system/network"
    const val TIMER = "timer"
    const val PERMISSIONS = "permissions"
    const val PERMISSION = "permission/{name}"
    fun permission(name: String) = "permission/$name"
    const val LOCATION = "location"
    const val BLUETOOTH = "bluetooth"
    const val BLUETOOTH_CLIENT = "bluetooth/client"
    const val BLUETOOTH_SERVER = "bluetooth/server"
    const val SCIENTIFIC = "scientific"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRootScreen(features: List<Feature>, onUnmigratedFeatureSelected: (Feature) -> Unit, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.ROOT,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(Routes.ROOT) {
            RootScaffold(
                features = features,
                onFeatureSelected = { feature ->
                    val route = cmpRouteFor(feature)
                    if (route != null) navController.navigate(route)
                    else onUnmigratedFeatureSelected(feature)
                },
            )
        }
        composable(Routes.LINKS) {
            DetailScaffold(title = Feature.Links.title, onBack = { navController.popBackStack() }) {
                LinksScreen()
            }
        }
        composable(Routes.SYSTEM) {
            DetailScaffold(title = Feature.System.title, onBack = { navController.popBackStack() }) {
                SystemScreen(onNetworkSelected = { navController.navigate(Routes.NETWORK) })
            }
        }
        composable(Routes.NETWORK) {
            DetailScaffold(title = "Network", onBack = { navController.popBackStack() }) {
                NetworkScreen()
            }
        }
        composable(Routes.TIMER) {
            DetailScaffold(title = Feature.DateTime.title, onBack = { navController.popBackStack() }) {
                TimerScreen()
            }
        }
        composable(Routes.PERMISSIONS) {
            DetailScaffold(title = Feature.Permissions.title, onBack = { navController.popBackStack() }) {
                PermissionsListScreen(onPermissionSelected = { permissionView ->
                    navController.navigate(Routes.permission(permissionView.name))
                })
            }
        }
        composable(Routes.LOCATION) {
            DetailScaffold(title = Feature.Location.title, onBack = { navController.popBackStack() }) {
                LocationScreen()
            }
        }
        composable(Routes.BLUETOOTH) {
            DetailScaffold(title = Feature.Bluetooth.title, onBack = { navController.popBackStack() }) {
                BluetoothListScreen(
                    onClient = { navController.navigate(Routes.BLUETOOTH_CLIENT) },
                    onServer = { navController.navigate(Routes.BLUETOOTH_SERVER) },
                )
            }
        }
        composable(Routes.BLUETOOTH_CLIENT) {
            DetailScaffold(title = "Bluetooth Client", onBack = { navController.popBackStack() }) {
                BluetoothDeviceListScreen()
            }
        }
        composable(Routes.BLUETOOTH_SERVER) {
            DetailScaffold(title = "Bluetooth Server", onBack = { navController.popBackStack() }) {
                BluetoothServerScreen()
            }
        }
        composable(Routes.SCIENTIFIC) {
            DetailScaffold(title = Feature.Scientific.title, onBack = { navController.popBackStack() }) {
                ScientificScreen()
            }
        }
        composable(
            route = Routes.PERMISSION,
            arguments = listOf(navArgument("name") { type = NavType.StringType }),
        ) { entry ->
            val name = entry.arguments?.read { getString("name") } ?: return@composable
            val permissionView = com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView.valueOf(name)
            DetailScaffold(title = permissionView.title, onBack = { navController.popBackStack() }) {
                PermissionScreen(permissionView)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RootScaffold(features: List<Feature>, onFeatureSelected: (Feature) -> Unit) {
    var selected by rememberSaveable { mutableStateOf(RootTab.Features) }
    val tabs = remember { RootTab.entries }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Kaluga Example") }) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailScaffold(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("<")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            content()
        }
    }
}
