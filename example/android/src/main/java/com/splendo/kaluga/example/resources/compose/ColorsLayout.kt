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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.resources.ColorViewModel
import com.splendo.kaluga.resources.compose.Composable
import com.splendo.kaluga.resources.compose.backgroundStyle
import com.splendo.kaluga.resources.stylable.KalugaBackgroundStyle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ColorsLayout() {
    val viewModel = koinViewModel<ColorViewModel>()

    ViewModelComposable(viewModel) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.Padding.default)
                .verticalScroll(rememberScrollState()),
        ) {
            val mainColorSize = 80.dp
            val backdropText by backdropText.state()
            val sourceText by sourceText.state()
            val backdropColorBackground by backdropColorBackground.state()
            val blendedColorBackground by blendedColorBackground.state()
            val sourceColorBackground by sourceColorBackground.state()
            val blendModeButton by blendModeButton.state()
            val lightenBackdrops by lightenBackdrops.state()
            val darkenBackdrops by darkenBackdrops.state()
            val lightenSource by lightenSource.state()
            val darkenSource by darkenSource.state()
            val lightenBlended by lightenBlended.state()
            val darkenBlended by darkenBlended.state()

            val focusManager = LocalFocusManager.current

            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.width(mainColorSize)) {
                    var backdropTempText by remember { mutableStateOf(backdropText) }
                    Box(
                        modifier = Modifier
                            .size(mainColorSize)
                            .backgroundStyle(backdropColorBackground),
                    )
                    OutlinedTextField(
                        value = backdropTempText,
                        onValueChange = { backdropTempText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (!it.hasFocus) submitBackdropText(backdropTempText) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() },
                        ),
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Box(
                    modifier = Modifier
                        .size(mainColorSize)
                        .backgroundStyle(blendedColorBackground),
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Column(Modifier.width(mainColorSize)) {
                    var sourceTempText by remember { mutableStateOf(sourceText) }
                    Box(
                        modifier = Modifier
                            .size(mainColorSize)
                            .backgroundStyle(sourceColorBackground),
                    )
                    OutlinedTextField(
                        value = sourceTempText,
                        onValueChange = { sourceTempText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (!it.hasFocus) submitSourceText(sourceTempText) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() },
                        ),
                    )
                }
            }

            blendModeButton.Composable(modifier = Modifier.fillMaxWidth())
            flipButton.Composable(modifier = Modifier.fillMaxWidth())

            ListOfColors(lightenBackdrops)
            ListOfColors(darkenBackdrops)
            ListOfColors(lightenSource)
            ListOfColors(darkenSource)
            ListOfColors(lightenBlended)
            ListOfColors(darkenBlended)
        }
    }
}

@Composable
fun ListOfColors(list: List<KalugaBackgroundStyle>) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        list.forEach {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .backgroundStyle(it),
            )
        }
    }
}
