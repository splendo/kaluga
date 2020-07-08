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

import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.FeaturesListFragment
import com.splendo.kaluga.example.InfoDialog
import com.splendo.kaluga.example.InfoFragment
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.architecture.ArchitectureDetailsActivity
import com.splendo.kaluga.example.architecture.ArchitectureInputActivity
import com.splendo.kaluga.example.bluetooth.BluetoothActivity
import com.splendo.kaluga.example.keyboard.KeyboardManagerActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoListActivity
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.*
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.keyboard.KeyboardHostingView
import com.splendo.kaluga.keyboard.KeyboardManagerBuilder
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.net.URL

val utilitiesModule = module {
    single { Permissions(PermissionsBuilder()) }
    single { LocationStateRepoBuilder() }
}

val viewModelModule = module {
    viewModel {
        ExampleViewModel(
            Navigator { action ->
                when(action) {
                    is ExampleTabNavigation.FeatureList -> NavigationSpec.Fragment(R.id.example_fragment, createFragment = {FeaturesListFragment()})
                    is ExampleTabNavigation.Info -> NavigationSpec.Fragment(R.id.example_fragment, createFragment = {InfoFragment()})
                }
            }
        )
    }

    viewModel {
        FeatureListViewModel(
            Navigator { action ->
                when (action) {
                    is FeatureListNavigationAction.Alerts -> NavigationSpec.Activity(AlertsActivity::class.java)
                    is FeatureListNavigationAction.Architecture -> NavigationSpec.Activity(ArchitectureInputActivity::class.java)
                    is FeatureListNavigationAction.Bluetooth -> NavigationSpec.Activity(BluetoothActivity::class.java)
                    is FeatureListNavigationAction.Keyboard -> NavigationSpec.Activity(KeyboardManagerActivity::class.java)
                    is FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity(LoadingActivity::class.java)
                    is FeatureListNavigationAction.Location -> NavigationSpec.Activity(LocationActivity::class.java)
                    is FeatureListNavigationAction.Permissions -> NavigationSpec.Activity(PermissionsDemoListActivity::class.java)
                }
            })
    }

    viewModel {
        InfoViewModel(
            Navigator { action ->
                when (action) {
                    is InfoNavigation.Dialog -> {
                        val title = action.bundle?.get(DialogSpecRow.TitleRow) ?: ""
                        val message = action.bundle?.get(DialogSpecRow.MessageRow) ?: ""
                        NavigationSpec.Dialog(createDialog = {
                            InfoDialog(title, message)
                        })
                    }
                    is InfoNavigation.Link -> NavigationSpec.Browser(URL(action.bundle!!.get(LinkSpecRow.LinkRow)))
                    is InfoNavigation.Mail -> NavigationSpec.Email(NavigationSpec.Email.EmailSettings(to = action.bundle?.get(MailSpecRow.ToRow) ?: emptyList(), subject = action.bundle?.get(MailSpecRow.SubjectRow)))
                }
            })
    }

    viewModel {
        PermissionsListViewModel(
            Navigator {
                NavigationSpec.Activity(PermissionsDemoActivity::class.java)
            })
    }

    viewModel { (permission: Permission) -> PermissionViewModel(get(), permission) }

    viewModel { (permission: Permission.Location) -> LocationViewModel(permission, get()) }

    viewModel {
        ArchitectureInputViewModel(
            Navigator {
                NavigationSpec.Activity(ArchitectureDetailsActivity::class.java, requestCode = ArchitectureInputActivity.requestCode)
            }
        )
    }

    viewModel { (name: String, number: Int) ->
        ArchitectureDetailsViewModel(name, number, Navigator {
            NavigationSpec.Close(ArchitectureDetailsActivity.resultCode)
        })
    }

    viewModel { (keyboardManagerBuilder: () -> KeyboardManagerBuilder, keyboardHostingView: () -> KeyboardHostingView) ->
        KeyboardViewModel(keyboardManagerBuilder, keyboardHostingView)
    }
}