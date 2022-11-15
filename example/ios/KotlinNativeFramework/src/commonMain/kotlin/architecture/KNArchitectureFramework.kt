/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package architecture

/* ktlint-disable no-wildcard-imports */
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.ViewControllerNavigator
import com.splendo.kaluga.architecture.observable.Disposable
import com.splendo.kaluga.architecture.observable.DisposeBag
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import com.splendo.kaluga.architecture.viewmodel.LifecycleManager
import com.splendo.kaluga.architecture.viewmodel.addLifecycleManager
import com.splendo.kaluga.architecture.viewmodel.onLifeCycleChanged
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.beacons.Beacons
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.example.shared.viewmodel.beacons.BeaconsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothDeviceDetailViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.BluetoothListViewModel
import com.splendo.kaluga.example.shared.viewmodel.bluetooth.DeviceDetailsSpecRow
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.*
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserSpecRow
import com.splendo.kaluga.example.shared.viewmodel.link.LinksViewModel
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionNavigationBundleSpecRow
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ButtonViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ColorViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.LabelViewModel
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListViewModel
import com.splendo.kaluga.example.shared.viewmodel.system.SystemNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import com.splendo.kaluga.keyboard.FocusHandler
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.links.LinksBuilder
import com.splendo.kaluga.links.manager.PlatformLinksHandler
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.base.Permission
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.calendar.CalendarPermission
import com.splendo.kaluga.permissions.camera.CameraPermission
import com.splendo.kaluga.permissions.contacts.ContactsPermission
import com.splendo.kaluga.permissions.location.LocationPermission
import com.splendo.kaluga.permissions.microphone.MicrophonePermission
import com.splendo.kaluga.permissions.notifications.*
import com.splendo.kaluga.permissions.storage.StoragePermission
import com.splendo.kaluga.resources.StyledStringBuilder
import com.splendo.kaluga.review.ReviewManager
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.UIKit.*
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound

class KNArchitectureFramework {

    fun createExampleViewModel(
        parent: UIViewController,
        containerView: UIView,
        featuresList: () -> UIViewController,
        info: () -> UIViewController
    ): ExampleViewModel {
        return ExampleViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Nested(
                    NavigationSpec.Nested.Type.Replace(1),
                    containerView,
                    when (action) {
                        is ExampleTabNavigation.FeatureList -> featuresList
                        is ExampleTabNavigation.Info -> info
                    }
                )
            }
        )
    }

    fun createFeatureListViewModel(parent: UIViewController): FeatureListViewModel {
        return FeatureListViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Segue(
                    when (action) {
                        is FeatureListNavigationAction.Location -> "showLocation"
                        is FeatureListNavigationAction.Permissions -> "showPermissions"
                        is FeatureListNavigationAction.Alerts -> "showAlerts"
                        is FeatureListNavigationAction.DateTimePicker -> "showDateTimePicker"
                        is FeatureListNavigationAction.LoadingIndicator -> "showHUD"
                        is FeatureListNavigationAction.Architecture -> "showArchitecture"
                        is FeatureListNavigationAction.Keyboard -> "showKeyboard"
                        is FeatureListNavigationAction.System -> "showSystem"
                        is FeatureListNavigationAction.Links -> "showLinks"
                        is FeatureListNavigationAction.Bluetooth -> "showBluetooth"
                        is FeatureListNavigationAction.Beacons -> "showBeacons"
                        is FeatureListNavigationAction.PlatformSpecific -> "showPlatformSpecific"
                        is FeatureListNavigationAction.Resources -> "showResources"
                    }
                )
            }
        )
    }

    fun createInfoViewModel(parent: UIViewController): InfoViewModel {
        return InfoViewModel(
            ReviewManager.Builder(),
            ViewControllerNavigator(parent) { action ->
                when (action) {
                    is InfoNavigation.Dialog -> {
                        NavigationSpec.Present(
                            true,
                            present = {
                                val title = action.bundle?.get(DialogSpecRow.TitleRow) ?: ""
                                val message = action.bundle?.get(DialogSpecRow.MessageRow) ?: ""
                                UIAlertController.alertControllerWithTitle(title, message, UIAlertControllerStyleAlert).apply {
                                    addAction(UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault) {})
                                }
                            }
                        )
                    }
                    is InfoNavigation.Link -> NavigationSpec.Browser(
                        NSURL.URLWithString(
                            action.bundle!!.get(LinkSpecRow.LinkRow)
                        )!!,
                        NavigationSpec.Browser.Type.Normal
                    )
                    is InfoNavigation.Mail -> NavigationSpec.Email(
                        NavigationSpec.Email.EmailSettings(
                            to = action.bundle?.get(
                                MailSpecRow.ToRow
                            ) ?: emptyList(),
                            subject = action.bundle?.get(MailSpecRow.SubjectRow)
                        )
                    )
                }
            }
        )
    }

    fun createBluetoothListViewModel(parent: UIViewController, bluetooth: Bluetooth, createDeviceDetailsViewController: (Identifier, Bluetooth) -> UIViewController): BluetoothListViewModel {
        return BluetoothListViewModel(
            bluetooth,
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Push(
                    push = {
                        val bundle = action.bundle ?: return@Push UIViewController()
                        val identifier = NSUUID(uUIDString = bundle.get(DeviceDetailsSpecRow.UUIDRow))
                        createDeviceDetailsViewController(identifier, bluetooth)
                    }
                )
            }
        )
    }

    fun createBluetoothDeviceDetailsViewModel(identifier: Identifier, bluetooth: Bluetooth): BluetoothDeviceDetailViewModel {
        return BluetoothDeviceDetailViewModel(bluetooth, identifier)
    }

    fun createBeaconsListViewModel(parent: UIViewController, service: Beacons): BeaconsListViewModel {
        return BeaconsListViewModel(service)
    }

    fun createPermissionListViewModel(parent: UIViewController, createPermissionViewController: (Permission) -> UIViewController): PermissionsListViewModel {
        return PermissionsListViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Push(
                    push = {
                        val bundle = action.bundle ?: return@Push UIViewController()
                        val permission = when (bundle.get(PermissionNavigationBundleSpecRow)) {
                            PermissionView.Bluetooth -> BluetoothPermission
                            PermissionView.Calendar -> CalendarPermission()
                            PermissionView.Camera -> CameraPermission
                            PermissionView.Contacts -> ContactsPermission()
                            PermissionView.Location -> LocationPermission(
                                background = true,
                                precise = true
                            )
                            PermissionView.Microphone -> MicrophonePermission
                            PermissionView.Notifications -> NotificationsPermission(
                                NotificationOptions(
                                    UNAuthorizationOptionAlert or UNAuthorizationOptionSound
                                )
                            )
                            PermissionView.Storage -> StoragePermission()
                        }
                        createPermissionViewController(permission)
                    }
                )
            }
        )
    }

    fun createPermissionViewModel(permissions: Permissions, permission: Permission): PermissionViewModel {
        return PermissionViewModel(permissions, permission)
    }

    fun createLocationViewModel(permission: LocationPermission, repoBuilder: LocationStateRepoBuilder): LocationViewModel {
        return LocationViewModel(permission, repoBuilder)
    }

    fun createArchitectureInputViewModel(parent: UIViewController, createDetailsViewController: (InputDetails) -> UIViewController): ArchitectureInputViewModel {
        return ArchitectureInputViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Present(
                    present = {
                        createDetailsViewController(action.bundle?.get(action.type) ?: InputDetails("", 0))
                    }
                )
            }
        )
    }

    fun createArchitectureDetailsViewModel(parent: UIViewController, inputDetails: InputDetails, onDismiss: (InputDetails) -> Unit): ArchitectureDetailsViewModel {
        return ArchitectureDetailsViewModel(
            inputDetails,
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Dismiss(
                    completion = {
                        onDismiss(action.bundle?.get(action.type) ?: InputDetails("", 0))
                    }
                )
            }
        )
    }

    fun createKeyboardViewModel(focusHandler: FocusHandler): KeyboardViewModel {
        return KeyboardViewModel(KeyboardManager.Builder(), focusHandler)
    }

    fun createSystemViewModel(parent: UIViewController): SystemViewModel {
        return SystemViewModel(
            ViewControllerNavigator(parent) { action ->
                when (action) {
                    SystemNavigationActions.Network ->
                        NavigationSpec.Segue("showNetwork")
                }
            }
        )
    }

    fun createResourcesViewModel(parent: UIViewController): ResourcesListViewModel {
        return ResourcesListViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Segue(
                    when (action) {
                        is ResourcesListNavigationAction.Button -> "showButton"
                        is ResourcesListNavigationAction.Color -> "showColor"
                        is ResourcesListNavigationAction.Label -> "showLabel"
                    }
                )
            }
        )
    }

    fun createButtonViewModel(parent: UIViewController) = ButtonViewModel(
        StyledStringBuilder.Provider(),
        AlertPresenter.Builder(parent)
    )

    fun createLabelViewModel() = LabelViewModel(StyledStringBuilder.Provider())

    fun createColorViewModel(parent: UIViewController) = ColorViewModel(AlertPresenter.Builder(parent))

    fun <VM : BaseLifecycleViewModel> bind(viewModel: VM, to: UIViewController, onLifecycleChanges: onLifeCycleChanged): LifecycleManager {
        return viewModel.addLifecycleManager(to, onLifecycleChanges)
    }

    fun createLinksViewModel(
        parent: UIViewController,
        alertPresenter: AlertPresenter.Builder,
        animated: Boolean,
        completion: (() -> Unit)? = null
    ): LinksViewModel {
        return LinksViewModel(
            LinksBuilder(PlatformLinksHandler()),
            alertPresenter,
            ViewControllerNavigator(parent) { action ->
                when (action) {
                    is BrowserNavigationActions.OpenWebView -> NavigationSpec.Browser(
                        NSURL.URLWithString(action.bundle!!.get(BrowserSpecRow.UrlSpecRow))!!,
                        NavigationSpec.Browser.Type.Normal
                    )
                }
            }
        )
    }
}

fun ExampleViewModel.observeTabs(stackView: UIStackView, addOnPressed: (UIButton, () -> Unit) -> Unit): List<Disposable> {
    val selectedButtonDisposeBag = DisposeBag()
    return listOf(
        tabs.observeInitialized { tabs ->
            selectedButtonDisposeBag.dispose()
            stackView.arrangedSubviews.forEach { subView -> (subView as UIView).removeFromSuperview() }
            tabs.forEach { tab ->
                val button = UIButton()
                button.setTitle(tab.title, UIControlStateNormal)
                button.setTitleColor(UIColor.systemBlueColor, UIControlStateSelected)
                button.setTitleColor(UIColor.systemBlueColor, UIControlStateHighlighted)
                button.setTitleColor(UIColor.grayColor, UIControlStateNormal)
                this.tab.observeInitialized { selectedTab ->
                    button.setSelected(selectedTab == tab)
                }.addTo(selectedButtonDisposeBag)
                addOnPressed(button) {
                    this.tab.post(tab)
                }
                stackView.addArrangedSubview(button)
            }
        }
    )
}

fun SystemViewModel.observeModules(onModuleChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable =
    modules.observeInitialized { modules ->
        val moduleName = modules.map { it.name }
        onModuleChanged(moduleName) { onButtonTapped(modules[it]) }
    }

fun FeatureListViewModel.observeFeatures(onFeaturesChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable {
    return feature.observeInitialized { features ->
        val titles = features.map { feature -> feature.title }
        onFeaturesChanged(titles) { index -> this.onFeaturePressed(features[index]) }
    }
}

fun InfoViewModel.observeButtons(onInfoButtonsChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable {
    return buttons.observeInitialized { buttons ->
        val titles = buttons.map { button -> button.title }
        onInfoButtonsChanged(titles) { index -> this.onButtonPressed(buttons[index]) }
    }
}

fun PermissionsListViewModel.observePermissions(onPermissionsChanged: (List<PermissionView>, (Int) -> Unit) -> Unit): Disposable {
    return permissions.observeInitialized { permissions ->
        onPermissionsChanged(permissions) { index -> this.onPermissionPressed(permissions[index]) }
    }
}

fun ResourcesListViewModel.observeResources(onResourcesChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable {
    return resources.observeInitialized { features ->
        val titles = features.map { feature -> feature.title }
        onResourcesChanged(titles) { index -> this.onResourceSelected(features[index]) }
    }
}
