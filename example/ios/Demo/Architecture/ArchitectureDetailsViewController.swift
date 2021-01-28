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
    
    static func create(inputDetails: InputDetails, onDismiss: @escaping (InputDetails) -> Void) -> ArchitectureDetailsViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! ArchitectureDetailsViewController
        if #available(iOS 13.0, *) {
            vc.isModalInPresentation = true
        }
        vc.viewModel = KNArchitectureFramework().createArchitectureDetailsViewModel(parent: vc, inputDetails: inputDetails) { inputDetails in
            onDismiss(inputDetails)
        }
        return vc
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
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
            
            guard let viewModel = self?.viewModel else {
                return []
            }
            
            return [
                viewModel.name.observe { name in
                    self?.nameLabel.text = name as String?
                },
                viewModel.number.observe { number in
                    self?.numberLabel.text = number as String?
                }
            ]
        }
    }
    
    @objc @IBAction func onInversePressed(sender: Any?) {
        viewModel.onInversePressed()
    }
    
    @objc @IBAction func onCloseButtonPressed(sender: Any?) {
        viewModel.onClosePressed()
    }
    
}
