/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import KotlinNativeFramework

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
    
    lazy var viewModel: ExampleViewModel = KNArchitectureFramework().createExampleViewModel(parent: self,
                                                                                                  containerView: containerView,
                                                                                                  featuresList: { self.featuresListController },
                                                                                                  info:  { self.infoViewController })
    
    var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel, let bottomView = self?.bottomView else { return [] }
            return viewModel.observeTabs(stackView: bottomView) { (button: UIButton, action: @escaping () -> KotlinUnit) in
                button.addAction { let _ = action() }
            }
        }
    }
    
}
