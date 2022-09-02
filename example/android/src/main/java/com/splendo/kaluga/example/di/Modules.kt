/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.di

/* ktlint-disable no-wildcard-imports */
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.architecture.ArchitectureDetailsActivity
import com.splendo.kaluga.example.bluetooth.BluetoothMoreActivity
import com.splendo.kaluga.example.resources.ButtonActivity
import com.splendo.kaluga.example.resources.ColorActivity
import com.splendo.kaluga.example.resources.LabelActivity
import com.splendo.kaluga.example.shared.AlertViewModel
import com.splendo.kaluga.example.shared.HudViewModel
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.example.shared.viewmodel.beacons.BeaconsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDeviceDetailViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListNavigation
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.datetimepicker.DateTimePickerViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.InfoNavigation
import com.splendo.kaluga.example.shared.viewmodel.info.InfoViewModel
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserSpecRow
import com.splendo.kaluga.example.shared.viewmodel.link.LinksViewModel
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ColorViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.LabelViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.SystemNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.network.NetworkViewModel
import com.splendo.kaluga.example.system.fragments.NetworkFragment
import com.splendo.kaluga.hud.HUD
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.links.LinksBuilder
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.registerAllPermissions
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.review.ReviewManager
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.net.URL

val utilitiesModule = module {
    single {
        Permissions(
            PermissionsBuilder().apply {
                registerAllPermissions(
                    settings = BasePermissionManager.Settings(
                        logger = RestrictedLogger(RestrictedLogLevel.Verbose)
                    )
                )
            },
            coroutineContext = singleThreadDispatcher("Permissions")
        )
    }
    single { LocationStateRepoBuilder() }
    single {
        BluetoothBuilder().create({ BaseScanner.Settings(get()) })
    }
    single { Beacons(get<Bluetooth>(), timeoutMs = 60_000) }
}

val viewModelModule = module {
    viewModel { (navigator: Navigator<ExampleTabNavigation>) ->
        ExampleViewModel(
            navigator
        )
    }

    viewModel { (navigator: Navigator<FeatureListNavigationAction>) ->
        FeatureListViewModel(
            navigator
        )
    }

    viewModel { (navigator: Navigator<InfoNavigation<*>>) ->
        InfoViewModel(
            ReviewManager.Builder(),
            navigator
        )
    }

    viewModel { (navigator: Navigator<PermissionsListNavigationAction>) ->
        PermissionsListViewModel(
            navigator
        )
    }

    viewModel { (permission: Permission) -> PermissionViewModel(get(), permission) }

    viewModel { (permission: LocationPermission) -> LocationViewModel(permission, get()) }

    viewModel { (navigator: Navigator<SingleValueNavigationAction<InputDetails>>) ->
        ArchitectureInputViewModel(
            navigator
        )
    }

    viewModel { (initialDetail: InputDetails) ->
        ArchitectureDetailsViewModel(
            initialDetail,
            ActivityNavigator {
                NavigationSpec.Close(ArchitectureDetailsActivity.resultCode)
            }
        )
    }

    viewModel {
        AlertViewModel(AlertPresenter.Builder())
    }

    viewModel {
        DateTimePickerViewModel(DateTimePickerPresenter.Builder())
    }

    viewModel {
        HudViewModel(HUD.Builder())
    }

    viewModel { (keyboardBuilder: KeyboardManager.Builder, focusHandler: FocusHandler) ->
        KeyboardViewModel(keyboardBuilder, focusHandler)
    }

    viewModel { (navigator: Navigator<BrowserNavigationActions<BrowserSpecRow>>) ->
        LinksViewModel(
            LinksBuilder(),
            AlertPresenter.Builder(),
            navigator
        )
    }

    viewModel { (navigator: Navigator<SystemNavigationActions<Unit>>) ->
        SystemViewModel(
            navigator
        )
    }

    viewModel {
        NetworkViewModel(NetworkStateRepoBuilder(get()))
    }

    viewModel { (navigator: Navigator<BluetoothListNavigation>) ->
        BluetoothListViewModel(
            get(),
            navigator
        )
    }

    viewModel { (identifier: com.splendo.kaluga.bluetooth.device.Identifier) ->
        BluetoothDeviceDetailViewModel(get(), identifier)
    }

    viewModel {
        BeaconsListViewModel(get())
    }

    viewModel { (navigator: Navigator<ResourcesListNavigationAction>) ->
        ResourcesListViewModel(
            navigator
        )
    }

    viewModel {
        ColorViewModel(AlertPresenter.Builder())
    }

    viewModel {
        LabelViewModel(StyledStringBuilder.Provider())
    }

    viewModel {
        ButtonViewModel(StyledStringBuilder.Provider(), AlertPresenter.Builder())
    }
}
