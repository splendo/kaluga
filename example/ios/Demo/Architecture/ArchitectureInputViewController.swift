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

class ArchitectureViewController: UIViewController  {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var nameInput: UITextField!
    @IBOutlet weak var nameError: UIImageView!
    
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var numberInput: UITextField!
    @IBOutlet weak var numberError: UIImageView!
    
    @IBOutlet weak var detailsButton: UIButton!
    @IBOutlet weak var bottomSheetButton: UIButton!

    lazy var navigator = ArchitectureNavigatorKt.ArchitectureViewControllerNavigator(
        parent: self,
        onDetails: { inputDetails in
            NavigationSpec.Push(
                animated: true
            ) {
                ArchitectureDetailsViewController.create(inputDetails: inputDetails) { [weak self] resultDetails in
                    self?.onDetailsDismissed(inputDetails: resultDetails)
                }
            }
        },
        onBottomSheet: {
            NavigationSpec.Present(
                animated: true,
                presentationStyle: Int64(UIModalPresentationStyle.automatic.rawValue),
                transitionStyle: Int64(UIModalTransitionStyle.coverVertical.rawValue)
            ) {
                let vc = BottomSheetViewController.create()
                let nav = UINavigationController(rootViewController: vc)
                if let sheet = nav.sheetPresentationController {
                    sheet.detents = [.medium()]
                }
                nav.isModalInPresentation = true
                return nav
            }
        }
    )
    lazy var viewModel = ArchitectureViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in

            guard let viewModel = self?.viewModel else {
                return []
            }

            return [
                viewModel.nameInput.observeInitialized { name in
                    self?.nameInput.text = name as String?
                },
                viewModel.isNameValid.observe { isValid in
                    self?.nameError.isHidden = isValid?.boolValue ?? false
                },
                viewModel.numberInput.observeInitialized { number in
                    self?.numberInput.text = number as String?
                },
                viewModel.isNumberValid.observe { isValid in
                    self?.numberError.isHidden = isValid?.boolValue ?? false
                }
            ]
        }
        nameLabel.text = viewModel.namePlaceholder
        numberLabel.text = viewModel.numberPlaceholder
        ButtonStyleKt.bindButton(detailsButton, button: viewModel.showDetailsButton)
        ButtonStyleKt.bindButton(bottomSheetButton, button: viewModel.showBottomSheetButton)
    }

    private func onDetailsDismissed(inputDetails: InputDetails) {
        viewModel.nameInput.post(newValue: NSString(string: inputDetails.name))
        viewModel.numberInput.post(newValue: NSString(string: "\(inputDetails.number)"))
    }

}

extension ArchitectureViewController : UITextFieldDelegate {

    func textFieldDidEndEditing(_ textField: UITextField) {
        postInput(text: textField.text ?? "", fromTextField: textField)
    }

    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        let resultingString = ((textField.text ?? "") as NSString).replacingCharacters(in: range, with: string)
        postInput(text: resultingString, fromTextField: textField)
        return false
    }

    private func postInput(text: String, fromTextField textField: UITextField) {
        if (textField == nameInput) {
            viewModel.nameInput.post(newValue: NSString(string: text))
        } else if (textField == numberInput) {
            viewModel.numberInput.post(newValue: NSString(string: text))
        }
    }
}
