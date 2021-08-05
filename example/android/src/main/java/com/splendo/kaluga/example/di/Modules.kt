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

import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.datetimepicker.DateTimePickerPresenter
import com.splendo.kaluga.example.FeaturesListFragment
import com.splendo.kaluga.example.InfoDialog
import com.splendo.kaluga.example.InfoFragment
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.architecture.ArchitectureDetailsActivity
import com.splendo.kaluga.example.architecture.ArchitectureInputActivity
import com.splendo.kaluga.example.bluetooth.BluetoothActivity
import com.splendo.kaluga.example.bluetooth.BluetoothMoreActivity
import com.splendo.kaluga.example.datetimepicker.DateTimePickerActivity
import com.splendo.kaluga.example.keyboard.KeyboardManagerActivity
import com.splendo.kaluga.example.link.LinksActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoListActivity
import com.splendo.kaluga.example.shared.AlertViewModel
import com.splendo.kaluga.example.shared.HudViewModel
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDeviceDetailViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.example.shared.viewmodel.datetimepicker.DateTimePickerViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.DialogSpecRow
import com.splendo.kaluga.example.shared.viewmodel.info.InfoNavigation
import com.splendo.kaluga.example.shared.viewmodel.info.InfoViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.LinkSpecRow
import com.splendo.kaluga.example.shared.viewmodel.info.MailSpecRow
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserSpecRow
import com.splendo.kaluga.example.shared.viewmodel.link.LinksViewModel
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.SystemNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.network.NetworkViewModel
import com.splendo.kaluga.example.system.SystemActivity
import com.splendo.kaluga.example.system.fragments.NetworkFragment
import com.splendo.kaluga.hud.HUD
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.links.LinksBuilder
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.calendar.registerCalendarPermission
import com.splendo.kaluga.permissions.camera.registerCameraPermission
import com.splendo.kaluga.permissions.contacts.registerContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.storage.registerStoragePermission
import com.splendo.kaluga.review.ReviewManager
import com.splendo.kaluga.system.network.state.NetworkStateRepoBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation.FeatureList
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation.Info
import java.net.URL

val utilitiesModule = module {
    single {
        Permissions(
            PermissionsBuilder().apply {
                registerBluetoothPermission()
                registerCameraPermission()
                registerStoragePermission()
                registerLocationPermission()
                registerNotificationsPermission()
                registerContactsPermission()
                registerMicrophonePermission()
                registerCalendarPermission()
            }
        )
    }
    single { LocationStateRepoBuilder() }
    single { BluetoothBuilder().create() }
    single { BluetoothMonitor.Builder().create() }
}

val viewModelModule = module {
    viewModel {
        ExampleViewModel(
            ActivityNavigator { action ->
                val navigationSpec: NavigationSpec = when (action) {
                    FeatureList -> NavigationSpec.Fragment(
                        R.id.example_fragment,
                        createFragment = { FeaturesListFragment() })
                    Info -> NavigationSpec.Fragment(
                        R.id.example_fragment,
                        createFragment = { InfoFragment() })
                }
                navigationSpec
            }
        )
    }

    viewModel {
        FeatureListViewModel(
            ActivityNavigator { action ->
                when (action) {
                    FeatureListNavigationAction.Location -> NavigationSpec.Activity(LocationActivity::class.java)
                    FeatureListNavigationAction.Permissions -> NavigationSpec.Activity(PermissionsDemoListActivity::class.java)
                    FeatureListNavigationAction.Alerts -> NavigationSpec.Activity(AlertsActivity::class.java)
                    FeatureListNavigationAction.DateTimePicker -> NavigationSpec.Activity(DateTimePickerActivity::class.java)
                    FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity(LoadingActivity::class.java)
                    FeatureListNavigationAction.Architecture -> NavigationSpec.Activity(ArchitectureInputActivity::class.java)
                    FeatureListNavigationAction.Keyboard -> NavigationSpec.Activity(KeyboardManagerActivity::class.java)
                    FeatureListNavigationAction.Links -> NavigationSpec.Activity(LinksActivity::class.java)
                    FeatureListNavigationAction.System -> NavigationSpec.Activity(SystemActivity::class.java)
                    FeatureListNavigationAction.Bluetooth -> NavigationSpec.Activity(BluetoothActivity::class.java)
                }
            }
        )
    }

    viewModel {
        InfoViewModel(
            ReviewManager.Builder(),
            ActivityNavigator { action ->
                when (action) {
                    is InfoNavigation.Dialog -> {
                        val title = action.bundle?.get(DialogSpecRow.TitleRow) ?: ""
                        val message = action.bundle?.get(DialogSpecRow.MessageRow) ?: ""
                        NavigationSpec.Dialog(createDialog = {
                            InfoDialog(title, message)
                        })
                    }
                    is InfoNavigation.Link -> NavigationSpec.Browser(
                        URL(action.bundle!!.get(LinkSpecRow.LinkRow)),
                        NavigationSpec.Browser.Type.Normal
                    )
                    is InfoNavigation.Mail -> NavigationSpec.Email(NavigationSpec.Email.EmailSettings(
                        to = action.bundle?.get(MailSpecRow.ToRow) ?: emptyList(),
                        subject = action.bundle?.get(MailSpecRow.SubjectRow)
                    ))
                }
            })
    }

    viewModel {
        PermissionsListViewModel(
            ActivityNavigator {
                NavigationSpec.Activity(PermissionsDemoActivity::class.java)
            })
    }

    viewModel { (permission: Permission) -> PermissionViewModel(get(), permission) }

    viewModel { (permission: LocationPermission) -> LocationViewModel(permission, get()) }

    viewModel {
        ArchitectureInputViewModel(
            ActivityNavigator {
                NavigationSpec.Activity(ArchitectureDetailsActivity::class.java, requestCode = ArchitectureInputActivity.requestCode)
            }
        )
    }

    viewModel { (initialDetail: InputDetails) ->
        ArchitectureDetailsViewModel(initialDetail, ActivityNavigator {
            NavigationSpec.Close(ArchitectureDetailsActivity.resultCode)
        })
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

    viewModel {
        LinksViewModel(
            LinksBuilder(),
            AlertPresenter.Builder(),
            ActivityNavigator {
                when (it) {
                    is BrowserNavigationActions.OpenWebView -> NavigationSpec.Browser(
                        URL(it.bundle!!.get(BrowserSpecRow.UrlSpecRow)),
                        NavigationSpec.Browser.Type.Normal
                    )
                }
            }
        )
    }

    viewModel {
        SystemViewModel(
            ActivityNavigator {
                when (it) {
                    SystemNavigationActions.Network -> NavigationSpec.Fragment(
                        R.id.system_features_fragment,
                        createFragment = { NetworkFragment() }
                    )
                }
            }
        )
    }

    viewModel {
        NetworkViewModel(NetworkStateRepoBuilder(get()))
    }

    viewModel {

        BluetoothListViewModel(get(), get(), ActivityNavigator {
            NavigationSpec.Activity(BluetoothMoreActivity::class.java)
        })
    }

    viewModel { (identifier:  com.splendo.kaluga.bluetooth.device.Identifier) ->
        BluetoothDeviceDetailViewModel(get(), identifier)
    }

}
