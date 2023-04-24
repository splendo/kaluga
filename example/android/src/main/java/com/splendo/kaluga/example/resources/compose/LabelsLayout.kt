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

package com.splendo.kaluga.example.resources.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.resources.LabelViewModel
import com.splendo.kaluga.resources.compose.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun LabelsLayout() {
    val viewModel = koinViewModel<LabelViewModel>()

    ViewModelComposable(viewModel) {
        val labels by labels.state()
        Column(
            verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.Padding.default)
                .verticalScroll(rememberScrollState()),
        ) {
            labels.forEach {
                it.Composable(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
