/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import KalugaExampleShared

class NetworkViewController: UIViewController {

    @IBOutlet weak var networkStateText: UILabel!
    private var lifecycleManager: LifecycleManager!
    
    private lazy var viewModel = NetworkViewModel(networkStateRepoBuilder: NetworkStateRepoBuilder())
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "network_feature".localized()
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            
            return [
                viewModel.networkState.observe { next in
                    self?.networkStateText.text = next as? String
                }
            ]
        }
    }
}
