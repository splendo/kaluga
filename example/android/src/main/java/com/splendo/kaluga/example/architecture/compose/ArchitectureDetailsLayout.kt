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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.HardwareBackButtonNavigation
import com.splendo.kaluga.architecture.compose.navigation.RouteNavigator
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.storeAndRemember
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.resources.compose.Composable

@Composable
fun ArchitectureDetailsLayout(inputDetails: InputDetails, navHostController: NavHostController) {
    val navigator = RouteNavigator(
        navHostController,
        ::architectureDetailsNavigationRouteMapper
    )

    val viewModel = storeAndRemember {
        ArchitectureDetailsViewModel(inputDetails, navigator)
    }

    ViewModelComposable(viewModel) {
        val nameText by name.state()
        val numberText by number.state()
        HardwareBackButtonNavigation(onBackButtonClickHandler = { onBackPressed() })
        Column(Modifier.fillMaxWidth()) {
            Text(nameText)
            Text(numberText)
            inverseButton.Composable(modifier = Modifier.fillMaxWidth())
            finishButton.Composable(modifier = Modifier.fillMaxWidth())
        }
    }
}