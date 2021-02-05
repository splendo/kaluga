//
//  NetworkViewController.swift
//  Demo
//
//  Created by Corrado Quattrocchi on 05/02/2021.
//  Copyright © 2021 Splendo. All rights reserved.
//

import Foundation
import UIKit
import KotlinNativeFramework

class NetworkViewController : UIViewController {
    
    private let knArchitectureFramework = KNArchitectureFramework()
    @IBOutlet weak var networkStateText: UILabel!
    private var lifecycleManager: LifecycleManager!
    
    private lazy var viewModel: NetworkViewModel = NetworkViewModel(networkStateRepoBuilder: NetworkStateRepoBuilder())
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = knArchitectureFramework.bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            
            return [
                viewModel.networkState.observe { value in
                    self?.networkStateText.text = value as? String
                }
            ]
        }
    }
    
}
