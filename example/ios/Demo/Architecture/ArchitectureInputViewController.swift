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
    
    lazy var viewModel = KNArchitectureFramework().createArchitectureInputViewModel(parent: self) { [weak self] name, number in
        return ArchitectureDetailsViewController.create(name: name, number: number.int32Value) { [weak self] resultName, resultNumber in
            self?.onDetailsDismissed(resultName: resultName, resultNumber: resultNumber)
        }
    }
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] disposeBag in
        
            guard let viewModel = self?.viewModel else {
                return
            }
            
            viewModel.nameHeader.observe { header in
                self?.nameLabel.text = header as String?
            }.addTo(disposeBag: disposeBag)
            
            viewModel.nameInput.observe { name in
                self?.nameInput.text = name as String?
            }.addTo(disposeBag: disposeBag)
            
            viewModel.isNameValid.observe { isValid in
                self?.nameError.isHidden = isValid?.boolValue ?? false
            }.addTo(disposeBag: disposeBag)
            
            viewModel.numberHeader.observe { header in
                self?.numberLabel.text = header as String?
            }.addTo(disposeBag: disposeBag)
            
            viewModel.numberInput.observe { number in
                self?.numberInput.text = number as String?
            }.addTo(disposeBag: disposeBag)
            
            viewModel.isNumberValid.observe { isValid in
                self?.numberError.isHidden = isValid?.boolValue ?? false
            }.addTo(disposeBag: disposeBag)
        }
    }
    
    @objc @IBAction func onShowDetailsPressed(sender: Any?) {
        viewModel.onShowDetailsPressed()
    }
    
    private func onDetailsDismissed(resultName: String, resultNumber: Int32) {
        viewModel.nameInput.post(newValue: NSString(string: resultName))
        viewModel.numberInput.post(newValue: NSString(string: "\(resultNumber)"))
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
