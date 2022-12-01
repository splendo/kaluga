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

class LoadingViewController: UIViewController {

    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "LoadingView"
    }

    static func instantiate() -> LoadingViewController {
        Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! LoadingViewController
    }

    @IBOutlet private var systemButton: UIButton!
    @IBOutlet private var customButton: UIButton!

    private lazy var viewModel = HudViewModel(builder: HUD.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_hud".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { return [] }
        ButtonStyleKt.bindButton(systemButton, button: viewModel.showSystemButton)
        ButtonStyleKt.bindButton(customButton, button: viewModel.showCustomButton)
    }
}
