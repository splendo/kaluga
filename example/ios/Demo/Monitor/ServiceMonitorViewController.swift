//
//  Copyright 2021 Splendo Consulting B.V. The Netherlands
// 
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

import Foundation
import UIKit
import KotlinNativeFramework

class ServiceMonitorViewController : UIViewController {
    
    @IBOutlet weak var bltMonitorTitle: UILabel!
    @IBOutlet weak var locationMonitorTitle: UILabel!
    
    @IBOutlet weak var bltMonitorText: UILabel!
    @IBOutlet weak var locationMonitorText: UILabel!
    
    private let knArchitectureFramework = KNArchitectureFramework()
    private lazy var viewModel: ServiceMonitorViewModel = {
        return knArchitectureFramework.createMonitorViewModel()
    }()
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        self.bltMonitorTitle.text = self.viewModel.bluetoothServiceTitleText as String
//        self.locationMonitorTitle.text = self.viewModel.locationServiceTitleText as String
        lifecycleManager = knArchitectureFramework.bind(viewModel: viewModel, to: self) {
            [
                self.viewModel.bluetoothServiceStatusText.observe { buttonText in
                    self.bltMonitorText.text = buttonText as String?
                },
                self.viewModel.locationServiceStatusText.observe { text in
                    self.locationMonitorText.text = text as String?
                }
            ]
        }
    }
}
