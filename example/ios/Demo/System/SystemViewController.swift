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

class SystemViewController: UITableViewController {

    private lazy var navigator: ViewControllerNavigator<SystemNavigationActions> = ViewControllerNavigator(parentVC: self) { action in
        switch action {
        case is SystemNavigationActions.Network: return NavigationSpec.Segue(identifier: "showNetwork")
        default: return NavigationSpec.Segue(identifier: "")
        }
    }
    private lazy var viewModel = SystemViewModel(navigator: navigator)

    private var systemFeatures = [String]()
    private var onSystemFeatureTapped: ((Int) -> Void)?
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_system".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.systemFeatures.observeInitialized { next in
                    let systemFeatures = next?.compactMap { $0 as? SystemFeatures } ?? []
                    self?.systemFeatures = systemFeatures.map { $0.name }
                    self?.onSystemFeatureTapped = { (index: Int) in viewModel.onButtonTapped(systemFeatures: systemFeatures[index]) }
                    self?.tableView.reloadData()
                }
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return systemFeatures.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: SystemListCell.Const.identifier, for: indexPath) { (cell: SystemListCell) in
            cell.label.text = systemFeatures[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSystemFeatureTapped?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class SystemListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "SystemListCell"
    }
    
    @IBOutlet weak var label: UILabel!
}
