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

package com.splendo.kaluga.example.bluetooth.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.bluetooth.state
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.BluetoothDeviceViewModel
import com.splendo.kaluga.resources.compose.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BluetoothDeviceLayout(identifier: String) {
    val viewModel = koinViewModel<BluetoothDeviceViewModel> { parametersOf(identifier) }

    ViewModelComposable(viewModel) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("${viewModel.name.state().value} - ${viewModel.identifierString}") },
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth()) {
                        Text("RSSI: ${ viewModel.rssi.state().value }")
                        Text("Distance: ${ viewModel.distance.state().value }")
                        Text("State: ${ viewModel.state.state().value }")
                    }
                    viewModel.heartRateViewModel.Layout()
                }
            }
        }
    }
}

@Composable
fun BluetoothDeviceViewModel.HeartRateViewModel.Layout() {
    val heartRate by heartRate.state()
    val isEnergyExpendedVisible by isEnergyExpendedVisible.state()
    val energyExpended by energyExpended.state()
    val isPositionVisible by isPositionVisible.state()
    val position by position.state()

    Column(Modifier.padding(8.dp)) {
        heartRate.Composable()
        if (isEnergyExpendedVisible) {
            Row(Modifier.fillMaxWidth()) {
                energyExpended.Composable()
                resetEnergyExpandedButton.Composable()
            }
        }
        if (isPositionVisible) {
            position.Composable()
            refreshPositionButton.Composable()
        }
    }
}