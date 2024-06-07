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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.compose.mutableState
import com.splendo.kaluga.architecture.compose.navigation.RootModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.navigation.composable
import com.splendo.kaluga.architecture.compose.navigation.result.NavHostResultHandler
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.resources.compose.Composable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ComposeArchitectureActivity : AppCompatActivity() {

    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this,
            ) {
                ArchitectureLayout()
            }
        }
    }
}

@Composable
fun ArchitectureLayout() {
    MaterialTheme {
        val viewModel = koinViewModel<ArchitectureViewModel> {
            parametersOf(
                RootModalBottomSheetNavigator<ArchitectureNavigationAction<*>>(
                    navigationMapper = { architectureNavigationRouteMapper(it) },
                    contentRootResultHandlers = listOf(
                        InputDetails.serializer().NavHostResultHandler<ArchitectureViewModel, InputDetails> {
                            nameInput.post(it.name)
                            numberInput.post(it.number.toString())
                        },
                    ),
                    contentBuilder = { bottomSheetNavigationState ->
                        composable<InputDetails, ArchitectureNavigationAction.Details>(
                            InputDetails.serializer(),
                        ) { inputDetails ->
                            ArchitectureDetailsLayout(inputDetails, bottomSheetNavigationState)
                        }
                    },
                    sheetContentBuilder = { bottomSheetNavigationState ->
                        composable(ArchitectureNavigationAction.BottomSheet.route()) {
                            BottomSheetLayout(
                                bottomSheetNavigationState,
                            )
                        }
                        composable(BottomSheetNavigation.SubPage.route()) {
                            BottomSheetSubPageLayout(
                                bottomSheetNavigationState,
                            )
                        }
                    },
                ),
            )
        }

        ViewModelComposable(viewModel) {
            val nameInput = nameInput.mutableState()
            val isNameValid by isNameValid.state()
            val numberInput = numberInput.mutableState()
            val isNumberValid by isNumberValid.state()

            val focusManager = LocalFocusManager.current

            Column(
                verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                OutlinedTextField(
                    value = nameInput.value,
                    onValueChange = { nameInput.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent {
                            if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                                focusManager.moveFocus(FocusDirection.Down)
                                true
                            } else {
                                false
                            }
                        },
                    isError = !isNameValid,
                    placeholder = { Text(namePlaceholder) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    ),
                )
                OutlinedTextField(
                    value = numberInput.value,
                    onValueChange = { numberInput.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isNumberValid,
                    placeholder = { Text(numberPlaceholder) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                )
                showDetailsButton.Composable(modifier = Modifier.fillMaxWidth())
                showBottomSheetButton.Composable(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
