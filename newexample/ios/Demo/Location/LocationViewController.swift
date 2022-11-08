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

import UIKit
import CoreLocation
import KotlinNativeFramework

class LocationViewController: UIViewController {

    struct Const {
        static let permission = LocationPermission(background: false, precise: true)
    }
    
    //MARK: Properties
    
    @IBOutlet weak var label: UILabel!
    
    lazy var viewModel = KNArchitectureFramework().createLocationViewModel(permission: Const.permission, repoBuilder: KNLocationFramework().getPermissionRepoBuilder())
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self, onLifecycle: { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [viewModel.location.observe(onNext: { (location) in
                self?.label.text = location as? String ?? ""
                })]
        })
    }
}
