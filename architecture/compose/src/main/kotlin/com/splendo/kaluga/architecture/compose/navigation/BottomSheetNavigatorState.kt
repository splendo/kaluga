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

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.StateFlow

/**
 * The State of a [BottomSheetNavigator]
 * @property contentNavHostController The [NavHostController] managing the content of the bottom sheet.
 * @property sheetContentNavHostController The [NavHostController] managing the sheet content of the bottom sheet.
 * @property scaffoldState The [BottomSheetScaffoldState] of the bottom sheet.
 */
data class BottomSheetNavigatorState(
    val contentNavHostController: NavHostController,
    val sheetContentNavHostController: NavHostController,
    val scaffoldState: BottomSheetScaffoldState,
)

typealias BottomSheetContentBuilder = NavGraphBuilder.(bottomSheetNavigationState: StateFlow<BottomSheetNavigatorState?>) -> Unit
