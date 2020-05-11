package architecture

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.Subject
import com.splendo.kaluga.architecture.observable.DisposeBag
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.shared.viewmodel.info.*
import platform.Foundation.NSURL
import platform.UIKit.*

class KNArchitectureFramework {

    fun createExampleViewModel(
        parent: UIViewController,
        containerView: UIView,
        featuresList: () -> UIViewController,
        info: () -> UIViewController): ExampleViewModel {
        return ExampleViewModel(Navigator(parent) { action ->
            NavigationSpec.Nested(
                NavigationSpec.Nested.Type.Replace,
                containerView,
                when (action) {
                    is ExampleTabNavigation.FeatureList -> featuresList
                    is ExampleTabNavigation.Info -> info
                })
        })
    }

    fun createFeatureListViewModel(parent: UIViewController): FeatureListViewModel {
        return FeatureListViewModel(
            Navigator(parent) { action ->
                NavigationSpec.Segue(
                    parent,
                    when (action) {
                        is FeatureListNavigationAction.Location -> "showLocation"
                        is FeatureListNavigationAction.Permissions -> "showPermissions"
                        is FeatureListNavigationAction.Alerts -> "showAlerts"
                        is FeatureListNavigationAction.LoadingIndicator -> "showHUD"
                        is FeatureListNavigationAction.Architecture -> "showArchitecture"
                    })
            })
    }

    fun createInfoViewModel(parent: UIViewController): InfoViewModel {
        return InfoViewModel(
            Navigator(parent) { action ->
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
                        NSURL.URLWithString(action.bundle!!.get(
                        LinkSpecRow.LinkRow))!!)
                    is InfoNavigation.Mail -> NavigationSpec.Email(NavigationSpec.Email.EmailSettings(to = action.bundle?.get(
                        MailSpecRow.ToRow) ?: emptyList(), subject = action.bundle?.get(MailSpecRow.SubjectRow)))
                }
            })
    }

}

fun ExampleViewModel.observeTabs(stackView: UIStackView, disposeBag: DisposeBag, addOnPressed: (UIButton, () -> Unit) -> Unit) {
    val selectedButtonDisposeBag = DisposeBag()
    tabs.observe { tabs ->
        selectedButtonDisposeBag.dispose()
        stackView.arrangedSubviews.forEach { subView -> stackView.removeArrangedSubview(subView as UIView) }
        tabs.forEach { tab ->
            val button = UIButton()
            button.setTitle(tab.title, UIControlStateNormal)
            button.setTitleColor(UIColor.systemBlueColor, UIControlStateSelected)
            button.setTitleColor(UIColor.systemBlueColor, UIControlStateHighlighted)
            button.setTitleColor(UIColor.grayColor, UIControlStateNormal)
            this.tab.observe { selectedTab ->
                button.setSelected(selectedTab == tab)
            }.putIn(selectedButtonDisposeBag)
            addOnPressed(button) {
                this.tab.post(tab)
            }
            stackView.addArrangedSubview(button)
        }
    }.putIn(disposeBag)
    disposeBag.add(selectedButtonDisposeBag)
}

fun FeatureListViewModel.observeFeatures(disposeBag: DisposeBag, onFeaturesChanged: (List<String>, (Int) -> Unit) -> Unit) {
    feature.observe { features ->
        val titles = features.map { feature -> feature.title }
        onFeaturesChanged(titles) { index -> this.onFeaturePressed(features[index]) }
    }.putIn(disposeBag)
}

fun InfoViewModel.observeButtons(disposeBag: DisposeBag, onInfoButtonsChanged: (List<String>, (Int) -> Unit) -> Unit) {
    buttons.observe { buttons ->
        val titles = buttons.map { button -> button.title }
        onInfoButtonsChanged(titles) { index -> this.onButtonPressed(buttons[index]) }
    }.putIn(disposeBag)
}