//
//  ExampleViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 11/05/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class ExampleViewController : UIViewController {
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var bottomView: UIStackView!
    
    let disposeBag = ArchitectureDisposeBag()
    
    lazy var viewModel: SharedExampleViewModel = KNArchitectureFramework().createExampleViewModel(parent: self,
                                                                                                  containerView: containerView,
                                                                                                  featuresList: { UIViewController() },
                                                                                                  info:  { UIViewController() })
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel.observeTabs(stackView: bottomView, disposeBag: disposeBag) { (button: UIButton, action: @escaping () -> KotlinUnit) in
            button.addAction { let _ = action() }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.didResume()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        viewModel.didPause()
        disposeBag.dispose()
    }
    
}
