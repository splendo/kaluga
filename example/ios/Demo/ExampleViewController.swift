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
    
    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let featuresList = "FeaturesList"
        static let infoView = "InfoViewController"
    }
    
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var bottomView: UIStackView!
    
    lazy var featuresListController = Const.storyboard.instantiateViewController(withIdentifier: Const.featuresList) as! FeaturesListViewController
    lazy var infoViewController = Const.storyboard.instantiateViewController(withIdentifier: Const.infoView) as! InfoViewController
    
    lazy var viewModel: SharedExampleViewModel = KNArchitectureFramework().createExampleViewModel(parent: self,
                                                                                                  containerView: containerView,
                                                                                                  featuresList: { self.featuresListController },
                                                                                                  info:  { self.infoViewController })
    
    
    let disposeBag = ArchitectureDisposeBag()
    
    deinit {
        viewModel.clear()
    }
    
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
