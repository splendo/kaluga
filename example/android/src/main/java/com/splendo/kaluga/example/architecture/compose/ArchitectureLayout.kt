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
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.mutableState
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetRouteController
import com.splendo.kaluga.architecture.compose.navigation.BottomSheetSheetContentRouteController
import com.splendo.kaluga.architecture.compose.navigation.HandleResult
import com.splendo.kaluga.architecture.compose.navigation.ModalBottomSheetNavigator
import com.splendo.kaluga.architecture.compose.navigation.NavHostRouteController
import com.splendo.kaluga.architecture.compose.navigation.NavigatingModalBottomSheetLayout
import com.splendo.kaluga.architecture.compose.navigation.composable
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.storeAndRemember
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.resources.compose.Composable

class ComposeArchitectureActivity : AppCompatActivity() {

    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this
            ) {
                ArchitectureLayout()
            }
        }
    }
}

@Composable
fun ArchitectureLayout() {
    MdcTheme {
        val architectureRouteController = BottomSheetRouteController(
            NavHostRouteController(rememberNavController()),
            BottomSheetSheetContentRouteController(
                rememberNavController(),
                rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
                rememberCoroutineScope()
            )
        )

        architectureRouteController.NavigatingModalBottomSheetLayout(
            sheetContent = { contentNavHostController, sheetContentNavHostController, sheetState ->
                composable(ArchitectureNavigationAction.BottomSheet.route()) {
                    BottomSheetLayout(contentNavHostController, sheetContentNavHostController, sheetState)
                }
                composable(BottomSheetNavigation.SubPage.route()) {
                    BottomSheetSubPageLayout(
                        contentNavHostController, sheetContentNavHostController, sheetState
                    )
                }
            },
            contentRoot = { contentNavHostController, sheetContentNavHostController, sheetState ->
                ArchitectureLayoutLayoutContent(contentNavHostController, sheetContentNavHostController, sheetState)
            },
            content = { contentNavHostController, _, _ ->
                composable<InputDetails, ArchitectureNavigationAction.Details>(
                    type = NavigationBundleSpecType.SerializedType(InputDetails.serializer())
                ) { inputDetails ->
                    ArchitectureDetailsLayout(inputDetails, contentNavHostController)
                }
            }
        )
    }
}

@Composable
fun ArchitectureLayoutLayoutContent(contentNavHostController: NavHostController, sheetNavHostController: NavHostController, sheetState: ModalBottomSheetState) {

    val navigator = ModalBottomSheetNavigator(
        NavHostRouteController(contentNavHostController),
        BottomSheetSheetContentRouteController(
            sheetNavHostController,
            sheetState,
            rememberCoroutineScope()
        ),
        ::architectureNavigationRouteMapper
    )

    val viewModel = storeAndRemember {
        ArchitectureViewModel(navigator)
    }

    ViewModelComposable(viewModel) {
        val nameInput = nameInput.mutableState()
        val isNameValid by isNameValid.state()
        val numberInput = numberInput.mutableState()
        val isNumberValid by isNumberValid.state()

        val focusManager = LocalFocusManager.current

        contentNavHostController.HandleResult(NavigationBundleSpecType.SerializedType(InputDetails.serializer())) {
            nameInput.value = name
            numberInput.value = number.toString()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
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
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            OutlinedTextField(
                value = numberInput.value,
                onValueChange = { numberInput.value = it },
                modifier = Modifier.fillMaxWidth(),
                isError = !isNumberValid,
                placeholder = { Text(numberPlaceholder) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            showDetailsButton.Composable(modifier = Modifier.fillMaxWidth())
            showBottomSheetButton.Composable(modifier = Modifier.fillMaxWidth())
        }
    }
}