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

class ArchitectureInputViewController: UIViewController  {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var nameInput: UITextField!
    @IBOutlet weak var nameError: UIImageView!
    
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var numberInput: UITextField!
    @IBOutlet weak var numberError: UIImageView!
    
    @IBOutlet weak var detailsButton: UIButton!

    lazy var navigator: ViewControllerNavigator<InputNavigation> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Present(animated: true, presentationStyle: Int64(UIModalPresentationStyle.automatic.rawValue), transitionStyle: Int64(UIModalTransitionStyle.coverVertical.rawValue)) {
            ArchitectureDetailsViewController.create(inputDetails: action.value!) { [weak self] inputDetails in
                self?.onDetailsDismissed(inputDetails: inputDetails)
            }
        }
    }
    lazy var viewModel = ArchitectureInputViewModel(navigator: navigator)
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
                viewModel.nameHeader.observeInitialized { header in
                    self?.nameLabel.text = header as String?
                },
                viewModel.nameInput.observeInitialized { name in
                    self?.nameInput.text = name as String?
                },
                viewModel.isNameValid.observe { isValid in
                    self?.nameError.isHidden = isValid?.boolValue ?? false
                },
                viewModel.numberHeader.observeInitialized { header in
                    self?.numberLabel.text = header as String?
                },
                viewModel.numberInput.observeInitialized { number in
                    self?.numberInput.text = number as String?
                },
                viewModel.isNumberValid.observe { isValid in
                    self?.numberError.isHidden = isValid?.boolValue ?? false
                }
            ]
        }
    }

    @objc @IBAction func onShowDetailsPressed(sender: Any?) {
        viewModel.onShowDetailsPressed()
    }

    private func onDetailsDismissed(inputDetails: InputDetails) {
        viewModel.nameInput.post(newValue: NSString(string: inputDetails.name))
        viewModel.numberInput.post(newValue: NSString(string: "\(inputDetails.number)"))
    }

}

extension ArchitectureInputViewController : UITextFieldDelegate {

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
