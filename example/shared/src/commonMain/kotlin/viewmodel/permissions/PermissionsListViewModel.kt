package com.splendo.kaluga.example.shared.viewmodel.permissions

import com.splendo.kaluga.architecture.navigation.*
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import kotlinx.serialization.Serializable

@Serializable
enum class PermissionView(val title: String) {
    Bluetooth("permissions_bluetooth"),
    Calendar("permissions_calendar"),
    Camera("permissions_camera"),
    Contacts("permissions_contacts"),
    Location("permissions_location"),
    Microphone("permissions_microphone"),
    Notifications("permissions_notifications"),
    Storage("permissions_storage")
}

object PermissionNavigationBundleSpecRow : NavigationBundleSpecRow<PermissionView>(NavigationBundleSpecType.SerializedType(PermissionView.serializer()))
class PermissionNavigationBundleSpec: NavigationBundleSpec<PermissionNavigationBundleSpecRow>(setOf(PermissionNavigationBundleSpecRow))

class PermissionsListNavigationAction(permissionView: PermissionView) : NavigationAction<PermissionNavigationBundleSpecRow>(PermissionNavigationBundleSpec().toBundle { spec -> spec.convertValue(permissionView) })

class PermissionsListViewModel(navigator: Navigator<PermissionsListNavigationAction>) : NavigatingViewModel<PermissionsListNavigationAction>(navigator) {

    val permissions = observableOf(listOf(
        PermissionView.Bluetooth,
        PermissionView.Calendar,
        PermissionView.Camera,
        PermissionView.Contacts,
        PermissionView.Location,
        PermissionView.Microphone,
        PermissionView.Notifications,
        PermissionView.Storage
    ))

    fun onPermissionPressed(permissionView: PermissionView) {
        navigator.navigate(PermissionsListNavigationAction(permissionView))
    }

}