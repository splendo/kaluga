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

import UIKit
import KalugaExampleShared

class ExampleViewController : UIViewController {
    
    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let featuresList = "FeaturesList"
        static let infoView = "InfoViewController"
    }
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var bottomView: UIStackView!
    
    lazy var featuresListController = Const.storyboard.instantiateViewController(withIdentifier: Const.featuresList) as! FeaturesListViewController
    lazy var infoViewController = Const.storyboard.instantiateViewController(withIdentifier: Const.infoView) as! InfoViewController

    private lazy var navigator: ViewControllerNavigator<ExampleTabNavigation> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Nested(type: NavigationSpec.NestedTypeReplace(tag: 1), containerView: self.containerView) {
            switch action {
            case is ExampleTabNavigation.FeatureList: return self.featuresListController
            case is ExampleTabNavigation.Info: return self.infoViewController
            default: return UIViewController()
            }
        }
    }
    lazy var viewModel: ExampleViewModel = ExampleViewModel(navigator: navigator)
    var lifecycleManager: LifecycleManager!
    let selectedButtonDisposeBag = DisposeBag()

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "app_name".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel, let bottomView = self?.bottomView else { return [] }

            return [
                viewModel.tabs.observeInitialized { tabs in
                    guard let disposeBag = self?.selectedButtonDisposeBag else { return }
                    disposeBag.dispose()
                    let tabs = tabs ?? []
                    bottomView.arrangedSubviews.forEach { $0.removeFromSuperview() }
                    for tab in tabs {
                        guard let tab = tab as? ExampleViewModel.Tab else { return }
                        let button = UIButton()
                        button.setTitle(tab.title, for: .normal)
                        button.setTitleColor(UIColor.systemBlue, for: .selected)
                        button.setTitleColor(UIColor.systemBlue, for: .highlighted)
                        button.setTitleColor(UIColor.gray, for: .normal)

                        viewModel.tab.observeInitialized { selectedTab in
                            button.isSelected = selectedTab == tab
                        }.addTo(disposeBag: disposeBag)
                        button.addAction {
                            viewModel.tab.post(newValue: tab)
                        }
                        bottomView.addArrangedSubview(button)
                    }

                }
            ]
        }
    }
    
}
