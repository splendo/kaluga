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

class AlertsViewController: UIViewController {

    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "AlertsViewController"
    }

    static func instantiate() -> AlertsViewController {
        Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! AlertsViewController
    }

    @IBOutlet var showAlertButton: UIButton!
    @IBOutlet var showAndDismissButton: UIButton!
    @IBOutlet var showAlertWithInputButton: UIButton!
    @IBOutlet var showAlertWithListButton: UIButton!

    private lazy var viewModel = AlertViewModel(builder: AlertPresenter.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { return [] }
        ButtonStyleKt.bindButton(showAlertButton, button: viewModel.showAlertButton)
        ButtonStyleKt.bindButton(showAndDismissButton, button: viewModel.showAndDismissAfter3SecondsButton)
        ButtonStyleKt.bindButton(showAlertWithInputButton, button: viewModel.showAlertWithInputButton)
        ButtonStyleKt.bindButton(showAlertWithListButton, button: viewModel.showAlertWithListButton)
    }
}
