/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.platformspecific.compose.bottomSheet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRouteController
import com.splendo.kaluga.architecture.compose.navigation.ModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.navigation.NavHostRouteController
import com.splendo.kaluga.architecture.compose.navigation.NavigatingModalBottomSheetLayout
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.store
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.platformspecific.compose.bottomSheet.viewModel.bottomSheetParentNavigationRouteMapper
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetNavigation
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetParentNavigation
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetParentViewModel

@Composable
fun BottomSheetParentLayout() {
    MdcTheme {
        val navigator = ModalBottomSheetNavigator(
            NavHostRouteController(rememberNavController()),
            BottomSheetRouteController(
                rememberNavController(),
                rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
                rememberCoroutineScope()
            ),
            ::bottomSheetParentNavigationRouteMapper
        )

        NavigatingModalBottomSheetLayout(
            navigator = navigator,
            sheetContent = { sheetContentRouteController, contentRouteController ->
                composable(BottomSheetParentNavigation.ShowSheet.route()) {
                    BottomSheetLayout(contentRouteController, sheetContentRouteController)
                }
                composable(BottomSheetNavigation.SubPage.route()) {
                    BottomSheetSubPageLayout(
                        contentRouteController,
                        sheetContentRouteController
                    )
                }
            },
            contentRoot = {
                BottomSheetParentLayoutContent(navigator = it)
            },
            content = { contentRouteController ->
                composable(BottomSheetParentNavigation.SubPage.route()) {
                    BottomSheetParentSubPageLayout(contentRouteController)
                }
            }
        )
    }
}

@Composable
fun BottomSheetParentLayoutContent(navigator: Navigator<BottomSheetParentNavigation>) {

    val viewModel = store {
        remember {
            BottomSheetParentViewModel(navigator)
        }
    }

    ViewModelComposable(viewModel) {
        Column(Modifier.fillMaxWidth()) {
            Button(onClick = { onShowSheetPressed() }) {
                Text(sheetText)
            }
            Button(onClick = { onSubPagePressed() }) {
                Text(subPageText)
            }
        }
    }
}