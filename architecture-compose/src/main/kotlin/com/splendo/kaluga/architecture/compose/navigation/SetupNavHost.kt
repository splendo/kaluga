/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.compose.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.StateFlow

/**
 * Creates a a [NavHost] for a given [NavHostController] flow
 * @param navHostController A [StateFlow] of [NavHostController] to be managed by the [NavHost]
 * @param rootView The Composable view to display as the root of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun SetupNavHost(
    navHostController: StateFlow<NavHostController?>,
    rootView: @Composable () -> Unit = { Spacer(modifier = Modifier.fillMaxWidth()) },
    builder: RouteContentBuilder,
) {
    val currentNavHostController by navHostController.collectAsState()
    currentNavHostController?.let {
        SetupNavHost(
            navHostController = it,
            startDestination = ROOT_VIEW,
        ) {
            composable(ROOT_VIEW, content = { rootView() })
            builder(navHostController)
        }
    }
}

/**
 * Creates a a [NavHost] for a given [BottomSheetNavigatorState] flow
 * @param bottomSheetNavigatorState A [StateFlow] of [BottomSheetNavigatorState] to be managed by the [NavHost]
 * @param navHostController Converts a [BottomSheetNavigatorState] into a [NavHostController]
 * @param rootView The Composable view to display as the root of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun SetupNavHost(
    bottomSheetNavigatorState: StateFlow<BottomSheetNavigatorState?>,
    navHostController: BottomSheetNavigatorState.() -> NavHostController,
    rootView: @Composable () -> Unit = { Spacer(modifier = Modifier.fillMaxWidth()) },
    builder: BottomSheetContentBuilder,
) {
    val currentBottomSheetNavigatorState by bottomSheetNavigatorState.collectAsState()
    currentBottomSheetNavigatorState?.let {
        SetupNavHost(
            navHostController = it.navHostController(),
            startDestination = ROOT_VIEW,
        ) {
            composable(ROOT_VIEW, content = { rootView() })
            builder(bottomSheetNavigatorState)
        }
    }
}

/**
 * Creates a a [NavHost] for a given [NavHostController]
 * @param navHostController The [NavHostController] tp be managed by the [NavHost]
 * @param startDestination The start destination of the [NavHost]
 * @param builder The [RouteContentBuilder] for building the content of the [NavHost]
 */
@Composable
fun SetupNavHost(navHostController: NavHostController, startDestination: String, builder: NavGraphBuilder.() -> Unit) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        builder = { builder() },
    )
}
