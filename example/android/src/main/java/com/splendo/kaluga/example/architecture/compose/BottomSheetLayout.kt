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

package com.splendo.kaluga.example.architecture.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetNavigatorState
import com.splendo.kaluga.architecture.compose.navigation.HardwareBackButtonNavigation
import com.splendo.kaluga.architecture.compose.navigation.ModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetViewModel
import com.splendo.kaluga.resources.compose.Composable
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BottomSheetLayout(bottomSheetNavigationState: StateFlow<BottomSheetNavigatorState?>) {
    val viewModel = koinViewModel<BottomSheetViewModel> {
        parametersOf(
            ModalBottomSheetNavigator<BottomSheetNavigation>(
                bottomSheetNavigationState,
                navigationMapper = { bottomSheetNavigationRouteMapper(it) },
            ),
        )
    }

    ViewModelComposable(viewModel) {
        HardwareBackButtonNavigation(onBackButtonClickHandler = { onClosePressed() })
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Constants.Padding.default),
        ) {
            Text(text)
            button.Composable(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Padding.default),
            )
        }
    }
}
