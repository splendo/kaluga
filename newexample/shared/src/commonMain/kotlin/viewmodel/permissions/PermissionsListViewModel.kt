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

package com.splendo.kaluga.example.shared.viewmodel.permissions

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.registerAllPermissionsNotRegistered
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
enum class PermissionView(val title: String) {
    Bluetooth("permissions_bluetooth".localized()),
    Calendar("permissions_calendar".localized()),
    Camera("permissions_camera".localized()),
    Contacts("permissions_contacts".localized()),
    Location("permissions_location".localized()),
    Microphone("permissions_microphone".localized()),
    Notifications("permissions_notifications".localized()),
    Storage("permissions_storage".localized())
}

object PermissionNavigationBundleSpecRow : NavigationBundleSpecRow<PermissionView>(NavigationBundleSpecType.SerializedType(PermissionView.serializer()))
class PermissionNavigationBundleSpec : NavigationBundleSpec<PermissionNavigationBundleSpecRow>(setOf(PermissionNavigationBundleSpecRow))

class PermissionsListNavigationAction(permissionView: PermissionView) : NavigationAction<PermissionNavigationBundleSpecRow>(PermissionNavigationBundleSpec().toBundle { spec -> spec.convertValue(permissionView) })

class PermissionsListViewModel(private val permissionsBuilder: PermissionsBuilder, navigator: Navigator<PermissionsListNavigationAction>) : NavigatingViewModel<PermissionsListNavigationAction>(navigator) {

    val permissions = observableOf(
        listOf(
            PermissionView.Bluetooth,
            PermissionView.Calendar,
            PermissionView.Camera,
            PermissionView.Contacts,
            PermissionView.Location,
            PermissionView.Microphone,
            PermissionView.Notifications,
            PermissionView.Storage
        )
    )

    fun onPermissionPressed(permissionView: PermissionView) {
        coroutineScope.launch {
            permissionsBuilder.registerAllPermissionsNotRegistered(
                settings = BasePermissionManager.Settings(
                    logger = RestrictedLogger(RestrictedLogLevel.None)
                )
            )
            navigator.navigate(PermissionsListNavigationAction(permissionView))
        }
    }
}
