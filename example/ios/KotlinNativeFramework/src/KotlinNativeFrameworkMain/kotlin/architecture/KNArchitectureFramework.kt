package architecture

import com.splendo.kaluga.architecture.observable.Observable
import com.splendo.kaluga.architecture.observable.Subject
import com.splendo.kaluga.architecture.observable.DisposeBag
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
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