/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.bluetooth.server

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.NavHostComposableNavigator
import com.splendo.kaluga.architecture.compose.navigation.back
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.server.BluetoothServerViewModel
import com.splendo.kaluga.resources.compose.Composable
import kotlinx.coroutines.flow.StateFlow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BluetoothServerLayout(navHostController: StateFlow<NavHostController?>) {
    val viewModel = koinViewModel<BluetoothServerViewModel> {
        parametersOf(
            NavHostComposableNavigator<BluetoothServerViewModel.CloseNavigationAction>(navHostController) {
                it.back
            }
        )
    }

    BackHandler(onBack = viewModel::close)

    ViewModelComposable(viewModel) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(WindowInsets.systemBars.asPaddingValues()
                ), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            status.state().value.Composable()

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                decreaseBPM.Composable()
                heartRateLabel.state().value.Composable()
                increaseBPM.Composable()
            }

            energyExpendedLabel.state().value.Composable()
            positionPicker.state().value.Composable()
        }
    }
}
