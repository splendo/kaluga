//
//  ArchitectureInputViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 12/05/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class ArchitectureInputViewController: UIViewController  {
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var nameInput: UITextField!
    @IBOutlet weak var nameError: UIImageView!
    
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var numberInput: UITextField!
    @IBOutlet weak var numberError: UIImageView!
    
    @IBOutlet weak var detailsButton: UIButton!
    
    lazy var viewModel = KNArchitectureFramework().createArchitectureInputViewModel(parent: self) { [weak self] inputDetails in
        return ArchitectureDetailsViewController.create(inputDetails: inputDetails) { [weak self] inputDetails in
            self?.onDetailsDismissed(inputDetails: inputDetails)
        }
    }
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
        
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
