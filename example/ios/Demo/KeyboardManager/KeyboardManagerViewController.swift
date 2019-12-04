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
    private var exampleKeyboardManager: SharedExampleKeyboardManager!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        exampleKeyboardManager = KotlinNativeFramework().keyboardManager(textField: editField)
    }
    
    @IBAction
    func showButtonPressed() {
        exampleKeyboardManager.show()
    }
    
    @IBAction
    func hideButtonPressed() {
        exampleKeyboardManager.hide()
    }
}
