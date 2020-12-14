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

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.Subject
import com.splendo.kaluga.architecture.observable.Disposable
import com.splendo.kaluga.architecture.observable.DisposeBag
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.ViewControllerNavigator
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.architecture.viewmodel.LifecycleManager
import com.splendo.kaluga.architecture.viewmodel.addLifecycleManager
import com.splendo.kaluga.architecture.viewmodel.onLifeCycleChanged
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.DetailsSpecRow
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.keyboard.KeyboardViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.*
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionNavigationBundleSpecRow
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.keyboard.KeyboardManager
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.notifications.*
import platform.Foundation.NSURL
import platform.UIKit.*
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound

class KNArchitectureFramework {

    fun createExampleViewModel(
        parent: UIViewController,
        containerView: UIView,
        featuresList: () -> UIViewController,
        info: () -> UIViewController): ExampleViewModel {
        return ExampleViewModel(ViewControllerNavigator(parent) { action ->
            NavigationSpec.Nested(
                NavigationSpec.Nested.Type.Replace(1),
                containerView,
                when (action) {
                    is ExampleTabNavigation.FeatureList -> featuresList
                    is ExampleTabNavigation.Info -> info
                })
        })
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
                    })
            })
    }

    fun createInfoViewModel(parent: UIViewController): InfoViewModel {
        return InfoViewModel(
            ViewControllerNavigator(parent) { action ->
                when (action) {
                    is InfoNavigation.Dialog -> {
                        NavigationSpec.Present(true, present = {
                            val title = action.bundle?.get(DialogSpecRow.TitleRow) ?: ""
                            val message = action.bundle?.get(DialogSpecRow.MessageRow) ?: ""
                            UIAlertController.alertControllerWithTitle(title, message, UIAlertControllerStyleAlert).apply {
                                addAction(UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault) {})
                            }
                        })
                    }
                    is InfoNavigation.Link -> NavigationSpec.Browser(
                        NSURL.URLWithString(
                            action.bundle!!.get(LinkSpecRow.LinkRow))!!,
                            NavigationSpec.Browser.Type.Normal
                        )
                    is InfoNavigation.Mail -> NavigationSpec.Email(NavigationSpec.Email.EmailSettings(to = action.bundle?.get(
                        MailSpecRow.ToRow) ?: emptyList(), subject = action.bundle?.get(MailSpecRow.SubjectRow)))
                }
            })
    }

    fun createPermissionListViewModel(parent: UIViewController, createPermissionViewController: (Permission) -> UIViewController): PermissionsListViewModel {
        return PermissionsListViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Push(push = {
                    val bundle = action.bundle ?: return@Push UIViewController()
                    val permission = when (bundle.get(PermissionNavigationBundleSpecRow)) {
                        PermissionView.Bluetooth -> Permission.Bluetooth
                        PermissionView.Calendar -> Permission.Calendar()
                        PermissionView.Camera -> Permission.Camera
                        PermissionView.Contacts -> Permission.Contacts()
                        PermissionView.Location -> Permission.Location(
                            background = true,
                            precise = true
                        )
                        PermissionView.Microphone -> Permission.Microphone
                        PermissionView.Notifications -> Permission.Notifications(NotificationOptions(
                            UNAuthorizationOptionAlert or UNAuthorizationOptionSound))
                        PermissionView.Storage -> Permission.Storage()
                    }
                    createPermissionViewController(permission)
                })
            }
        )
    }

    fun createPermissionViewModel(permissions: Permissions, permission: Permission): PermissionViewModel {
        return PermissionViewModel(permissions, permission)
    }

    fun createLocationViewModel(permission: Permission.Location, repoBuilder: LocationStateRepoBuilder): LocationViewModel {
        return LocationViewModel(permission, repoBuilder)
    }

    fun createArchitectureInputViewModel(parent: UIViewController, createDetailsViewController: (String, Int) -> UIViewController): ArchitectureInputViewModel {
        return ArchitectureInputViewModel(
            ViewControllerNavigator(parent) { action ->
                NavigationSpec.Present(present = {
                    val name = action.bundle?.get(DetailsSpecRow.NameRow) ?: ""
                    val number = action.bundle?.get(DetailsSpecRow.NumberRow) ?: 0
                    createDetailsViewController(name, number)
                })
            }
        )
    }

    fun createArchitectureDetailsViewModel(parent: UIViewController, name: String, number: Int, onDismiss: (String, Int) -> Unit): ArchitectureDetailsViewModel {
        return ArchitectureDetailsViewModel(name, number, ViewControllerNavigator(parent) { action ->
            NavigationSpec.Dismiss(completion = {
                val finalName = action.bundle?.get(DetailsSpecRow.NameRow) ?: ""
                val finalNumber = action.bundle?.get(DetailsSpecRow.NumberRow) ?: 0
                onDismiss(finalName, finalNumber)
            })
        })
    }

    fun createKeyboardViewModel(textField: UITextField): KeyboardViewModel {
        return KeyboardViewModel(KeyboardManager.Builder(), textField)
    }

    fun <VM: BaseViewModel> bind(viewModel: VM, to: UIViewController, onLifecycleChanges: onLifeCycleChanged): LifecycleManager {
        return viewModel.addLifecycleManager(to, onLifecycleChanges)
    }

}

fun ExampleViewModel.observeTabs(stackView: UIStackView, addOnPressed: (UIButton, () -> Unit) -> Unit): List<Disposable> {
    val selectedButtonDisposeBag = DisposeBag()
    return listOf(tabs.observe { tabs ->
        selectedButtonDisposeBag.dispose()
        stackView.arrangedSubviews.forEach { subView -> (subView as UIView).removeFromSuperview() }
        tabs.forEach { tab ->
            val button = UIButton()
            button.setTitle(tab.title, UIControlStateNormal)
            button.setTitleColor(UIColor.systemBlueColor, UIControlStateSelected)
            button.setTitleColor(UIColor.systemBlueColor, UIControlStateHighlighted)
            button.setTitleColor(UIColor.grayColor, UIControlStateNormal)
            this.tab.observe { selectedTab ->
                button.setSelected(selectedTab == tab)
            }.addTo(selectedButtonDisposeBag)
            addOnPressed(button) {
                this.tab.post(tab)
            }
            stackView.addArrangedSubview(button)
        }
    })
}

fun FeatureListViewModel.observeFeatures(onFeaturesChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable {
    return feature.observe { features ->
        val titles = features.map { feature -> feature.title }
        onFeaturesChanged(titles) { index -> this.onFeaturePressed(features[index]) }
    }
}

fun InfoViewModel.observeButtons(onInfoButtonsChanged: (List<String>, (Int) -> Unit) -> Unit): Disposable {
    return buttons.observe { buttons ->
        val titles = buttons.map { button -> button.title }
        onInfoButtonsChanged(titles) { index -> this.onButtonPressed(buttons[index]) }
    }
}

fun PermissionsListViewModel.observePermissions(onPermissionsChanged: (List<PermissionView>, (Int) -> Unit) -> Unit): Disposable {
    return permissions.observe { permissions ->
        onPermissionsChanged(permissions) { index -> this.onPermissionPressed(permissions[index]) }
    }
}