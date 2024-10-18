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

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.compose.navigation.RootNavHostComposableNavigator
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ComposeResourcesActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this,
            ) {
                ResourcesLayout()
            }
        }
    }
}

@Composable
fun ResourcesLayout() {
    MaterialTheme {
        val viewModel = koinViewModel<ResourcesListViewModel> {
            parametersOf(
                RootNavHostComposableNavigator<ResourcesListNavigationAction>(
                    navigationMapper = { action ->
                        when (action) {
                            is ResourcesListNavigationAction.Button -> action.next
                            is ResourcesListNavigationAction.Color -> action.next
                            is ResourcesListNavigationAction.Image -> action.next
                            is ResourcesListNavigationAction.Label -> action.next
                        }
                    },
                ) {
                    composable(ResourcesListNavigationAction.Button.route()) { ButtonsLayout() }
                    composable(ResourcesListNavigationAction.Color.route()) { ColorsLayout() }
                    composable(ResourcesListNavigationAction.Image.route()) { ImagesLayout() }
                    composable(ResourcesListNavigationAction.Label.route()) { LabelsLayout() }
                },
            )
        }
        ViewModelComposable(viewModel) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Padding.default)
                    .verticalScroll(rememberScrollState()),
            ) {
                val resources by resources.state()
                resources.forEach {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onResourceSelected(it) },
                    ) {
                        Text(it.title)
                    }
                }
            }
        }
    }
}
