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
import Foundation
import KalugaExampleShared

class PermissionViewController: UIViewController {
    
    private struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let permissionVc = "Permission"
    }
    
    static func create(permission: Permission) -> PermissionViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.permissionVc) as! PermissionViewController
        vc.viewModel = PermissionViewModel(permission: permission)
        return vc
    }
    
    @IBOutlet weak var permissionStateLabel: UILabel!
    @IBOutlet weak var requestPermissionButton: UIButton!
    
    var viewModel: PermissionViewModel!
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        requestPermissionButton.setTitle(NSLocalizedString("permission_request", comment: ""), for: .normal)

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in

            guard let viewModel = self?.viewModel else {
                return []
            }

            return [
                viewModel.permissionStateMessage.observe { message in
                    self?.permissionStateLabel.text = NSLocalizedString(message as? String ?? "", comment: "")

                },

                viewModel.requestMessage.observe { optionalMessage in
                    guard let message = optionalMessage as? String else {
                        return
                    }

                    let alert = UIAlertController(title: NSLocalizedString("permission_request", comment: ""), message: message, preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .default, handler: nil))
                    self?.present(alert, animated: true, completion: nil)

                },

                viewModel.showPermissionButton.observe { show in
                    self?.requestPermissionButton.isHidden = !(show as? Bool ?? false)
                    }
            ]
        }
    }


    @IBAction func requestPermission(sender: Any?) {
        viewModel.requestPermission()
    }

}
