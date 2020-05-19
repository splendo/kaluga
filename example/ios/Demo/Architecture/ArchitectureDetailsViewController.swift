//
//  ArchitectureDetailsViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 12/05/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class ArchitectureDetailsViewController: UIViewController {
    
    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "ArchitectureDetails"
    }
    
    static func create(name: String, number: Int32, onDismiss: @escaping (String, Int32) -> Void) -> ArchitectureDetailsViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! ArchitectureDetailsViewController
        if #available(iOS 13.0, *) {
            vc.isModalInPresentation = true
        }
        vc.viewModel = KNArchitectureFramework().createArchitectureDetailsViewModel(parent: vc, name: name, number: number) { resultName, resultNumber in
            onDismiss(resultName, resultNumber.int32Value)
        }
        return vc
    }
    
    var viewModel: SharedArchitectureDetailsViewModel!
    private let disposeBag = ArchitectureDisposeBag()
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var inverseButton: UIButton!
    @IBOutlet weak var closeButton: UIButton!
    
    deinit {
        viewModel.clear()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        viewModel.didResume()
        
        viewModel.name.observe { name in
            self.nameLabel.text = name as? String
        }.putIn(disposeBag: disposeBag)
        
        viewModel.number.observe { number in
            self.numberLabel.text = number as? String
        }.putIn(disposeBag: disposeBag)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        
        viewModel.didPause()
        disposeBag.dispose()
    }
    
    @objc @IBAction func onInversePressed(sender: Any?) {
        viewModel.onInversePressed()
    }
    
    @objc @IBAction func onCloseButtonPressed(sender: Any?) {
        viewModel.onClosePressed()
    }
    
}
