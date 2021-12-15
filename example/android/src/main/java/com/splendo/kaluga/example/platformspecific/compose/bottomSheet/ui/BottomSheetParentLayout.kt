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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRouteController
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetSheetContentRouteController
import com.splendo.kaluga.architecture.compose.navigation.ModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.navigation.NavHostRouteController
import com.splendo.kaluga.architecture.compose.navigation.NavigatingModalBottomSheetLayout
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.store
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.platformspecific.compose.bottomSheet.viewModel.bottomSheetParentNavigationRouteMapper
import com.splendo.kaluga.example.platformspecific.compose.contacts.ui.Padding
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetNavigation
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetParentNavigation
import com.splendo.kaluga.example.shared.platformspecific.compose.bottomSheet.BottomSheetParentViewModel

@Composable
fun BottomSheetParentLayout() {
    MdcTheme {
        val bottomSheetRouteController = BottomSheetRouteController(
            NavHostRouteController(rememberNavController()),
            BottomSheetSheetContentRouteController(
                rememberNavController(),
                rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
                rememberCoroutineScope()
            )
        )

        bottomSheetRouteController.NavigatingModalBottomSheetLayout(
            sheetContent = { contentNavHostController, sheetContentNavHostController, sheetState ->
                composable(BottomSheetParentNavigation.ShowSheet.route()) {
                    BottomSheetLayout(contentNavHostController, sheetContentNavHostController, sheetState)
                }
                composable(BottomSheetNavigation.SubPage.route()) {
                    BottomSheetSubPageLayout(
                        contentNavHostController, sheetContentNavHostController, sheetState
                    )
                }
            },
            contentRoot = { contentNavHostController, sheetContentNavHostController, sheetState ->
                BottomSheetParentLayoutContent(contentNavHostController, sheetContentNavHostController, sheetState)
            },
            content = { contentNavHostController, _, _ ->
                composable(BottomSheetParentNavigation.SubPage.route()) {
                    BottomSheetParentSubPageLayout(contentNavHostController)
                }
            }
        )
    }
}

@Composable
fun BottomSheetParentLayoutContent(contentNavHostController: NavHostController, sheetNavHostController: NavHostController, sheetState: ModalBottomSheetState) {

    val navigator = ModalBottomSheetNavigator(
        NavHostRouteController(contentNavHostController),
        BottomSheetSheetContentRouteController(
            sheetNavHostController,
            sheetState,
            rememberCoroutineScope()
        ),
        ::bottomSheetParentNavigationRouteMapper
    )

    val viewModel = store {
        remember {
            BottomSheetParentViewModel(navigator)
        }
    }

    ViewModelComposable(viewModel) {
        Column(Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding.default),
                onClick = { onShowSheetPressed() }
            ) {
                Text(sheetText)
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Padding.default),
                onClick = { onSubPagePressed() }
            ) {
                Text(subPageText)
            }
        }
    }
}
