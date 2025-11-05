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

package com.splendo.kaluga.example.bluetooth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.composable
import com.splendo.kaluga.architecture.compose.navigation.RootNavHostComposableNavigator
import com.splendo.kaluga.architecture.compose.navigation.composable
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.LocalAppCompatActivity
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.bluetooth.device.SerializableIdentifier
import com.splendo.kaluga.example.bluetooth.client.BluetoothDeviceLayout
import com.splendo.kaluga.example.bluetooth.client.BluetoothDeviceListLayout
import com.splendo.kaluga.example.bluetooth.server.BluetoothServerLayout
import com.splendo.kaluga.example.compose.Constants
import com.splendo.kaluga.example.resources.compose.ButtonsLayout
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.DeviceDetails
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListNavigationAction
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class BluetoothActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall") // Lint bug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalAppCompatActivity provides this,
            ) {
                BluetoothLayout()
            }
        }
    }
}

@Composable
fun BluetoothLayout() {
    MaterialTheme {
        val viewModel = koinViewModel<BluetoothListViewModel> {
            parametersOf(
                RootNavHostComposableNavigator<BluetoothListNavigationAction>(
                    navigationMapper = { action ->
                        when (action) {
                            is BluetoothListNavigationAction.Server -> action.next
                            is BluetoothListNavigationAction.Client -> action.next
                        }
                    }
                ) { navigationState ->
                    composable(BluetoothListNavigationAction.Server.route()) { BluetoothServerLayout() }
                    composable(BluetoothListNavigationAction.Client.route()) {
                        BluetoothDeviceListLayout(navigationState)
                    }
                    composable<SerializableIdentifier, DeviceDetails>(SerializableIdentifier.serializer()) { identifier ->
                        BluetoothDeviceLayout(identifier.identifier)
                    }
                }
            )
        }
        ViewModelComposable(viewModel) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Constants.Padding.default),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Constants.Padding.default)
                    .padding(WindowInsets.systemBars.asPaddingValues())
                    .verticalScroll(rememberScrollState()),
            ) {
                val bluetooth by bluetooth.state()
                bluetooth.forEach {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onBluetoothSelected(it) },
                    ) {
                        Text(it.title)
                    }
                }
            }
        }
    }
}