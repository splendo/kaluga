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

@file:JvmName("AndroidDependencyInjection")

package com.splendo.kaluga.example.shared.di

import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.bluetooth.BluetoothBuilder
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.BluetoothDeviceListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.BluetoothDeviceViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.client.DeviceDetails
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.server.BluetoothServerViewModel
import com.splendo.kaluga.example.shared.viewmodel.datetime.TimerViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.InfoNavigation
import com.splendo.kaluga.example.shared.viewmodel.info.InfoViewModel
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.link.LinksViewModel
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificConverterNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificConverterViewModel
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificUnitSelectionAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificUnitSelectionViewModel
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.SystemNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.network.NetworkViewModel
import com.splendo.kaluga.links.DefaultLinksManager
import com.splendo.kaluga.location.DefaultLocationManager
import com.splendo.kaluga.location.GoogleLocationProvider
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.review.ReviewManager
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

internal val androidModule = module {
    viewModel { (navigator: Navigator<ExampleTabNavigation>) ->
        ExampleViewModel(navigator)
    }

    viewModel { (navigator: Navigator<FeatureListNavigationAction>) ->
        FeatureListViewModel(navigator)
    }

    viewModel { (navigator: Navigator<InfoNavigation<*>>) ->
        InfoViewModel(
            ReviewManager.Builder(),
            navigator,
        )
    }

    viewModel { (navigator: Navigator<PermissionsListNavigationAction>) ->
        PermissionsListViewModel(navigator)
    }

    viewModel { (permission: Permission) -> PermissionViewModel(permission) }

    viewModel { (permission: LocationPermission) -> LocationViewModel(permission) }

    viewModel {
        TimerViewModel(AlertPresenter.Builder())
    }

    viewModel { (navigator: Navigator<BrowserNavigationActions<*>>) ->
        LinksViewModel(
            DefaultLinksManager.Builder(),
            AlertPresenter.Builder(),
            navigator,
        )
    }

    viewModel { (navigator: Navigator<SystemNavigationActions>) ->
        SystemViewModel(
            navigator,
        )
    }

    viewModel {
        NetworkViewModel(NetworkStateRepoBuilder())
    }

    viewModel { (navigator: Navigator<BluetoothListNavigationAction>) ->
        BluetoothListViewModel(navigator)
    }
    viewModel { (navigator: Navigator<BluetoothServerViewModel.CloseNavigationAction>) ->
        BluetoothServerViewModel(AlertPresenter.Builder(), navigator)
    }

    viewModel { (navigator: Navigator<DeviceDetails>) ->
        BluetoothDeviceListViewModel(AlertPresenter.Builder(), navigator)
    }

    viewModel { (identifier: Identifier, navigator: Navigator<BluetoothDeviceViewModel.CloseNavigationAction>) ->
        BluetoothDeviceViewModel(identifier, navigator)
    }

    viewModel { (navigator: Navigator<ScientificNavigationAction<*>>) ->
        ScientificViewModel(AlertPresenter.Builder(), navigator)
    }

    viewModel { (quantity: PhysicalQuantity, navigator: Navigator<ScientificUnitSelectionAction<*>>) ->
        ScientificUnitSelectionViewModel(quantity, navigator)
    }

    viewModel { (arguments: ScientificConverterViewModel.Arguments, navigator: Navigator<ScientificConverterNavigationAction<*>>) ->
        ScientificConverterViewModel(arguments, navigator)
    }
}

fun initKoin(customModules: List<Module> = emptyList()) = initKoin(
    androidModule,
    {
        LocationStateRepoBuilder(
            locationManagerBuilder = DefaultLocationManager.Builder(
                googleLocationProviderSettings = GoogleLocationProvider.Settings(),
            ),
            permissionsBuilder = it,
        )
    },
    { BluetoothBuilder(permissionsBuilder = it) },
    customModules,
)

internal actual val appDeclaration: KoinAppDeclaration = {
    androidContext(ApplicationHolder.applicationContext)
}
