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

class ArchitectureDetailsViewController: UIViewController {
    
    static func create(inputDetails: InputDetails, onDismiss: @escaping (InputDetails) -> Void) -> ArchitectureDetailsViewController {
        let viewController = MainStoryboard.instantiateArchitectureDetailsViewController()
        if #available(iOS 13.0, *) {
            viewController.isModalInPresentation = true
        }
        let navigator = ArchitectureDetailsNavigatorKt.ArchitectureDetailsViewControllerNavigator(
            parent: viewController,
            onFinishWithDetails: { details in
                NavigationSpec.Pop(to: nil, animated: true) {
                    onDismiss(details)
                }
            },
            onClose: {
                NavigationSpec.Pop(to: nil, animated: true)
            }
        )
        viewController.viewModel = ArchitectureDetailsViewModel(initialDetail: inputDetails, navigator: navigator)
        return viewController
    }

    var viewModel: ArchitectureDetailsViewModel!
    private var lifecycleManager: LifecycleManager!
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var inverseButton: UIButton!
    @IBOutlet weak var closeButton: UIButton!
    
    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.hidesBackButton = true
        let newBackButton = UIBarButtonItem(title: "Back", style: .plain, target: self, action: #selector(didDismiss))
        self.navigationItem.leftBarButtonItem = newBackButton

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in

            guard let viewModel = self?.viewModel else {
                return []
            }

            return [
                viewModel.name.observe { name in
                    self?.nameLabel.text = name as? String ?? ""
                },
                viewModel.number.observe { number in
                    self?.numberLabel.text = number as? String ?? ""
                }
            ]
        }

        ButtonStyleKt.bindButton(inverseButton, button: viewModel.inverseButton)
        ButtonStyleKt.bindButton(closeButton, button: viewModel.finishButton)
    }

    @objc func didDismiss() {
        viewModel.onBackPressed()
    }
}
