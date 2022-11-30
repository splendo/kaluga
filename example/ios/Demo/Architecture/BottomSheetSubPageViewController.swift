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

class BottomSheetSubPageViewController: UIViewController, UISheetPresentationControllerDelegate {

    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "BottomSheetSubPage"
    }

    static func create() -> BottomSheetSubPageViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! BottomSheetSubPageViewController
        let navigator = BottomSheetSubPageNavigatorKt.BottomSheetSubPageViewControllerNavigator(parent: vc, onClose: {
            NavigationSpec.Dismiss(toDismiss: { viewController in
                viewController.navigationController!
            }, animated: true)
        }, onBack: {
            NavigationSpec.Pop(to: nil, animated: true)
        })
        vc.viewModel = BottomSheetSubPageViewModel(navigator: navigator)
        return vc
    }

    var viewModel: BottomSheetSubPageViewModel!
    private var lifecycleManager: LifecycleManager!

    @IBOutlet weak var label: UILabel!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.hidesBackButton = true
        let newBackButton = UIBarButtonItem(title: "Back", style: .plain, target: self, action: #selector(didDismiss))
        self.navigationItem.leftBarButtonItem = newBackButton
        let closeButton = UIBarButtonItem(title: "Close", style: .plain, target: self, action: #selector(didClose))
        self.navigationItem.rightBarButtonItem = closeButton

        lifecycleManager = viewModel.addLifecycleManager(parent: self) {
            return []
        }

        label.text = viewModel.text
    }

    @objc func didDismiss() {
        viewModel.onBackPressed()
    }

    @objc func didClose() {
        viewModel.onClosePressed()
    }

}
