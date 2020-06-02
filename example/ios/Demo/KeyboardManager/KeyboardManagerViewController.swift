//
//  KeyboardManagerViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 04/12/2019.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class KeyboardManagerViewController : UIViewController {
    
    @IBOutlet private var editField: UITextField!
    
    lazy var viewModel = KNArchitectureFramework().createKeyboardViewModel(textField: self.editField)
    private var lifecycleManager: ArchitectureLifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { _ in })
    }
    
    @IBAction
    func showButtonPressed() {
        viewModel.onShowPressed()
    }
    
    @IBAction
    func hideButtonPressed() {
        viewModel.onHidePressed()
    }
}
