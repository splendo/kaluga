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

package com.splendo.kaluga.example.keyboard.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.keyboard.compose.ComposeFocusHandler
import com.splendo.kaluga.keyboard.compose.ComposeKeyboardManager
import com.splendo.kaluga.resources.compose.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

val composeKeyboardViewModel = "ComposeKeyboardViewModel"

class ComposeKeyboardActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider {
                KeyboardLayout()
            }
        }
    }
}

@Composable
fun KeyboardLayout() {
    MaterialTheme {
        val focusHandler = LocalFocusManager.current
        val focusRequester = remember { FocusRequester() }
        val viewModel =
            koinViewModel<KeyboardViewModel<ComposeFocusHandler>>(named(composeKeyboardViewModel)) {
                parametersOf(
                    ComposeKeyboardManager.Builder(),
                )
            }

        ViewModelComposable(viewModel) {
            LaunchedEffect(focusRequester) {
                viewModel.editFieldFocusHandler.post(ComposeFocusHandler(focusRequester))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Padding.default)
                    .verticalScroll(rememberScrollState()),
            ) {
                var text by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusHandler.clearFocus() },
                    ),
                )
                showButton.Composable(modifier = Modifier.fillMaxWidth())
                hideButton.Composable(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
