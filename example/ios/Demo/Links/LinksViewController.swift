//
//  LinksViewController.swift
//  Demo
//
//  Created by Corrado Quattrocchi on 03/02/2021.
//  Copyright © 2021 Splendo. All rights reserved.
//

import Foundation
import UIKit
import KotlinNativeFramework

class LinksViewController : UIViewController {
    
    @IBOutlet weak var browserButton: UIButton!
    @IBOutlet weak var instructionsText: UILabel!
    
    private let knArchitectureFramework = KNArchitectureFramework()
    private lazy var viewModel: LinksViewModel = {
        return knArchitectureFramework.createLinksViewModel(parent: self, animated: true, completion: nil)
    }()
    private var lifecycleManager: LifecycleManager!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = knArchitectureFramework.bind(viewModel: viewModel, to: self) {
            [
                self.viewModel.browserButtonText.observe { buttonText in
                    self.browserButton.setTitle(buttonText as String?, for: .normal)
                },
                self.viewModel.linksInstructions.observe { text in
                    self.instructionsText.text = text as String?
                }
            ]
        }
    }
    
    @IBAction func onBrowserButtonTapped(_ sender: UIButton) {
        self.viewModel.openWebPage()
    }
    
    func handleIncomingLink(url: String) {
        self.viewModel.handleIncomingLink(url: url)
    }
}
