/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

import Foundation
import UIKit
import KotlinNativeFramework

class LinksViewController : UIViewController {
    
    @IBOutlet weak var browserButton: UIButton!
    @IBOutlet weak var instructionsText: UILabel!
    
    private let knArchitectureFramework = KNArchitectureFramework()
    private lazy var viewModel: LinksViewModel = {
        return knArchitectureFramework.createLinksViewModel(
            parent: self,
            alertPresenter: AlertPresenter.Builder(viewController: self),
            animated: true,
            completion: nil
        )
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
