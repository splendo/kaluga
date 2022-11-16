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

class SystemViewController : UITableViewController {

    private lazy var navigator: ViewControllerNavigator<SystemNavigationActions> = ViewControllerNavigator(parentVC: self) { action in
        switch action {
        case is SystemNavigationActions.Network: return NavigationSpec.Segue(identifier: "showNetwork")
        default: return NavigationSpec.Segue(identifier: "")
        }
    }
    private lazy var viewModel: SystemViewModel = SystemViewModel(navigator: navigator)

    private var modules = [String]()
    private var onModuleTapped: ((Int) -> Void)? = nil
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.modules.observeInitialized { next in
                    let modules = next?.compactMap { $0 as? SystemFeatures } ?? []
                    self?.modules = modules.map{ $0.name }
                    self?.onModuleTapped = { (index: Int) in viewModel.onButtonTapped(systemFeatures: modules[index]) }
                    self?.tableView.reloadData()
                }
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return modules.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SystemListCell.Const.identifier, for: indexPath) as! SystemListCell
        cell.label.text = modules[indexPath.row]
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onModuleTapped?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class SystemListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "SystemListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
